package com.example.kagaid.kagaid;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Logs.Logs;
import com.example.kagaid.kagaid.Maps.MapsActivity;
import com.example.kagaid.kagaid.Patient.PatientRecords;

import maes.tech.intentanim.CustomIntent;

public class Homepage extends AppCompatActivity {
    String uId;
    String bId;
    String barangayName;

    TextView bName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        }else {
            toastMessage("No Internet Connection");
        }

        uId = (String) getIntent().getStringExtra("USER_ID");
        bId = (String) getIntent().getStringExtra("BARANGAY_ID");
        barangayName = (String) getIntent().getStringExtra("BARANGAY_NAME");

        bName = (TextView) findViewById(R.id.header);
        bName.setText("Welcome to " + barangayName);

//        Toast.makeText(this,"User Id:" + uId, Toast.LENGTH_SHORT).show();
//        toastMessage("User Id:" + uId);
//        toastMessage("Barangay Id:" + bId);
//        TextView name = (TextView)findViewById(R.id.header);
//        name.setText("Welcome "+userName+"!");
    }



    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void logOut(View view) {
        alertLogout();
    }

    public void alertLogout(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(Homepage.this);
        a_builder.setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openLogin();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Kag-Aid");
        alert.show();
    }

    public void openLogin(){
        Intent login = new Intent(this, LogIn.class);
        startActivity(login);
        finish();
        CustomIntent.customType(Homepage.this, "fadein-to-fadeout");
    }

    public void map(View view){
        Intent mapNav = new Intent(this, MapsActivity.class);
        startActivity(mapNav);
    }

    public void openPatientRecord(View view){
        Intent patientRec = new Intent(this, PatientRecords.class);
        patientRec.putExtra("USER_ID", uId);
        patientRec.putExtra("BARANGAY_ID", bId);
        patientRec.putExtra("BARANGAY_NAME", barangayName);
        startActivity(patientRec);
        CustomIntent.customType(Homepage.this, "fadein-to-fadeout");
    }

    @Override
    public void onBackPressed() {alertLogout();}

    public void goPrivacyPolicy (View view) {
        Uri uri = Uri.parse("https://www.freeprivacypolicy.com/privacy/view/71a0efb9e219bb3cdfc6e2a10832d112"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        CustomIntent.customType(Homepage.this, "fadein-to-fadeout");
    }

    public void openLogs(View view) {
        Intent logs = new Intent(this, Logs.class);
        logs.putExtra("USER_ID", uId);
        logs.putExtra("BARANGAY_ID", bId);
        startActivity(logs);
        CustomIntent.customType(Homepage.this, "fadein-to-fadeout");
    }



}
