package com.example.kagaid.kagaid;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kagaid.kagaid.Database.DatabaseHelperLogin;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.kagaid.kagaid.User;

public class LogIn extends AppCompatActivity {
    //Firebase
    FirebaseDatabase database;
    DatabaseReference ref;

    private static final String TAG = "Login";

    EditText username;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //firebase
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("users");

    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void goToHomepage(View view) {
        username = (EditText) findViewById(R.id.loginUser);
        password = (EditText) findViewById(R.id.loginPwd);

        if(TextUtils.isEmpty(username.getText().toString()) == true || TextUtils.isEmpty(password.getText().toString()) == true) {
            Toast.makeText(LogIn.this, "You did not enter a username/password", Toast.LENGTH_LONG).show();
        }else{
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(username.getText().toString()).exists()){
//                        toastMessage("Nakita ko besh!");
                            toastMessage("Successful Login");
                            Intent homepage = new Intent(LogIn.this, Homepage.class);
                            homepage.putExtra("USERNAME", username.getText().toString());
                            startActivity(homepage);
                            finish();
                    }else{
                        toastMessage("Unregistered Account");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }




}
