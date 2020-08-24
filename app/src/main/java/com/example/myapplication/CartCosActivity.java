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

public class CartCosActivity extends AppCompatActivity {
    private RecyclerView reyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount , txtMsg1;
    private int overTotalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        reyclerView = findViewById(R.id.cart_list);
        reyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        reyclerView.setLayoutManager(layoutManager);
        NextProcessBtn= (Button) findViewById(R.id.next_process_btn);
        txtTotalAmount=(TextView)findViewById(R.id.total_price);
        txtMsg1=(TextView)findViewById(R.id.msg1);


        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {   txtTotalAmount.setText("Prix totale = " + String.valueOf(overTotalPrice));
                Intent intent = new Intent(CartCosActivity.this , ConfirmFinalOrderActivity.class);
                intent.putExtra("Prix totale", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrdersState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Cosmétiques"),Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText( "Quantité =" + model.getQuantity());
                holder.txtProductPrice.setText( "prix totale = " + model.getPrice() );
                holder.txtProductName.setText( model.getPname());
                try{
                    int oneTypeProductPrice =((Integer.valueOf(model.getPrice())))* Integer.valueOf(model.getQuantity());
                    overTotalPrice = overTotalPrice + oneTypeProductPrice;}
                catch(NumberFormatException e)
                {
                    int oneTypeProductPrice =0;
                }



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Modifier",
                                        "Supprimer"

                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(CartCosActivity.this);
                        builder.setTitle("Options du panier:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0)
                                {
                                    Intent intent = new Intent(CartCosActivity.this,CosmeticDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }
                                if (i == 1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Cosmétiques")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartCosActivity.this,"\n" + "Élément supprimé avec succès",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartCosActivity.this,home.class);
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
                    String userName = datasnapshot.child("name").getValue().toString();

                    if (shippingState.equals("expédiée"))
                    {
                        txtTotalAmount.setText("Dear" + userName + "\n \n" + "la commande est expédiée avec succès.");
                        reyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("\n" + "Félicitations, votre commande finale a été expédiée avec succès, vous recevrez bientôt votre commande à votre porte.");
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartCosActivity.this," vous pouvez acheter plus de produits quand vous avez reçu votre première commande",Toast.LENGTH_SHORT);



                    }
                    else if (shippingState.equals(" non expédiée"))
                    {
                        txtTotalAmount.setText("\n" + "État d'expédition = non expédié");
                        reyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartCosActivity.this," vous pouvez acheter plus de produits quand vous avez reçu votre première commande",Toast.LENGTH_SHORT);


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}