package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Products;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduct  extends RecyclerView.Adapter<AdapterProduct.Myholder>{

    Context context;
    List<Products> cosmeticList;

    public AdapterProduct(Context context, List<Products>cosmeticList)
    {
        this.context=context;
        this.cosmeticList=cosmeticList;
    }




    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_items_layout, viewGroup, false);

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder myholder, int i) {

        String img = cosmeticList.get(i).getImage();
        final String name = cosmeticList.get(i).getPname();
        String des = cosmeticList.get(i).getDescription();
        String pr = cosmeticList.get(i).getPrice();

        myholder.mTitle.setText(name);
        myholder.mDes.setText(des);
        myholder.mprice.setText(pr);
        try {
            Picasso.get().load(img)
                    .placeholder(R.drawable.masque)
                    .into(myholder.mImaeView);
        }
        catch (Exception e)
        {

        }

        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+name,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cosmeticList.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        ImageView mImaeView;
        TextView mTitle, mDes, mprice;

        public Myholder (@NonNull View itemView) {
            super(itemView);
            this.mImaeView = itemView.findViewById(R.id.product_image);
            this.mTitle = itemView.findViewById(R.id.product_name);
            this.mDes = itemView.findViewById(R.id.product_description);
            this.mprice = itemView.findViewById(R.id.product_price);
        }

    }
}
