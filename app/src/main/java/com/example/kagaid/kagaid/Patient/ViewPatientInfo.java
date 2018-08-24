package com.example.kagaid.kagaid.Patient;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Logs.Log;
import com.example.kagaid.kagaid.Logs.Log;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class ViewPatientInfo extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 200;
    private ImageView selectedImageView;

    TextView textViewPatientName;
    TextView textViewPatientBday;
    TextView textviewPatientGender;
    TextView textViewPatientAddress;
    String uId;
    String pId;

    DatabaseReference databaseLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_info);
        uId = (String) getIntent().getStringExtra("USER_ID");

        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewPatientBday = (TextView) findViewById(R.id.textViewPatientBday);
        textviewPatientGender = (TextView) findViewById(R.id.textViewPatientGender);
        textViewPatientAddress = (TextView) findViewById(R.id.textViewPatientAddress);

        Intent intent = getIntent();

        String pfullname = intent.getStringExtra(PatientRecords.PATIENT_FULLNAME);
        String pbday = intent.getStringExtra(PatientRecords.PATIENT_BIRTHDAY);
        String pgender = intent.getStringExtra(PatientRecords.PATIENT_GENDER);
        String paddress = intent.getStringExtra(PatientRecords.PATIENT_ADDRESS);
        pId = intent.getStringExtra(PatientRecords.PATIENT_ID);
        //String lastscan = intent.getStringExtra(PatientRecords.PATIENT_LAST_SCAN);

        textViewPatientName.setText(pfullname);
        textViewPatientBday.setText(pbday);
        textviewPatientGender.setText(pgender);
        textViewPatientAddress.setText(paddress);

        toastMessage("User Id:" + uId + ", Patient Id: " + pId );
        //", Last Scan: " + lastscan
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        openPatientRecords();

    }

    public void openPatientRecords(){
        Intent intent = new Intent(this, PatientRecords.class);
        intent.putExtra("USER_ID", uId);
        finish();
        startActivity(intent);
        CustomIntent.customType(ViewPatientInfo.this, "right-to-left");
    }

    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("USER_ID", uId);

        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");

        //unique id
        String logId = databaseLogs.push().getKey();
        Log logSingle = new Log(logId, currentDateTime(), pId, uId);

        databaseLogs.child(logId).setValue(logSingle);
        Toast.makeText(this, "Logged", Toast.LENGTH_LONG).show();
        //do the adding to logs db;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

    public String currentDateTime(){
        String datetime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        return datetime;
    }
}
