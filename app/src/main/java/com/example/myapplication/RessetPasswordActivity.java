package com.example.myapplication;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RessetPasswordActivity extends AppCompatActivity
{

    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title);
        titleQuestions = findViewById(R.id.title_question);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyButton = (Button) findViewById(R.id.verify_btn);

    }

    protected void onStart()
    {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);

        if (check.equals("paramètres"))
        {
            pageTitle.setText("Questions");
            titleQuestions.setText("Svp Répondre aux questions suivantes de sécurité?");
            verifyButton.setText("Confirmer");

            displayPreviousAnswers();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    setAnswers();
                }
            });
        }
        else if (check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    verifyUser();
                }
            });
        }
    }


    private void setAnswers()
    {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(question1.equals("") && question2.equals(""))
        {
            Toast.makeText(RessetPasswordActivity.this, "SVP répond à tout les deux questions.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Utilisateurs")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("Questions de Sécurité").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@Nullable Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RessetPasswordActivity.this, "Vous avez répondu aux questions avec succé",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RessetPasswordActivity.this, home.class);
                                startActivity(intent);
                            }

                        }
                    });
        }

    }

    private void displayPreviousAnswers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Utilisateurs")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Questions de Sécurité").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String ans1= dataSnapshot.child("answer1").getValue().toString();
                    String ans2= dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@Nullable DatabaseError error) {

            }
        });

    }

    private void verifyUser()
    {
        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") &&!answer2.equals(""))
        {
            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Utilisateurs")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@Nullable  DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        String mPhone = dataSnapshot.child("phone").getValue().toString();

                        if (dataSnapshot.hasChild("Questions de Sécurité"))
                        {
                            String ans1= dataSnapshot.child("Questions de Sécurité").child("answer1").getValue().toString();
                            String ans2= dataSnapshot.child("Questions de Sécurité").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1))
                            {
                                Toast.makeText(RessetPasswordActivity.this,"Votre première réponse est fausse!",Toast.LENGTH_SHORT).show();

                            }
                            else if(!ans2.equals(answer2))
                            {
                                Toast.makeText(RessetPasswordActivity.this,"Votre deuxième réponse est fausse!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RessetPasswordActivity.this);
                                builder.setTitle("Nouveau mot de passe");

                                final EditText newPassword = new EditText(RessetPasswordActivity.this);
                                newPassword.setHint("Ecrivez votre mot de passe ici...");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Changer", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if(!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@Nullable Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(RessetPasswordActivity.this,"Votre mot de passe est réintialisé avec succé",Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(RessetPasswordActivity.this, Login.class);
                                                                startActivity(intent);

                                                            }

                                                        }
                                                    });
                                            ref.child("password2")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@Nullable Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                Intent intent = new Intent(RessetPasswordActivity.this, Login.class);
                                                                startActivity(intent);

                                                            }

                                                        }
                                                    });
                                        }

                                    }
                                });
                                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.cancel();

                                    }
                                });
                                builder.show();
                            }


                        }
                        else
                        {
                            Toast.makeText(RessetPasswordActivity.this,"Vous n'avez pas répondu aux questions de sécurité.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        {
                            Toast.makeText(RessetPasswordActivity.this,"Ce numéro n'existe pas!.",Toast.LENGTH_SHORT).show();

                        }

                }

                @Override
                public void onCancelled(@Nullable DatabaseError error)
                {

                }
            });

        }
        else
            {
                Toast.makeText(RessetPasswordActivity.this,"Veuillez remplir le formulaire.",Toast.LENGTH_SHORT).show();
            }


    }
}