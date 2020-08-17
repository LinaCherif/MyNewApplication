package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Inscription extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName, InputPhone,InputPassword,InputPassword2;
    private ProgressDialog loadingBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);

         CreateAccountButton= (Button) findViewById(R.id.Btinscris);
         InputName= (EditText) findViewById(R.id.Etmail);
         InputPhone= (EditText) findViewById(R.id.Etnum);
         InputPassword= (EditText) findViewById(R.id.editTextTextPassword);
         InputPassword2= (EditText) findViewById(R.id.editTextTextPassword2);
         loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }


    });
}
    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputPhone.getText().toString();
        String password = InputPassword.getText().toString();
        String password2 = InputPassword2.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Veuillez taper votre nom!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Veuillez taper votre numéro de téléphone!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "veuillez taper votre mot de passe", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password2))
        {
            Toast.makeText(this, "veuillez confirmer votre mot de passe", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals(password2))
        {
            loadingBar.setTitle("Créer un compte");
            loadingBar.setMessage("Veuillez patienter pendant que nous vérifions les informations d'identification.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name, phone, password, password2);
        }
        else{
            Toast.makeText(this, "veuillez reconfirmer votre mot de passe", Toast.LENGTH_SHORT).show();
        }
    }
    private void ValidatephoneNumber(final String name, final String phone, final String password, final String password2)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Utilisateurs").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("password2", password2);
                    userdataMap.put("name", name);

                    RootRef.child("Utilisateurs").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@Nullable Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Inscription.this, "Félicitations, votre compte a été créé.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Inscription.this, Login.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Inscription.this, "Erreur réseau: veuillez réessayer après un certain temps...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Inscription.this, "This " + phone + " \n" + "existe déjà.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(Inscription.this, "Veuillez réessayer en utilisant un autre numéro de téléphone.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Inscription.this, Login.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@Nullable DatabaseError databaseError) {

            }
        });
    }
}
