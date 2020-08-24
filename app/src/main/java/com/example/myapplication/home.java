package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myapplication.Admin.AdminMaintainProductsActivity;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


 public class home extends AppCompatActivity
         implements NavigationView.OnNavigationItemSelectedListener {
     private DatabaseReference ProductsRef;
     private RecyclerView recyclerView;
     RecyclerView.LayoutManager layoutManager;
     private String type = "";
     private Button favBtn;
     private ImageView productImage;
     private TextView productPrice, productDescription,productName;
     private String productID ="" ;




     FirebaseDatabase firebaseDatabase;
     ActionBar actionBar;

     @SuppressLint("WrongViewCast")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_hom);
         productID = getIntent().getStringExtra("pid");
         productImage=(ImageView) findViewById(R.id.product_image);
         productName=(TextView) findViewById(R.id.product_name);
         productDescription=(TextView) findViewById(R.id.product_description);
         productPrice=(TextView) findViewById(R.id.product_price);
         favBtn = (ToggleButton) findViewById( R.id.fav_prod );






         Intent intent = getIntent();
         Bundle bundle = intent.getExtras();
         if (bundle != null) {
             type = getIntent().getExtras().get("Admins").toString();


         }


         ProductsRef = FirebaseDatabase.getInstance().getReference().child("Produits");
         //cosRef= FirebaseDatabase.getInstance().getReference().child("Cosmétiques");



         Paper.init(this);


         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitle("I&L Shop");
         setSupportActionBar(toolbar);

         FloatingActionButton fa = (FloatingActionButton) findViewById(R.id.fa);
         fa.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (!type.equals("Admins")) {
                     Intent intent = new Intent(home.this, SearchProductsActivity.class);
                     startActivity(intent);
                 }
             }
         });


         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 if(!type.equals("Admins")) {
                     Intent intent = new Intent( home.this , CartActivity.class );
                     startActivity( intent );
                 }

             }
         });

         /*favBtn = (Button) findViewById( R.id.fav_prod );
         setContentView( R.layout.product_items_layout );
         favBtn.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 addingToFavList();
             }
         } );*/





         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                 this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.addDrawerListener(toggle);
         toggle.syncState();


         NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
         nav.setNavigationItemSelectedListener(this);
         nav.getMenu().getItem(0).setChecked(true);


         View headerView = nav.getHeaderView(0);
         TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
         CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

         if (!type.equals("Admins")) {
         userNameTextView.setText(Prevalent.currentOnlineUser.getName());
         Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
         }


        recyclerView = findViewById( R.id.recycler_menu );
        recyclerView.setHasFixedSize(true);
         layoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);


     }


     public void onCustom(View view) {
         Toast.makeText( this , "Produit favoris" , Toast.LENGTH_SHORT ).show();
        // addingToFavList();
     }
     /*private void  addingToFavList()
     {  String saveCurrentDate, saveCurrentTime;
         Calendar calForDate =  Calendar.getInstance();
         SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
         saveCurrentDate= currentDate.format(calForDate.getTime());

         SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
         saveCurrentTime= currentTime.format(calForDate.getTime());

         final DatabaseReference favListRef = FirebaseDatabase.getInstance().getReference().child("Favoris");
         final HashMap<String, Object> cartMap = new HashMap<>();
         cartMap.put("pid",productID);
         //cartMap.put("pname",productName.getText().toString());
         //cartMap.put("price",productPrice.getText().toString());
         cartMap.put("date",saveCurrentDate);
         cartMap.put("time",saveCurrentTime);

                          favListRef.child(productID)
                                 .updateChildren(cartMap)
                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if (task.isSuccessful())
                                         {
                                             Toast.makeText(home.this,"Ajouté au favoris",Toast.LENGTH_SHORT).show();
                                             Intent intent = new Intent(home.this, home.class);
                                             startActivity(intent);

                                         }
                                     }
                                 });




     }*/



     @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions <Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter <Products, ProductViewHolder > adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @NonNull

                    @Override
                    protected void onBindViewHolder(@Nullable ProductViewHolder holder, int position, @Nullable final Products model)
                    {


                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText( model.getPrice()+ "DA");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admins"))
                                {
                                    Intent intent = new Intent(home.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }
                                else {
                                    Intent intent = new Intent(home.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }



                            }
                        });


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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


     public void onBackPressed() {
         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         if (drawer.isDrawerOpen(GravityCompat.START)) {
             drawer.closeDrawer(GravityCompat.START);
         } else {
             super.onBackPressed();
         }
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.hom, menu);
         return true;
     }


     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

         return super.onOptionsItemSelected(item);
     }


     //@SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(MenuItem item) {
         // Handle navigation view item clicks here.
         int id = item.getItemId();

         if (id == R.id.nav_cart) {
             if (!type.equals("Admins")) {
                 Intent intent = new Intent(home.this, CartActivity.class);
                 startActivity(intent);

             }

         }
         else if (id == R.id.nav_fav) {
             if(!type.equals("Admins")) {
                 Intent intent = new Intent(home.this, Favoris.class);
                 startActivity(intent);

             }

         }
         else if (id == R.id.nav_cos) {
             if(!type.equals("Admins")) {
                 Intent intent = new Intent(home.this, CosmeticActivity.class);
                 startActivity(intent);
             }

         } else if (id == R.id.nav_vet) {
             if(!type.equals("Admins")) {
                 Intent intent = new Intent( home.this , VetementActivity.class );
                 startActivity( intent );
             }

         }else if (id == R.id.nav_manage) {
             if (!type.equals("Admins")) {
             Intent intent = new Intent(home.this, SettinsActivity.class);
             startActivity(intent);
             }
         } else if (id == R.id.nav_logout) {
             if (!type.equals("Admins")) {
                 Paper.book().destroy();

                 Intent intent = new Intent(home.this, Login.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
                 finish();

             }
             Paper.book().destroy();


         }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
 }

}