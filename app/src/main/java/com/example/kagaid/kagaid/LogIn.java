package com.example.kagaid.kagaid;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Patient.ViewPatientInfo;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.kagaid.kagaid.Homepage;

import com.example.kagaid.kagaid.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class LogIn extends AppCompatActivity {
    //Firebase
    DatabaseReference databaseLogin;
    DatabaseReference databaseBarangay;
    User u = new User();

    private static final String TAG = "Login";
    private static int TIME_OUT = 3000; //Time to launch the another activity

    EditText username;
    EditText password;

    String barangayName;
    String barangayId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Internet Connectivity
        if(isNetworkAvailable() == false){
            toastMessage("No Internet Connection");
        }


        username = (EditText) findViewById(R.id.loginUser);
        password = (EditText) findViewById(R.id.loginPwd);

        //firebase
        databaseLogin = FirebaseDatabase.getInstance().getReference("users");
        databaseBarangay = FirebaseDatabase.getInstance().getReference("barangay");


    }

    @Override
    protected void onStart() {
        super.onStart();

        Button login = (Button) findViewById(R.id.loginBtn);

        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                goToHomepage();
            }
        });

    }

    public void goToHomepage() {
        //toastMessage("You are here!");
//        DatabaseReference usernameRef = ref.child(username.getText().toString());
//        final DatabaseReference passwordDetailsRef = usernameRef.child(password.getText().toString());
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            if(TextUtils.isEmpty(username.getText().toString()) == true || TextUtils.isEmpty(password.getText().toString()) == true) {
                Toast.makeText(LogIn.this, "You did not enter a username/password", Toast.LENGTH_LONG).show();
            }else{
                databaseLogin.addValueEventListener(new ValueEventListener() {
                    boolean userName = false;
                    boolean pass = false;
                    String uId = null;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String md5HashPass = md5(password.getText().toString());
//                    toastMessage("Inputted Password:" + md5HashPass);

                        for(DataSnapshot loginSnapshot: dataSnapshot.getChildren()){
                            if(username.getText().toString().equals(loginSnapshot.child("username").getValue().toString())){
                                //toastMessage("Got it Username!");
                                userName = true;

                                if(md5HashPass.equals(loginSnapshot.child("password").getValue().toString())){
                                    //toastMessage("Got it Password!");
                                    pass = true;
                                    if(userName == true && pass == true){
                                        uId = loginSnapshot.child("uId").getValue().toString();
                                        barangayId = loginSnapshot.child("bId").getValue().toString();
//                                    toastMessage(uId);
                                    }
                                }
                            }
                        }
                        toastMessage(barangayId);

                        if(userName == false && pass == false){
                            toastMessage("Unregistered Account or Incorrect username/password");
                        }else if(userName == false && pass == true){
                            toastMessage("You did not enter a correct username");
                        }else if(userName == true && pass == false){
                            toastMessage("You did not enter a correct password");
                        }else if(userName == true && pass == true){
                            databaseBarangay.addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     for(DataSnapshot barangaySnapshot: dataSnapshot.getChildren()) {
                                        if(barangayId.equals(barangaySnapshot.child("bId").getValue().toString())){
                                            barangayName = barangaySnapshot.child("name").getValue().toString();
                                        }
                                     }
                                     progressDialog(barangayName);
                                     new Handler().postDelayed(new Runnable() {
                                         @Override
                                         public void run() {
                                             Intent homepage = new Intent(LogIn.this, Homepage.class);
                                             homepage.putExtra("USER_ID", uId);
                                             homepage.putExtra("BARANGAY_ID", barangayId);
                                             homepage.putExtra("BARANGAY_NAME", barangayName);
                                             startActivity(homepage);
                                             finish();
                                         }
                                     }, TIME_OUT);
                                 }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
//
                                }
                            });
                        }
                    }
                    //
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//
                    }
                });
            }
        }
        else {
            toastMessage("No Internet Connection");
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void progressDialog(String barangayName){
        ProgressDialog pd = new ProgressDialog(LogIn.this);
        pd.setMessage("Logging In to " + barangayName + "...");
        pd.setCancelable(false);
        pd.show();
    }

    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
