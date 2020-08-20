package com.example.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Interface.ItemClickListner;
import com.example.myapplication.R;

public class VetmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice, txtTaille;
    public ImageView imageView;
    public ItemClickListner listner;


    public VetmentViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_imag);
        txtProductName = (TextView) itemView.findViewById(R.id.product_nam);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_desc);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_pri);
        txtTaille = (TextView) itemView.findViewById(R.id.taille);
        itemView.setOnClickListener(this);
    }


    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}