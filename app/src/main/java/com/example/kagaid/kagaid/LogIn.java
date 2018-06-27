package com.example.kagaid.kagaid;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {

    //public Button loginBtn = (Button)findViewById(R.id.loginBtn);
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //initLoginBtn();
    }

    public void goToHomepage(View view) {
        username = (EditText) findViewById(R.id.loginUser);
        password = (EditText) findViewById(R.id.loginPwd);
        if(TextUtils.isEmpty(username.getText().toString()) == true || TextUtils.isEmpty(password.getText().toString()) == true){
            Toast.makeText(LogIn.this, "You did not enter a username/password", Toast.LENGTH_LONG).show();
        }else{
            Intent homepage = new Intent(this, Homepage.class);
            startActivity(homepage);
        }

    }


//    public void initLoginBtn(){
//        loginBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
////                if(username.getText().toString().isEmpty() == true && password.getText().toString().isEmpty() == true){
////                    Toast.makeText(LogIn.this, "You did not enter a username/password", Toast.LENGTH_LONG).show();
//////                    //emptyText();
////                }else{
//                    startActivity(new Intent(LogIn.this, Homepage.class));
////                }
////
//            }
//        });



}
