package com.example.kagaid.kagaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hamza.slidingsquaresloaderview.SlidingSquareLoaderView;

import maes.tech.intentanim.CustomIntent;

public class MainScreen extends AppCompatActivity {
    SlidingSquareLoaderView squareLoaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        squareLoaderView=(SlidingSquareLoaderView)findViewById(R.id.loader);
        squareLoaderView.start();

        Thread xyz=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    java.lang.Thread.sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainScreen.this, LogIn.class));
                            CustomIntent.customType(MainScreen.this, "fadein-to-fadeout");
                        }
                    });
                }

            }
        });

        xyz.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
