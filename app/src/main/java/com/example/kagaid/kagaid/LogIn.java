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

public class LogIn extends AppCompatActivity {
    private static final String TAG = "Login";
    DatabaseHelperLogin mDatabaseHelper;
    //public Button loginBtn = (Button)findViewById(R.id.loginBtn);
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mDatabaseHelper = new DatabaseHelperLogin(this);

    }

    public void AddData(String username, String password) {
        boolean insertData = mDatabaseHelper.addData(username, password);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void goToHomepage(View view) {
        username = (EditText) findViewById(R.id.loginUser);
        password = (EditText) findViewById(R.id.loginPwd);
        //AddData("Kent", "004323");
        if(TextUtils.isEmpty(username.getText().toString()) == true || TextUtils.isEmpty(password.getText().toString()) == true){
            Toast.makeText(LogIn.this, "You did not enter a username/password", Toast.LENGTH_LONG).show();
        //}else {
        } else
            //if (mDatabaseHelper.checkUser(username.getText().toString(), password.getText().toString()))
            {
//                toastMessage("Successfully Logged In");
                Intent homepage = new Intent(this, Homepage.class);
                homepage.putExtra("USERNAME", username.getText().toString());
                startActivity(homepage);
        }
//        else {
//            toastMessage("Invalid Username/Password");
//        }

//        else if (mDatabaseHelper.checkUser(username.getText().toString(), password.getText().toString())) {
////                toastMessage("Successfully Logged In");
//            Intent homepage = new Intent(this, Homepage.class);
//            homepage.putExtra("USERNAME", username.getText().toString());
//            startActivity(homepage);
//            startActivity(homepage);
//        }else {
//            toastMessage("Invalid Username/Password");
//        }
//        mDatabaseHelper.close();

    }




}
