package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


 public class home extends AppCompatActivity
         implements NavigationView.OnNavigationItemSelectedListener {
     private FrameLayout frameLayout;
     private DatabaseReference ProductsRef;
     private DatabaseReference VetRef;
     private DatabaseReference CosmeticRef;
     private RecyclerView recyclerView;
     RecyclerView.LayoutManager layoutManager;
     private String type = "";
     BottomNavigationView bottomNavigationView;

     FirebaseDatabase firebaseDatabase;
     ActionBar actionBar;

     @SuppressLint("WrongViewCast")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_hom);
         bottomNavigationView= (BottomNavigationView)findViewById(R.id.navigation);


         Intent intent = getIntent();
         Bundle bundle = intent.getExtras();
         if (bundle != null) {
             type = getIntent().getExtras().get("Admins").toString();


         }
         firebaseDatabase = FirebaseDatabase.getInstance();
         BottomNavigationView navigationView= findViewById(R.id.navigation);
         navigationView.setOnNavigationItemSelectedListener(selectedListner);
         //actionBar.setTitle("Vêtements");
         VetementFragment fragment1 = new VetementFragment();
         FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
         ft1.replace(R.id.content, fragment1, "");
         ft1.commit();


         //CosmeticRef = FirebaseDatabase.getInstance().getReference().child("Cosmétiques");


         Paper.init(this);


         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitle("I&L Store");
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
             public void onClick(View view) {
                 Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                         .setAction("Action", null).show();
             }
         });


         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                 this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.addDrawerListener(toggle);
         toggle.syncState();


         NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
         nav.setNavigationItemSelectedListener(this);
         nav.getMenu().getItem(0).setChecked(true);

         //frameLayout = findViewById(R.id.home_framelayout);
         //setFragment(new VetementFragment());
         //setFragment(new CosmeticFragment());

         View headerView = nav.getHeaderView(0);
         TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
         CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

         userNameTextView.setText(Prevalent.currentOnlineUser.getName());
         Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        /*recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/
     }

     BottomNavigationView.OnNavigationItemSelectedListener selectedListner =
             new BottomNavigationView.OnNavigationItemSelectedListener()
             {
                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem item)  {
                     return false;
                 }

                 public boolean onNavigationItemSelectedListner(@NonNull MenuItem menuItem)
                 {
                     switch (menuItem.getItemId())
                     {
                         case R.id.nav_vet:
                             actionBar.setTitle("Vêtements");
                             VetementFragment fragment1 = new VetementFragment();
                             FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                             ft1.replace(R.id.content, fragment1, "");
                             ft1.commit();
                             return true;
                         case R.id.nav_vis:
                             actionBar.setTitle("Cosmétiques");
                             CosmeticFragment fragment2 = new CosmeticFragment();
                             FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                             ft2.replace(R.id.content, fragment2, "");
                             ft2.commit();

                             return true;
                     }
                     return false;
                 }

             };



    /* @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(CosmeticRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }*/


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


     @SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(MenuItem item) {
         // Handle navigation view item clicks here.
         int id = item.getItemId();

         if (id == R.id.nav_cart) {

         } else if (id == R.id.nav_fav) {

         } else if (id == R.id.nav_orders) {

         } else if (id == R.id.nav_categories) {

         } else if (id == R.id.nav_manage) {
             Intent intent = new Intent(home.this, SettinsActivity.class);
             startActivity(intent);
         } else if (id == R.id.nav_logout) {
             Paper.book().destroy();

             Intent intent = new Intent(home.this, Login.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);
             finish();
         }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
 }

    /*private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }*/
}