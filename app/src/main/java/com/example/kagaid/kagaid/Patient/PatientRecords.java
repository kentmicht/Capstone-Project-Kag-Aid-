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
import android.view.KeyEvent;
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
    DatabaseReference dbLogs;

    ListView patient_record;
    List<Patient> pList;
//    EditText search_patient;
//    ImageView search_patient_btn;
    ArrayList<String> patient_names;
    PatientLists pLadapter;

    TextView test;
    EditText fullNameSearch;
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
                CustomIntent.customType(PatientRecords.this, "fadein-to-fadeout");
            }
        });

        //Show dialog for updating
        patient_record.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Patient patient = pList.get(position);

                showUpdateDialog(patient.getId(), patient.getFullname(), patient.getBirthday(), patient.getGender(), patient.getAddress(), patient.getLastscan(), patient.getbId());

                return false;
            }
        });

        fullNameSearch = (EditText) findViewById(R.id.patientSearch);
        fullNameSearch.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            searchPatient();
                            return true;
                        default:
                            break;
                    }
                }
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
    private void showUpdateDialog(final String pid, final String fullname, final String bday, final String gender, final String address, final String lastscan, final String bid)
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
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
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
                String status = "1";
                String pAge = calculateAge(pbday);

                //error handlings
                boolean duplicatePatient = false;
                boolean yearBeyondCurrent = false;
                boolean fullNameAllNumbers = false;
                boolean fullNameAllSpecial = false;
                boolean fullNameContainsNumber = false;
                boolean fullNameContainsSpecial = false;
                boolean addressAllNumbers = false;
                boolean addressAllSpecial = false;
                boolean addressContainsSpecial = false;

                if(TextUtils.isEmpty(pname)){
                    editTextName.setError("Fullname is required");
                    return;
                }else if(TextUtils.isEmpty(paddress)){
                    editTextAddress.setError("Address is required");
                    return;
                }

                if(checkBirthdateYear(pbday) == false) {
                    yearBeyondCurrent = true;
                    toastMessage("Not Updated: Patient’s Birthdate year exceeds the current year");
                }

                if(checkFullNameAllNumbers(pname)) {
                    fullNameAllNumbers = true;
                    toastMessage("Not Updated: Patient’s name is all digits");
                }else if(checkFullNameAllSpecial(pname)) {
                    fullNameAllSpecial  = true;
                    toastMessage("Not Updated: Patient’s Name is all special characters");
                }else if(checkFullNameContainsNumber(pname)) {
                    addressAllSpecial  = true;
                    toastMessage("Not Updated: Patient’s name contains a digit");
                }else if(checkFullNameContainsSpecial(pname)){
                    fullNameContainsSpecial = true;
                    toastMessage("Not Updated: Patient’s Name contains invalid special characters");
                }

                if(checkAddressAllNumbers(paddress)) {
                    addressAllNumbers = true;
                    toastMessage("Not Updated: Patient’s Address is all digits");
                }else if(checkAddressSpecial(paddress)) {
                    fullNameAllSpecial  = true;
                    toastMessage("Not Updated: Patient's address is all special characters");
                }else if(checkAddressContainsNumber(paddress)){
                    addressContainsSpecial  = true;
                    toastMessage("Not Updated: Patient's address contains invalid special characters");
                }

                if(yearBeyondCurrent == false &&
                        fullNameAllSpecial == false &&
                        fullNameAllNumbers == false &&
                        addressAllNumbers == false &&
                        addressAllSpecial == false &&
                        fullNameContainsNumber == false &&
                        addressContainsSpecial == false &&
                        fullNameContainsSpecial == false){
                    updatePatient(pid, pname, pbday, pAge, pgender, paddress, lastscan, status, bid);
                }
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

    public boolean checkBirthdateYear(String bday){
        boolean ret = false;
        String age = null;
        String year = bday.substring(0, 4);

        if(Calendar.getInstance().get(Calendar.YEAR) >= Integer.parseInt(year)){
            ret = true;
        }


        return ret;
    }

    public boolean checkFullNameAllSpecial(String fullname){
        boolean ret = false;
        if(fullname.matches("[+×÷=/_€£¥₩!@#$%^&*()'\":;?`~<>¡¿]+")){
            ret = true;
        }
        return ret;
    }

    public boolean checkFullNameAllNumbers(String fullname){
        boolean ret = false;
        if(fullname.matches("[0-9]+")){
            ret = true;
        }
        return ret;
    }

    public boolean checkFullNameContainsNumber(String fullname){
        boolean ret = false;
        if(fullname.matches(".*\\d+.*")){
            ret = true;
        }
        return ret;
    }

    public boolean checkFullNameContainsSpecial(String fullname){
        boolean ret = false;
        if(fullname.matches(".*[+×÷=/_€£¥₩!@#$%^&*()'\":;?`~<>¡¿]+.*")){
            ret = true;
        }
        return ret;
    }

    public boolean checkAddressSpecial(String address){
        boolean ret = false;
        if(address.matches("[+×÷=/_€£¥₩!@#$%^&*()'\":;?`~<>¡¿]+")){
            ret = true;
        }
        return ret;
    }

    public boolean checkAddressAllNumbers(String address){
        boolean ret = false;
        if(address.matches("[0-9]+")){
            ret = true;
        }
        return ret;
    }

    public String formatFullname(String fullname){
        String[] strArray = fullname.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap);
        }

        return builder.toString();
    }

    public boolean checkAddressContainsNumber(String fullname){
        boolean ret = false;
        if(fullname.matches(".*[+×÷=/_€£¥₩!@#$%^&*()'\":;?`~<>¡¿]+.*")){
            ret = true;
        }
        return ret;
    }

    //Actual updating in the database
    private boolean updatePatient(final String pid, final String fullname, final String bday, final String age, final String gender, final String address, final String lastscan, final String status, final String bid){
        final boolean[] ret = {false};
//        println(pid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  boolean duplicatePatient = false;
                  for (DataSnapshot dsPatient : dataSnapshot.getChildren()) {
                      Patient patient = dsPatient.getValue(Patient.class);
                      if(fullname.equals(patient.getFullname())){
                          if(!pid.equals(patient.getId())){
                              duplicatePatient = true;
                          }

                      }
                  }

                  if(duplicatePatient == false){
                      db = FirebaseDatabase.getInstance().getReference("person_information").child(pid);
                      dbLogs = FirebaseDatabase.getInstance().getReference("logs");
                      Patient patient = new Patient(pid, fullname, bday, age, gender, address, lastscan, status, bid);

                      db.setValue(patient);
                      toastMessage("Patient Record Updated");
                      ret[0] = true;
                  }else{
                      toastMessage("Not Updated: Patient has been added already");
                  }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
        });
        return ret[0];
    }

    //Showing new window for adding
    public void addPatientRecord(View view){
        Intent addPatientRec = new Intent(this, AddPatientRecord.class);
        //addPatientRec.putExtra("USERNAME", userName);
        addPatientRec.putExtra("USER_ID", uId);
        addPatientRec.putExtra("BARANGAY_ID", bId);
        startActivity(addPatientRec);
        CustomIntent.customType(PatientRecords.this, "fadein-to-fadeout");
    }

    @Override
    public void onBackPressed() {
        finish();
        CustomIntent.customType(PatientRecords.this, "fadein-to-fadeout");
    }

    public void openHomepage(){
        Intent intent = new Intent(this, Homepage.class);
        intent.putExtra("USER_ID", uId);
        startActivity(intent);
        finish();
        CustomIntent.customType(PatientRecords.this, "fadein-to-fadeout");
    }

    public void searchPatient(){
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
                        if(patient.getFullname().toLowerCase().contains(fullnameS.toLowerCase()) && bId.equals(patient.getbId()) && patient.getStatus().equals("1")){
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
//                    toastMessage("Firebase:" + pSnapshot.child("bId").getValue().toString());
//                    toastMessage("Patient Class:" + patient.getbId());
//                    toastMessage("Passed: " + bId);
                    if(!patient.getFullname().equals(null)){
//                        if(patient.getStatus().equals("1")){
                        if(patient.getStatus().equals("1") && bId.equals(patient.getbId())){
//                            toastMessage(patient.getFullname() + "is in this Barangay");
                            pList.add(patient);
                        }
                    }


//                    patient_names.add(patient.getFullname());
                }
                //sort the list
//                toastMessage(bId);
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
        CustomIntent.customType(PatientRecords.this, "fadein-to-fadeout");
    }
}
