package com.example.myapplication;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductDetailsActivity extends AppCompatActivity {
    private FloatingActionButton addTocartBtn;
    private ImageView productImage;
    private ElegantNumberButton NumberButton;
    private TextView productPrice, productDescription,productName;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_product_details);

         addTocartBtn=(FloatingActionButton) findViewById(R.id.add_product_to_cart_btn);
         NumberButton=(ElegantNumberButton) findViewById(R.id.number_btn);
         productImage=(ImageView) findViewById(R.id.product_image_details);
         productName=(TextView) findViewById(R.id.product_name_details);
         productDescription=(TextView) findViewById(R.id.product_description_details);
         productPrice=(TextView) findViewById(R.id.product_price_details);

        /*DIRI HNA L CODE TAE VIDEO 21 */



}}


