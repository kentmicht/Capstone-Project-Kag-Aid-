package com.example.kagaid.kagaid.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

import com.example.kagaid.kagaid.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PatientRecords extends AppCompatActivity {

    ListView patient_record;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_records);

        patient_record = (ListView) findViewById(R.id.patient_records);

        ArrayList<String> arrayPatientRecords = new ArrayList<>();
        arrayPatientRecords.addAll(Arrays.asList(getResources().getStringArray(R.array.p_records)));

        adapter = new ArrayAdapter<String>(
                PatientRecords.this,
                android.R.layout.simple_list_item_1,
                arrayPatientRecords
        );

        patient_record.setAdapter(adapter);
    }

    public void addPatientRecord(View view){
        Intent addPatientRec = new Intent(this, AddPatientRecord.class);
        startActivity(addPatientRec);
    }
}
