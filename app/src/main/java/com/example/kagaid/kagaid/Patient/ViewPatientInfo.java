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
import com.example.kagaid.kagaid.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    String employeeName = null;
    String pfullname = null;
    String pbday = null;
    String pgender = null;
    String paddress = null;

    DatabaseReference databaseLogs;
    DatabaseReference databasePatient;
    DatabaseReference databaseEmployee;

    User u = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_info);
        //uId = (String) getIntent().getStringExtra("USER_ID");

        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewPatientBday = (TextView) findViewById(R.id.textViewPatientBday);
        textviewPatientGender = (TextView) findViewById(R.id.textViewPatientGender);
        textViewPatientAddress = (TextView) findViewById(R.id.textViewPatientAddress);

        Intent intent = getIntent();

        pfullname = intent.getStringExtra(PatientRecords.PATIENT_FULLNAME);
        pbday = intent.getStringExtra(PatientRecords.PATIENT_BIRTHDAY);
        pgender = intent.getStringExtra(PatientRecords.PATIENT_GENDER);
        paddress = intent.getStringExtra(PatientRecords.PATIENT_ADDRESS);
        pId = intent.getStringExtra(PatientRecords.PATIENT_ID);
        uId = intent.getStringExtra(PatientRecords.USER_ID);
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
        databasePatient = FirebaseDatabase.getInstance().getReference("person_information");

        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");
        databasePatient = FirebaseDatabase.getInstance().getReference("person_information");
        databaseEmployee = FirebaseDatabase.getInstance().getReference("users");

        databaseEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //toastMessage(ds.child());
//                    u = ds.getValue(User.class);
                    if (uId.equals(ds.child("uId").getValue().toString())) {
                        //if (.equals(u.getUId()))
                        employeeName = ds.child("firstname").getValue().toString() + " " + ds.child("lastname").getValue().toString();
//
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(this, "E: " + employeeName, Toast.LENGTH_LONG).show();


        if(employeeName==null){
            Toast.makeText(this, "Pres Scan Again", Toast.LENGTH_LONG).show();
        }else{
            //add Logs
            String logId = databaseLogs.push().getKey();
            Log logSingle = new Log(logId, currentDateTime(), pId, uId, pfullname, employeeName);
            String status = "1";
            String age = calculateAge(pbday);
            Patient patient = new Patient(pId, pfullname, pbday, pgender, paddress, currentDateTime(), status, age);

            databasePatient.child(pId).setValue(patient);
            databaseLogs.child(logId).setValue(logSingle);
            Toast.makeText(this, "Logged", Toast.LENGTH_LONG).show();

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public String currentDateTime(){
        String datetime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        return datetime;
    }

    public String calculateAge(String date){
        String age = null;
        String year = date.substring(0, 4);

        age = Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(year));
        return age;
    }
}
