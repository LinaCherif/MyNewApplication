package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText,addressEditText, cityEditText, sizeTxt;
    private Button confirmOrderBtn;
    private  String totalAmount = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Prix totale");
        Toast.makeText(this,"Prix totale =  Da " + totalAmount ,Toast.LENGTH_SHORT).show();

        confirmOrderBtn=(Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText =(EditText)findViewById(R.id.shippment_name);
        phoneEditText =(EditText)findViewById(R.id.shippment_phone_number);
        addressEditText =(EditText)findViewById(R.id.shippment_address);
        cityEditText =(EditText)findViewById(R.id.shippment_city);
        //sizeTxt = ( EditText)findViewById(R.id.taille);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }
    private void Check()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this,"\n" + "Merci de saisir votre nom complet", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this,"\n" + "Merci de saisir votre numéro de téléphone", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"\n" + "Merci de saisir votre Address", Toast.LENGTH_SHORT).show();
        }
         else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this,"Merci de saisir le nom de votre ville", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder()
    {   final String saveCurrentDate,saveCurrentTime;
        Calendar calForDate =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate= currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Ordres")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        //ordersMap.put("taille",sizeTxt.getText().toString());
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("state","not shipped");
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {  FirebaseDatabase.getInstance().getReference().child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                Toast.makeText(ConfirmFinalOrderActivity.this,"\n" + "votre commande finale a été passée avec succès",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ConfirmFinalOrderActivity.this, home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } }
                        });

            }
            }
        });

    }

}