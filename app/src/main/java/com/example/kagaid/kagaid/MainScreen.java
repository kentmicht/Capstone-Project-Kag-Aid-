package com.example.kagaid.kagaid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hamza.slidingsquaresloaderview.SlidingSquareLoaderView;

import maes.tech.intentanim.CustomIntent;

public class MainScreen extends AppCompatActivity {
    SlidingSquareLoaderView squareLoaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        }else {
            toastMessage("No Internet Connection");
        }

        setContentView(R.layout.activity_main_screen);

        squareLoaderView=(SlidingSquareLoaderView)findViewById(R.id.loader);
        squareLoaderView.start();

        Thread loaderThread=new Thread(new Runnable() {
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

        loaderThread.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
