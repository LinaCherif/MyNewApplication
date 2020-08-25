package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Products;
import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.example.myapplication.ViewHolder.VetmentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class VetementActivity extends AppCompatActivity {
    private DatabaseReference vetRef;
    private RecyclerView recyclerv;
    private RecyclerView recycler;
    private AdapterProduct vAdapter;
    private VetAdapter VetAdapter;
    private List<Products>vet_list;
    private List<Products>vet_list2;
    RecyclerView.LayoutManager layoutMn;
    FirebaseDatabase data;
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.vet_page );

        vetRef= FirebaseDatabase.getInstance().getReference().child("Vêtements");

        Paper.init(this);

      Toolbar tool = (Toolbar) findViewById(R.id.tool);
        tool.setTitle("I&L Vêtements");
        setSupportActionBar(tool);

        /*FloatingActionButton vet_cart = (FloatingActionButton) findViewById(R.id.vet_cart);
        vet_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!type.equals("Admins")) {
                    Intent intent = new Intent( VetementActivity.this , CartVetActivity.class );
                    startActivity( intent );
                }

            }
        });*/

        //feuilleter();
        //nvlCollec();


        recycler = findViewById( R.id.vet_recyclerview);
        recycler.setHasFixedSize(true);
        layoutMn = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutMn);



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
                        .setQuery(vetRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter <Products, ProductViewHolder > adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @NonNull

                    @Override
                    protected void onBindViewHolder(@Nullable ProductViewHolder holder, int position, @Nullable final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() + "Da");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        if (!type.equals("Admins")) {
                            holder.itemView.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent( VetementActivity.this , VetementDetailsActivity.class );
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
        recycler.setAdapter(adapter);
        adapter.startListening();
    }

   /*public  void nvlCollec()
   {
       recyclerv = findViewById( R.id.nvl_collection );
       recyclerv.setHasFixedSize( true );
       recyclerv.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false ) );

       vet_list =  new ArrayList <>( );
       vAdapter = new AdapterProduct( this,vet_list );
       recyclerv.setAdapter( vAdapter );
       //nvlRef = FirebaseDatabase.getInstance().getReference("Vêtements");
       Query query = FirebaseDatabase.getInstance().getReference("Vêtements")
               .limitToFirst( 5 );
       query.addListenerForSingleValueEvent(valueEventListener);

   }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for ( DataSnapshot postSnaphsot: snapshot.getChildren())
            {
                Products products = postSnaphsot.getValue(Products.class);
                vet_list.add( products );

            }

            vAdapter= new AdapterProduct( VetementActivity.this, vet_list );
            recyclerv.setAdapter( vAdapter );

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText( VetementActivity.this,  error.getMessage(), Toast.LENGTH_LONG).show();

        }
    };

   public void  feuilleter()
   {
       recycler = findViewById( R.id.vet_recycler);
       recycler.setHasFixedSize( true );
       recycler.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false ) );
       vet_list2 =  new ArrayList <>( );
       vetRef = FirebaseDatabase.getInstance().getReference("Vêtements");
       vetRef.addValueEventListener( new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for ( DataSnapshot postSnaphsot: snapshot.getChildren())
               {
                   Products products = postSnaphsot.getValue(Products.class);
                   vet_list2.add( products );

               }

               VetAdapter= new VetAdapter( VetementActivity.this, vet_list2 );
               recycler.setAdapter( VetAdapter );

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText( VetementActivity.this,  error.getMessage(), Toast.LENGTH_LONG).show();


           }
       } );

        //Intent intent = new Intent(VetementActivity.this , ProductDetailsActivity.class);
       //startActivity(intent);
       //finish();



   }*/

}
