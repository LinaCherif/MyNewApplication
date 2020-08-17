package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Products;
import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CosmeticFragment extends Fragment {

    RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    View view;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    FirebaseDatabase database;
    private DatabaseReference cosmetiques;


    public  CosmeticFragment(){

    }

    /*public void onCreate(@NonNull Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        cosmetiques = database.getReference("Cosmétiques");
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cosmetic, container,false);

        recyclerview = (RecyclerView) view.findViewById(R.id.cosmetic_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerview.setHasFixedSize(true);

        cosmetiques = FirebaseDatabase.getInstance().getReference().child("Cosmétiques");

        //getAllCosmetic();

        return view;
    }
    /*public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(CosmeticRef, Products.class)
                        .build();

    }*/
    /*private void getAllCosmetic()
    {
        adapter= new FirebaseRecyclerAdapter<Products, ProductViewHolder>(
                Products.class, R.layout.fragment_cosmetic,
                ProductViewHolder.class,
                cosmetiques)
        {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {

            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int position, @NonNull final Products products)
            {
               viewHolder.txtProductName.setText(products.getPname());
                Picasso.with(getActivity())
                        .load(products.getImage())
                        .into(viewHolder.imageView);

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), String.format("%s|%s",adapter.getRef(position).getKey(),products.getPname()), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };

        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
    }*/
}