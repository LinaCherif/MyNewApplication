package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCatActivity extends AppCompatActivity {

    private ImageView tShirts, robe, jupe;

    private Button cosmeticBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cat);

        cosmeticBtn = (Button) findViewById(R.id.cosmetic_btn);

        cosmeticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCatActivity.this, AdminCat2Activity.class);
                intent.putExtra("Admin","ajout cosmetic");
                startActivity(intent);
            }
        });

        tShirts = (ImageView) findViewById(R.id.t_shirts);
        robe =(ImageView)findViewById(R.id.female_dresses);
        jupe=(ImageView)findViewById(R.id.sports_t_shirts);


        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCatActivity.this ,AdminAddActivity.class);
                intent.putExtra("categorie","Haut");
                startActivity(intent);
            }

        });
        robe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCatActivity.this ,AdminAddActivity.class);
                intent.putExtra("categorie","Robe");
                startActivity(intent);

            }
        });
        jupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCatActivity.this ,AdminAddActivity.class);
                intent.putExtra("categorie","Bat");
                startActivity(intent);
            }
        });


    }
}