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
import com.squareup.picasso.Target;

import java.util.List;

public class AdapterProduct  extends RecyclerView.Adapter<AdapterProduct.Myholder>{


    private Context context;
    private List<Products> vet_list;

    public AdapterProduct(Context context,List<Products>vet_list)
    {
        this.context=context;
        this.vet_list=vet_list;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.product_items_layout, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder myholder, int i) {

        Products vet = vet_list.get(i);
        myholder.mTitle.setText(vet.getPname());
        myholder.mDes.setText(vet.getDescription());
        myholder.mprice.setText(vet.getPrice());

        Picasso.get()
                .load(vet.getImage())
                .placeholder(R.drawable.vetement)
                .fit()
                .centerCrop()
                .into ( myholder.mImaeView);

    }

    @Override
    public int getItemCount() {

          return vet_list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        private ImageView mImaeView;
        private TextView mTitle, mDes, mprice;

        public Myholder (@NonNull View itemView) {
            super(itemView);
            this.mImaeView = itemView.findViewById(R.id.product_imag);
            this.mTitle = itemView.findViewById(R.id.product_nam);
            this.mDes = itemView.findViewById(R.id.product_desc);
            this.mprice = itemView.findViewById(R.id.product_pri);
        }

    }
}
