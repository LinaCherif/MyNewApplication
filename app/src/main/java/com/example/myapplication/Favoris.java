package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Cart;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Integer.*;

public class Favoris extends AppCompatActivity {
    private RecyclerView reyclerView;
    private RecyclerView.LayoutManager layoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        reyclerView = findViewById(R.id.fav_list);
        reyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        reyclerView.setLayoutManager(layoutManager);




    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference favListRef = FirebaseDatabase.getInstance().getReference().child("Favoris");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(favListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Produits"),Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText( "Quantité =" + model.getQuantity());
                holder.txtProductPrice.setText( "Prix = " + model.getPrice() );
                holder.txtProductName.setText( model.getPname());



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Supprimer"

                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(Favoris.this);
                        builder.setTitle("Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {

                                if (i == 0)
                                {
                                    favListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Produits")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(Favoris.this,"\n" + "Élément supprimé avec succès",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Favoris.this,home.class);
                                                        startActivity(intent);

                                                    }

                                                }
                                            });

                                }


                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;

            }
        };

        reyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}