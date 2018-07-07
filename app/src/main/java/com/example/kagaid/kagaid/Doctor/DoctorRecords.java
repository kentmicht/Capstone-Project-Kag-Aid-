package com.example.kagaid.kagaid.Doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorRecords extends AppCompatActivity {

    DatabaseReference db;

    ListView doctor_record;
    List<Doctor> drList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_records);

        db = FirebaseDatabase.getInstance().getReference("doctor");

        doctor_record = (ListView) findViewById(R.id.listViewDoctors);
        drList = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                drList.clear();

                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    Doctor doctor = dSnapshot.getValue(Doctor.class);

                    drList.add(doctor);

                }
                DoctorLists adapter = new DoctorLists(DoctorRecords.this, drList);
                doctor_record.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
