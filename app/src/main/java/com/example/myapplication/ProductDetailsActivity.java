package com.example.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton NumberButton;
    private TextView productPrice, productDescription,productName;
    private String productID ="" , state ="Normal";

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_product_details);

         productID = getIntent().getStringExtra("pid");
         addToCartButton=(Button) findViewById(R.id.pd_add_to_cart_button);
         //addTocartBtn=(FloatingActionButton) findViewById(R.id.add_product_to_cart_btn);
         NumberButton=(ElegantNumberButton) findViewById(R.id.number_btn);
         productImage=(ImageView) findViewById(R.id.product_image_details);
         productName=(TextView) findViewById(R.id.product_name_details);
         productDescription=(TextView) findViewById(R.id.product_description_details);
         productPrice=(TextView) findViewById(R.id.product_price_details);

         getProductDetails(productID);

         addToCartButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 addingToCartList();

                 if (state.equals("Commande passée") || state.equals("\n" + "Commande expédiée"))
                 {
                     Toast.makeText(ProductDetailsActivity.this,"\n" + "Vous pouvez acheter plus de produits, une fois votre commande expédiée ou confirmée \"",Toast.LENGTH_LONG).show();
             }

                 else
                 {

                   addingToCartList();

             } }
         });



}

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrdersState();
    }

    private void  addingToCartList()
    {  String saveCurrentDate, saveCurrentTime;
        Calendar calForDate =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate= currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calForDate.getTime());

       final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",NumberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Produits").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                { cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                        .child("Produits").child(productID)
                        .updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(ProductDetailsActivity.this,"Ajouté au panier",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProductDetailsActivity.this, home.class);
                                    startActivity(intent);

                                }
                            }
                        });

                }
            }
        });


    }

    private void getProductDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Produits");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice() );
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void CheckOrdersState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Ordres").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if (datasnapshot.exists())
                {
                    String shippingState = datasnapshot.child("state").getValue().toString();


                    if (shippingState.equals("expédiée"))
                    {
                        state="Commande expédiée";



                    }
                    else if (shippingState.equals(" non expédiée"))
                    {
                        state = "Commande passée";


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}


