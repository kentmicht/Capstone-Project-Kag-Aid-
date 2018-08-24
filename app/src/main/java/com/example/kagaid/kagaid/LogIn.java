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

import com.example.kagaid.kagaid.SkinIllness.SkinIllness;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.kagaid.kagaid.User;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class LogIn extends AppCompatActivity {
    //Firebase
    DatabaseReference ref;
    ArrayList<User> userList = new ArrayList<User>();
    User u = new User();

    private static final String TAG = "Login";

    EditText username;
    EditText password;
    String uId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        username = (EditText) findViewById(R.id.loginUser);
        password = (EditText) findViewById(R.id.loginPwd);

        //firebase
        ref = FirebaseDatabase.getInstance().getReference().child("users");

    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void goToHomepage(View view) {
//        DatabaseReference usernameRef = ref.child(username.getText().toString());
//        final DatabaseReference passwordDetailsRef = usernameRef.child(password.getText().toString());

        if(TextUtils.isEmpty(username.getText().toString()) == true || TextUtils.isEmpty(password.getText().toString()) == true) {
            Toast.makeText(LogIn.this, "You did not enter a username/password", Toast.LENGTH_LONG).show();
        }else{
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean userMatch = false;
                    boolean pwdMatch = false;
                    String uFirstName = null;
                    String uId = null;
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        u = ds.getValue(User.class);
                        if(username.getText().toString().equals(u.getUsername())){
                            userMatch = true;
                            if(password.getText().toString().equals(u.getPassword())){
                                pwdMatch = true;

                                if(userMatch == true && pwdMatch == true){
                                    uFirstName = u.getFirstname();
                                    uId = u.getUId();
                                }
                            }
                        }
                    }

                    if(userMatch == false){
                        toastMessage("You did not enter a correct username");
                    }

                    if(pwdMatch == false){
                        toastMessage("You did not enter a correct password");
                    }

                    if(userMatch == false && pwdMatch == false){
                        toastMessage("Unregistered Account or Incorrect username/password");
                    }

                    if(userMatch == true && pwdMatch == true){
                        toastMessage("Successful Login");
                        Intent homepage = new Intent(LogIn.this, Homepage.class);
                        homepage.putExtra("USER_ID", uId);
                        finish();
                        startActivity(homepage);
                        CustomIntent.customType(LogIn.this, "fadein-to-fadeout");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
