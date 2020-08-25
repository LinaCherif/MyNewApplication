package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Model.AdminOrders;
import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Ordres");
        ordersList= findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders>options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();
       FirebaseRecyclerAdapter<AdminOrders,AdminOrderViewHolder> adapter =
               new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int i, @NonNull final AdminOrders model) {
                       holder.userName.setText("Nom: " + model.getName());
                       holder.userPhoneNumber.setText("Numéro de téléphone: " + model.getPhone());
                       holder.userTotalPrice.setText("Montant total: DA" + model.getTotalAmount());
                       holder.userShippingAddress.setText("Adresse de livraison: " + model.getAddress() + " " + model.getCity());
                       //holder.userSize.setText("Taille: " + model.getTaille() );
                       holder.userDateTime.setText("Commande à: " + model.getDate() +" " + model.getTime() );

                       holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               String uID= getRef(i).getKey();

                               Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                               intent.putExtra("uid",uID);
                               startActivity(intent);

                           }
                       });
                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               CharSequence options[]= new CharSequence[]
                                       {      "OUI",
                                               "NON"
                           };
                               AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                               builder.setTitle("Avez vous expédié les produits de cette commande ?");

                               builder.setItems(options, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {

                                       if (i==0)
                                       {
                                           String uID= getRef(i).getKey();
                                          RemoverOrder(uID);
                                       }
                                       else
                                       {
                                           finish();


                                       }
                                   }
                               });
                               builder.show();
                           }
                       });



                   }

                   @NonNull
                   @Override
                   public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                       return new AdminOrderViewHolder(view);
                   }
               };
       ordersList.setAdapter(adapter);
       adapter.startListening();
    }
    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhoneNumber , userTotalPrice,userDateTime, userShippingAddress, userSize ;
        public Button ShowOrdersBtn;
        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userName =itemView.findViewById(R.id.order_user_name);
            userPhoneNumber =itemView.findViewById(R.id.order_phone_number);
            userTotalPrice =itemView.findViewById(R.id.order_total_price);
            userDateTime =itemView.findViewById(R.id.order_date_time);
            userShippingAddress =itemView.findViewById(R.id.order_address);
            ShowOrdersBtn =itemView.findViewById(R.id.show_all_products);
          //  userSize =itemView.findViewById(R.id.size);



        }
    }

    private void RemoverOrder(String uID){
        ordersRef.child(uID).removeValue();

    }
}