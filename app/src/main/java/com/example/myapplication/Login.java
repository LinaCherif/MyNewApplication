package com.example.myapplication;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Admin.AdminCatActivity;
import com.example.myapplication.Model.Utilisateurs;
import com.example.myapplication.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;
public class Login extends AppCompatActivity {
    private TextView inscrire;
    private EditText InputPhone, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink , NotAdminLink,ForgetPasswordLink;
    private String parentDbName = "Utilisateurs";
    private CheckBox chkboxrememberme;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_xml);

        inscrire = (TextView) findViewById(R.id.textViewRegister);
        LoginButton = (Button) findViewById(R.id.BtLogin);
        InputPassword = (EditText) findViewById(R.id.Etpassword);
        InputPhone = (EditText) findViewById(R.id.Etname);
        loadingBar = new ProgressDialog(this);
        AdminLink=(TextView) findViewById(R.id.admin) ;
        NotAdminLink=(TextView) findViewById(R.id.notadmin);
        ForgetPasswordLink=findViewById(R.id.forget);
        chkboxrememberme=(CheckBox) findViewById(R.id.rememberme);
        Paper.init(this);





        //Bouton sinscrire
        inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Inscription.class);
                startActivity(intent);
                finish();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });
        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RessetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(view.INVISIBLE);
                NotAdminLink.setVisibility(view.VISIBLE);
                parentDbName ="Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(view.VISIBLE);
                NotAdminLink.setVisibility(view.INVISIBLE);
                parentDbName ="Utilisateurs";


            }
        });
    }
    private void LoginUser()
    {
        String phone = InputPhone.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "\n" + "Veuillez écrire votre numéro de téléphone.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Veuillez écrire votre mot de passe.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Compte login");
            loadingBar.setMessage("Veuillez patienter pendant que nous vérifions les informations d'identification.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password);
        }
    }
    private void AllowAccessToAccount(final String phone, final String password)
    { if (chkboxrememberme.isChecked())
       {
           Paper.book().write(Prevalent.UserPhoneKey,phone);
           Paper.book().write(Prevalent.UserPasswordKey,password);
       }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Utilisateurs usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Utilisateurs.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(Login.this, "Bienvenue Admin, Vous étes connectés!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(Login.this, AdminCatActivity.class);

                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Utilisateurs"))
                            {
                                Toast.makeText(Login.this, "la connexion est réussie.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Login.this, home.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);

                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(Login.this, "Mot de passe incorrecte.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "Compte avec " + phone + " n'existe pas.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@Nullable DatabaseError databaseError) {

            }
        });
    }

}
