package com.example.kagaid.kagaid.Patient;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class AddPatientRecord extends AppCompatActivity {

    EditText fullname, address;
    TextView birthdate;
    Spinner gender;
    Button patientAdd;
    String uId;

    DatabaseReference db;
    private android.app.DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "AddPatientRecord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_record);
        uId = (String) getIntent().getStringExtra("USER_ID");
        db = FirebaseDatabase.getInstance().getReference("person_information");

        Toast.makeText(this,"User Id:" + uId, Toast.LENGTH_SHORT).show();

        fullname = (EditText) findViewById(R.id.fullname);
        birthdate = (TextView) findViewById(R.id.birthday);
        address = (EditText) findViewById(R.id.address);
        gender = (Spinner) findViewById(R.id.gender);
        patientAdd = (Button) findViewById(R.id.addPatientBtn);

        patientAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addPatient();
            }
        });
        birthdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddPatientRecord.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDataSet: yyyy-mm-dd: " + year + "-" + month + "-" + dayOfMonth);
                if(month >= 1 && month <=9){
                    String date = year + "-0" + month + "-" + dayOfMonth;
                    birthdate.setText(date);
                }else{
                    String date = year + "-" + month + "-" + dayOfMonth;
                    birthdate.setText(date);
                }


            }
        };
    }

    private void addPatient(){
        String fullnameP = fullname.getText().toString().trim();
        String bdayP = birthdate.getText().toString();
        String addressP = address.getText().toString();
        String genderP = gender.getSelectedItem().toString();
        String status = "1";

        if(!TextUtils.isEmpty(fullnameP) && !TextUtils.isEmpty(bdayP) && !TextUtils.isEmpty(addressP)){
            String pid = db.push().getKey();
            String age = calculateAge(bdayP);
            //Patient patient = new Patient(pid, fullnameP, bdayP, genderP, addressP);
            Patient patient = new Patient(pid, fullnameP, bdayP, genderP, addressP, "Not yet scanned", status, age);

            db.child(pid).setValue(patient);

            fullname.setText("");
            birthdate.setText("");
            address.setText("");

            Toast.makeText(this, "Patient Record Added", Toast.LENGTH_LONG).show();
            openPatientRecords();
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
        intent.putExtra("USER_ID", uId);
        finish();
        startActivity(intent);
        CustomIntent.customType(AddPatientRecord.this, "up-to-bottom");
    }

    public String currentDate(){
        java.util.Date c = java.util.Calendar.getInstance().getTime();

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public String calculateAge(String date){
        String age = null;
        String year = date.substring(0, 4);

        age = Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(year));
        return age;
    }

    public void back(View view){
        finish();
    }
}
