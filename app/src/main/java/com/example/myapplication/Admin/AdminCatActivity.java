package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.example.myapplication.home;

public class AdminCatActivity extends AppCompatActivity {

    private ImageView tShirts, robe, jupe;

    private Button cosmeticBtn;
    private Button LogoutBtn, CheckorderBtn, maintainProductsBtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cat);


        cosmeticBtn = (Button) findViewById(R.id.cosmetic_btn);
        LogoutBtn = (Button) findViewById(R.id.logout_btn);
        CheckorderBtn = (Button) findViewById(R.id.Check_new_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);

        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCatActivity.this, home.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });



        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCatActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  );
                startActivity(intent);
                finish();
            }
        });

        CheckorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCatActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);

            }
        });


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
                Intent intent= new Intent(AdminCatActivity.this , AdminAddActivity.class);
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