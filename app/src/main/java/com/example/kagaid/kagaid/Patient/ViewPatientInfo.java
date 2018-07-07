package com.example.kagaid.kagaid.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kagaid.kagaid.R;

public class ViewPatientInfo extends AppCompatActivity {

    TextView textViewPatientName;
    TextView textViewPatientBday;
    TextView textviewPatientGender;
    TextView textViewPatientAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_info);

        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewPatientBday = (TextView) findViewById(R.id.textViewPatientBday);
        textviewPatientGender = (TextView) findViewById(R.id.textViewPatientGender);
        textViewPatientAddress = (TextView) findViewById(R.id.textViewPatientAddress);

        Intent intent = getIntent();

        String pfullname = intent.getStringExtra(PatientRecords.PATIENT_FULLNAME);
        String pbday = intent.getStringExtra(PatientRecords.PATIENT_BIRTHDAY);
        String pgender = intent.getStringExtra(PatientRecords.PATIENT_GENDER);
        String paddress = intent.getStringExtra(PatientRecords.PATIENT_ADDRESS);

        textViewPatientName.setText(pfullname);
        textViewPatientBday.setText(pbday);
        textviewPatientGender.setText(pgender);
        textViewPatientAddress.setText(paddress);
    }
}
