package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.AdminMaintainProductsActivity;
import com.example.myapplication.Model.Products;
import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.cosmetic_page );

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admins").toString();


        }

        CosRef= FirebaseDatabase.getInstance().getReference().child("Cosmétiques");

        Paper.init(this);

        Toolbar toolb = (Toolbar) findViewById(R.id.toolb);
        toolb.setTitle("I&L Cosmétiques");
        setSupportActionBar(toolb);

        /*FloatingActionButton cos_cart = (FloatingActionButton) findViewById(R.id.cos_cart);
        cos_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!type.equals("Admins")) {
                    Intent intent = new Intent( CosmeticActivity.this , CartCosActivity.class );
                    startActivity( intent );
                }

            }
        });*/


        recyclerview = findViewById( R.id.cosmetic_recyclerview);
        recyclerview.setHasFixedSize(true);
        layoutMan = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutMan);


    }
    public void onCustom(View view) {
        Toast.makeText( this , "Produit favoris" , Toast.LENGTH_SHORT ).show();
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
                    protected void onBindViewHolder(@Nullable ProductViewHolder holder, int position, @Nullable final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() + "DA");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        if (!type.equals("Admins")) {
                            holder.itemView.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent( CosmeticActivity.this , CosmeticDetailsActivity.class );
                                    intent.putExtra( "pid" , model.getPid() );
                                    startActivity( intent );

                                }
                            } );
                        }
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
       // Intent intent = new Intent(CosmeticActivity.this , ProductDetailsActivity.class);
        //startActivity(intent);
       // finish();
    }


}