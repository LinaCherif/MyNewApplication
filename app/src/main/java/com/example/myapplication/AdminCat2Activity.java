package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCat2Activity extends AppCompatActivity {

    private ImageView creme,pom,pommade;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cosmetic);


        pom=(ImageView)findViewById(R.id.glasses);
        pommade=(ImageView)findViewById(R.id.purses_bags_wallets);
        creme=(ImageView)findViewById(R.id.hats_caps);


        pom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCat2Activity.this ,AdminAdd2Activity.class);
                intent.putExtra("categorie","Gommage");
                startActivity(intent);
            }
        });
        pommade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCat2Activity.this ,AdminAdd2Activity.class);
                intent.putExtra("categorie","Masque");
                startActivity(intent);
            }
        });
        creme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCat2Activity.this ,AdminAdd2Activity.class);
                intent.putExtra("categorie","Crème");
                startActivity(intent);
            }
        });

    }}
