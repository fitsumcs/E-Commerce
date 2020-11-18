package com.example.dailyshoping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth= FirebaseAuth.getInstance();

        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }
        },3000);
    }
}