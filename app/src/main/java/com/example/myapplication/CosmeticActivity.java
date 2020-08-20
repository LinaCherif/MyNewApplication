package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Products;
import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import io.paperdb.Paper;

public class CosmeticActivity extends AppCompatActivity {
    private DatabaseReference CosRef;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutMan;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.cosmetic_page );

        CosRef= FirebaseDatabase.getInstance().getReference().child("Cosmétiques");

        Paper.init(this);

        Toolbar toolb = (Toolbar) findViewById(R.id.toolb);
        toolb.setTitle("Cosmétiques");
        setSupportActionBar(toolb);


        recyclerview = findViewById( R.id.cosmetic_recyclerview);
        recyclerview.setHasFixedSize(true);
        layoutMan = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutMan);


    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions < Products > options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(CosRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter <Products, ProductViewHolder > adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @NonNull

                    @Override
                    protected void onBindViewHolder(@Nullable ProductViewHolder holder, int position, @Nullable Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerview.setAdapter(adapter);
        adapter.startListening();
    }


}