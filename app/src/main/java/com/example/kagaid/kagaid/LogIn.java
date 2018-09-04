package com.example.kagaid.kagaid;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.kagaid.kagaid.Homepage;

import com.example.kagaid.kagaid.User;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class LogIn extends AppCompatActivity {
    //Firebase
    DatabaseReference databaseLogin;
    User u = new User();

    private static final String TAG = "Login";

    EditText username;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        username = (EditText) findViewById(R.id.loginUser);
        password = (EditText) findViewById(R.id.loginPwd);

        //firebase
        databaseLogin = FirebaseDatabase.getInstance().getReference("users");



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


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void goToHomepage() {
        //toastMessage("You are here!");
//        DatabaseReference usernameRef = ref.child(username.getText().toString());
//        final DatabaseReference passwordDetailsRef = usernameRef.child(password.getText().toString());

        if(TextUtils.isEmpty(username.getText().toString()) == true || TextUtils.isEmpty(password.getText().toString()) == true) {
            Toast.makeText(LogIn.this, "You did not enter a username/password", Toast.LENGTH_LONG).show();
        }else{
            databaseLogin.addValueEventListener(new ValueEventListener() {
                boolean userName = false;
                boolean pass = false;
                String uId = null;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot loginSnapshot: dataSnapshot.getChildren()){
                        if(username.getText().toString().equals(loginSnapshot.child("username").getValue().toString())){
                            //toastMessage("Got it Username!");
                            userName = true;
                            if(password.getText().toString().equals(loginSnapshot.child("password").getValue().toString())){
                                //toastMessage("Got it Password!");
                                pass = true;
                                if(userName == true && pass == true){
                                    uId = loginSnapshot.child("uId").getValue().toString();
                                    toastMessage(uId);
                                }
                            }
                        }
                    }

                    if(userName == false && pass == false){
                        toastMessage("Unregistered Account or Incorrect username/password");
                    }else if(userName == false && pass == true){
                        toastMessage("You did not enter a correct username");
                    }else if(userName == true && pass == false){
                        toastMessage("You did not enter a correct password");
                    }else if(userName == true && pass == true){
                        toastMessage("Successful Login");
                        Intent homepage = new Intent(LogIn.this, Homepage.class);
                        homepage.putExtra("USER_ID", uId);
                        finish();
                        startActivity(homepage);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
