package com.example.myapplication;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Utilisateurs;
import com.example.myapplication.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.drawable.CircularProgressDrawable;

import java.security.Principal;

import io.paperdb.Paper;


public class principal extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_xml);
        mRecyclerView= findViewById(R.id.recyclerviewpr);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadingBar=new ProgressDialog(this);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPhoneKey!="" && UserPasswordKey!="")
        { if (!TextUtils.isEmpty(UserPhoneKey))
        { if (!TextUtils.isEmpty(UserPasswordKey))
            {  AllowAccess(UserPhoneKey,UserPasswordKey);
            loadingBar.setTitle("Already Logged");
            loadingBar.setMessage("Patientez s'il vous plait..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show(); }}


}



}

    private void AllowAccess(final String phone,final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    Utilisateurs usersData = dataSnapshot.child("Users").child(phone).getValue(Utilisateurs.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {

                            Toast.makeText(principal.this, "Patientez s'il vous plait,la connexion est réussie.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(principal.this, home.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(principal.this, "Mot de passe incorrecte.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(principal.this, "Compte avec " + phone + " n'existe pas.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@Nullable DatabaseError databaseError) {

            }
        });
    }
    }

