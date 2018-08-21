package com.example.kagaid.kagaid.Patient;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import maes.tech.intentanim.CustomIntent;

public class AddPatientRecord extends AppCompatActivity {

    EditText fullname, birthdate, address;
    Spinner gender;
    Button patientAdd;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_record);

        db = FirebaseDatabase.getInstance().getReference("person_information");

        fullname = (EditText) findViewById(R.id.fullname);
        birthdate = (EditText) findViewById(R.id.birthday);
        address = (EditText) findViewById(R.id.address);
        gender = (Spinner) findViewById(R.id.gender);
        patientAdd = (Button) findViewById(R.id.addPatientBtn);

        patientAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addPatient();
            }
        });
    }

    private void addPatient(){
        String fullnameP = fullname.getText().toString().trim();
        String bdayP = birthdate.getText().toString();
        String addressP = address.getText().toString();
        String genderP = gender.getSelectedItem().toString();

        if(!TextUtils.isEmpty(fullnameP) && !TextUtils.isEmpty(bdayP) && !TextUtils.isEmpty(addressP)){
            String pid = db.push().getKey();

            Patient patient = new Patient(pid, fullnameP, bdayP, genderP, addressP);

            db.child(pid).setValue(patient);

            fullname.setText("");
            birthdate.setText("");
            address.setText("");

            Toast.makeText(this, "Patient Record Added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please don't leave any field empty.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        openPatientRecords();

    }

    public void openPatientRecords(){
        Intent intent = new Intent(this, PatientRecords.class);
        //intent.putExtra("USERNAME", userName);
        finish();
        startActivity(intent);
        CustomIntent.customType(AddPatientRecord.this, "up-to-bottom");
    }
}
