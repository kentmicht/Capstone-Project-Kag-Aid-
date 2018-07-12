package com.example.kagaid.kagaid.Patient;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Homepage;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;

public class PatientRecords extends AppCompatActivity {

    public static final String PATIENT_FULLNAME = "patientfullname";
    public static final String PATIENT_BIRTHDAY = "patientbday";
    public static final String PATIENT_GENDER = "patientgender";
    public static final String PATIENT_ADDRESS = "patientaddress";
    String userName;
    DatabaseReference db;

    ListView patient_record;
    List<Patient> pList;
//    EditText search_patient;
//    ImageView search_patient_btn;
    ArrayList<String> patient_names;
    PatientLists pLadapter;

    TextView test;
//    ArrayAdapter<Patient> adapter;
    private ArrayAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Patient Records
        setContentView(R.layout.activity_patient_records);
        toastMessage("Long press to update a patient's information.");
        userName = (String) getIntent().getStringExtra("USERNAME");

        patient_record = (ListView) findViewById(R.id.listViewPatient);
        pList = new ArrayList<>();
//        search_patient = (EditText) findViewById(R.id.editTextSearchPatient);
//        search_patient_btn = (ImageView) findViewById(R.id.searchPatientButton);
//        test = (TextView) findViewById(R.id.test);
        patient_names = new ArrayList<String>();

        //Firebase Database
        db = FirebaseDatabase.getInstance().getReference("person_information");

        //Show individual patient record
        patient_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient patient = pList.get(position);

                Intent intent = new Intent(getApplicationContext(), ViewPatientInfo.class);

                intent.putExtra(PATIENT_FULLNAME, patient.getFullname());
                intent.putExtra(PATIENT_BIRTHDAY, patient.getBirthday());
                intent.putExtra(PATIENT_GENDER, patient.getGender());
                intent.putExtra(PATIENT_ADDRESS, patient.getAddress());
                intent.putExtra("USERNAME", userName);

                startActivity(intent);
            }
        });

        //Show dialog for updating
        patient_record.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Patient patient = pList.get(position);

                showUpdateDialog(patient.getId(), patient.getFullname(), patient.getBirthday(), patient.getGender(), patient.getAddress());

                return false;
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    //Viewing all the Patient records inside a listview
    @Override
    protected void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pList.clear();

                for (DataSnapshot pSnapshot : dataSnapshot.getChildren()){
                    Patient patient = pSnapshot.getValue(Patient.class);
                    pList.add(patient);
//                    patient_names.add(patient.getFullname());
                }

                PatientLists adapter = new PatientLists(PatientRecords.this, pList);
                patient_record.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Show the update Dialog
    private void showUpdateDialog(final String pid, final String fullname, final String bday, final String gender, final String address)
    {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_patient_info_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextBday = (EditText) dialogView.findViewById(R.id.editTextBirthday);
        final TextView textViewGender = (TextView) dialogView.findViewById(R.id.patient_gender); //for displaying the current gender
        final Spinner spinnerGender = (Spinner) dialogView.findViewById(R.id.spinnerGender); //for the updating of teh gender
        final EditText editTextAddress = (EditText) dialogView.findViewById(R.id.editTextAddress);
        final Button updatePatientBtn = (Button) dialogView.findViewById(R.id.updatePatientBtn);

        dialogBuilder.setTitle("Updating Patient "+ fullname);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        //Setting the values from the db to the fields
        editTextName.setText(fullname);
        editTextBday.setText(bday);
        textViewGender.setText(gender);
        editTextAddress.setText(address);

        updatePatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = editTextName.getText().toString().trim();
                String pbday = editTextBday.getText().toString();
                String pgender = spinnerGender.getSelectedItem().toString();
                String paddress = editTextAddress.getText().toString();

                if(TextUtils.isEmpty(pname)){
                    editTextName.setError("Name resquired");
                    return;
                }else if(TextUtils.isEmpty(pbday)){
                    editTextBday.setError("Birthday is required");
                    return;
                }else if(TextUtils.isEmpty(paddress)){
                    editTextAddress.setError("Address is required");
                    return;
                }

                updatePatient(pid, pname, pbday, pgender, paddress);
                alertDialog.dismiss();
            }
        });
    }

    //Actual updating in the database
    private boolean updatePatient(String pid, String fullname, String bday, String gender, String address){

        println(pid);

        db = FirebaseDatabase.getInstance().getReference("person_information").child(pid);
        Patient patient = new Patient(pid, fullname, bday, gender, address);

        db.setValue(patient);
        Toast.makeText(this, "Patient Record Updated", Toast.LENGTH_LONG).show();

        return true;
    }

    //Showing new window for adding
    public void addPatientRecord(View view){
        Intent addPatientRec = new Intent(this, AddPatientRecord.class);
        //addPatientRec.putExtra("USERNAME", userName);
        startActivity(addPatientRec);
    }

    @Override
    public void onBackPressed() {
        openHomepage();

    }

    public void openHomepage(){
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("USERNAME", userName);
        finish();
        startActivity(intent);
    }
}
