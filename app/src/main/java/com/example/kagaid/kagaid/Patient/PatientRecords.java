package com.example.kagaid.kagaid.Patient;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

import static java.sql.DriverManager.println;

public class PatientRecords extends AppCompatActivity {

    public static final String PATIENT_FULLNAME = "patientfullname";
    public static final String PATIENT_BIRTHDAY = "patientbday";
    public static final String PATIENT_GENDER = "patientgender";
    public static final String PATIENT_ADDRESS = "patientaddress";
    public static final String PATIENT_ID = "patientid";
    public static final String USER_ID = "uId";
    public static final String PATIENT_LAST_SCAN = "patientlastscan";

    private static android.app.DatePickerDialog.OnDateSetListener mDateSetListener;
    final String TAG = "PatientRecords";

    String uId;
    String bId;
    String bName;

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

        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        }else {
            toastMessage("No Internet Connection");
        }

//        toastMessage("Long press to update a patient's information.");
        uId = (String) getIntent().getStringExtra("USER_ID");
        bId = (String) getIntent().getStringExtra("BARANGAY_ID");
        bName = (String) getIntent().getStringExtra("BARANGAY_NAME");

        patient_record = (ListView) findViewById(R.id.listViewPatient);
        pList = new ArrayList<>();
//        search_patient = (EditText) findViewById(R.id.editTextSearchPatient);
//        search_patient_btn = (ImageView) findViewById(R.id.searchPatientButton);
//        test = (TextView) findViewById(R.id.test);
        patient_names = new ArrayList<String>();

//        toastMessage("User Id:" + uId);
//        toastMessage("Barangay Id: " + bId);

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
                intent.putExtra(PATIENT_ID, patient.getId());
                intent.putExtra(PATIENT_LAST_SCAN, patient.getLastscan());
                intent.putExtra(USER_ID, uId);
                intent.putExtra("BARANGAY_ID", bId);
                intent.putExtra("BARANGAY_NAME", bName);

                startActivity(intent);
                finish();
            }
        });

        //Show dialog for updating
        patient_record.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Patient patient = pList.get(position);

                showUpdateDialog(patient.getId(), patient.getFullname(), patient.getBirthday(), patient.getGender(), patient.getAddress(), patient.getLastscan());

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

        viewAllPatients();

        ImageView search = (ImageView) findViewById(R.id.patientSearchBtn);
        search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                searchPatient();
            }
        });
    }

    //Show the update Dialog
    private void showUpdateDialog(final String pid, final String fullname, final String bday, final String gender, final String address, final String lastscan)
    {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_patient_info_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final TextView editTextBday = (TextView) dialogView.findViewById(R.id.editTextBirthday);
        //final TextView textViewGender = (TextView) dialogView.findViewById(R.id.patient_gender); //for displaying the current gender
        final Spinner spinnerGender = (Spinner) dialogView.findViewById(R.id.spinnerGender); //for the updating of teh gender
        final EditText editTextAddress = (EditText) dialogView.findViewById(R.id.editTextAddress);
        final Button updatePatientBtn = (Button) dialogView.findViewById(R.id.updatePatientBtn);

        dialogBuilder.setTitle(fullname);

        //toastMessage("Position: " + Integer.toString(spinnerGender.getSelectedItemPosition()) + "Gender: " + gender);

        if(gender.equals("Female")) {
            spinnerGender.setSelection(1, true);
            //toastMessage("Laban");
        }




        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        //Setting the values from the db to the fields
        editTextName.setText(fullname);
        editTextBday.setText(bday);
        //textViewGender.setText(gender);
        editTextAddress.setText(address);

        editTextBday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] dateParts = editTextBday.getText().toString().split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1;
                int day = Integer.parseInt(dateParts[2]);

//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);

//                toastMessage("Year:" + dateParts[0] + " Month:" + dateParts[1]);

                DatePickerDialog dialog = new DatePickerDialog(PatientRecords.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        updatePatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = editTextName.getText().toString().trim();
                String pbday = editTextBday.getText().toString();
                String pgender = spinnerGender.getSelectedItem().toString();
                String paddress = editTextAddress.getText().toString();

                if(TextUtils.isEmpty(pname)){
                    editTextName.setError("Name is required");
                    return;
                }else if(TextUtils.isEmpty(pbday)){
                    editTextBday.setError("Birthday is required");
                    return;
                }else if(TextUtils.isEmpty(paddress)){
                    editTextAddress.setError("Address is required");
                    return;
                }

                updatePatient(pid, pname, pbday, pgender, paddress, lastscan);
                alertDialog.dismiss();
            }
        });

        mDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d(TAG, "onDataSet: yyyy-mm-dd: " + year + "-" + month + "-" + dayOfMonth);
                if(month >= 1 && month <=9){
                    String date = year + "-0" + month + "-" + dayOfMonth;
                    editTextBday.setText(date);
                }else{
                    String date = year + "-" + month + "-" + dayOfMonth;
                    editTextBday.setText(date);
                }

            }
        };
    }

    //Actual updating in the database
    private boolean updatePatient(String pid, String fullname, String bday, String gender, String address, String lastscan){

        println(pid);

        db = FirebaseDatabase.getInstance().getReference("person_information").child(pid);
        String status = "1";
        String age = calculateAge(bday);
        Patient patient = new Patient(pid, fullname, bday, gender, address, lastscan, status, age, bId);

        db.setValue(patient);
        Toast.makeText(this, "Patient Record Updated", Toast.LENGTH_LONG).show();

        return true;
    }

    //Showing new window for adding
    public void addPatientRecord(View view){
        Intent addPatientRec = new Intent(this, AddPatientRecord.class);
        //addPatientRec.putExtra("USERNAME", userName);
        addPatientRec.putExtra("USER_ID", uId);
        addPatientRec.putExtra("BARANGAY_ID", bId);
        startActivity(addPatientRec);
        CustomIntent.customType(PatientRecords.this, "bottom-to-up");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void openHomepage(){
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("USER_ID", uId);
        startActivity(intent);
        finish();
        CustomIntent.customType(PatientRecords.this, "fadein-to-fadeout");
    }

    public void searchPatient(){
        EditText fullNameSearch = (EditText) findViewById(R.id.patientSearch);
        final String fullnameS = fullNameSearch.getText().toString();

        if(fullnameS.matches("")){
            toastMessage("Nothing to search");
            viewAllPatients();
        }else{
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    pList.clear();

                    for (DataSnapshot pSnapshot : dataSnapshot.getChildren()){
                        Patient patient = pSnapshot.getValue(Patient.class);
                        if(patient.getFullname().toLowerCase().contains(fullnameS.toLowerCase()) && patient.getStatus().equals("1") && bId.equals(pSnapshot.child("bId").getValue().toString())){
                            pList.add(patient);
                        }
//                    patient_names.add(patient.getFullname());
                    }

                    if(pList.isEmpty()){
                        toastMessage("No Match");
                        viewAllPatients();
                    }else {
                        sortPList();
                        PatientLists adapter = new PatientLists(PatientRecords.this, pList);
                        patient_record.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void viewAllPatients(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pList.clear();

                for (DataSnapshot pSnapshot : dataSnapshot.getChildren()){
                    Patient patient = pSnapshot.getValue(Patient.class);
                    if(patient.getStatus().equals("1") && bId.equals(pSnapshot.child("bId").getValue().toString())){
                        pList.add(patient);
                    }

//                    patient_names.add(patient.getFullname());
                }
                //sort the list
                sortPList();
                PatientLists adapter = new PatientLists(PatientRecords.this, pList);
                patient_record.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sortPList(){
        if (pList.size() > 0) {
            Collections.sort(pList, new Comparator<Patient>() {
                @Override
                public int compare(final Patient object1, final Patient object2) {
                    return object1.getFullname().toLowerCase().compareTo(object2.getFullname().toLowerCase());
                }
            });
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

    public void back(View view){
        finish();
    }
}
