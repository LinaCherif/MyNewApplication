package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class zSplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN = 3000;
    //variables
    Animation topAnim, bottomAnim;
    TextView NameApp, store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);

        //Animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        NameApp = findViewById(R.id.textviewIL);
        store = findViewById(R.id.textViewstore);
        NameApp.setAnimation(topAnim);
        store.setAnimation(bottomAnim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(zSplashScreen.this , Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }

}