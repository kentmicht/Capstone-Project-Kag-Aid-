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
    String[] fullnamePatient = new String[3];

    DatabaseReference db;
    DatabaseReference dbLogs;

    ListView patient_record;
    List<Patient> pList;
    TextView patientErr;
    ArrayList<String> patient_names;
    PatientLists pLadapter;

    TextView test;
    TextView editTextName;
    EditText fullNameSearch;
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

        patientErr = (TextView) findViewById(R.id.noPatientRecord);
        patientErr.setVisibility(View.INVISIBLE);

//        toastMessage("Long press to update a patient's information.");
        uId = (String) getIntent().getStringExtra("USER_ID");
        bId = (String) getIntent().getStringExtra("BARANGAY_ID");
        bName = (String) getIntent().getStringExtra("BARANGAY_NAME");

        patient_record = (ListView) findViewById(R.id.listViewPatient);
        pList = new ArrayList<>();
        patient_names = new ArrayList<String>();

        //Firebase Database
        db = FirebaseDatabase.getInstance().getReference("person_information");

        //Show individual patient record through passing patient records to the IndivPatient page
        patient_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient patient = pList.get(position);

                Intent intent = new Intent(getApplicationContext(), ViewPatientInfo.class);

                intent.putExtra(PATIENT_FULLNAME, patient.getFullname());
                intent.putExtra("PATIENT_FIRSTNAME", patient.getFirstname());
                intent.putExtra("PATIENT_LASTNAME", patient.getLastname());
                intent.putExtra("PATIENT_MIDDLENAME", patient.getMiddlename());
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

                showUpdateDialog(patient.getId(), patient.getFirstname(), patient.getLastname(), patient.getMiddlename(), patient.getBirthday(), patient.getGender(), patient.getAddress(), patient.getLastscan(), patient.getbId());

                return false;
            }
        });

        fullNameSearch = (EditText) findViewById(R.id.patientSearch);
        onSearchEnter(fullNameSearch);
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
        onSearchEnter(fullNameSearch);
    }

    //using the enter key to search patients
    private void onSearchEnter(EditText fullNameSearch){
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

    //Show the update Dialog
    private void showUpdateDialog(final String pid, final String firstname, final String lastname, final String middlename, final String bday, final String gender, final String address, final String lastscan, final String bid)
    {

        final String oldName;
        if(middlename.equals(" ")){
            oldName = lastname + ", " + firstname + " " + middlename;
        }else{
            oldName = lastname + ", " + firstname + " " + middlename.charAt(0) + ".";
        }
        fullnamePatient[0] = firstname;
        fullnamePatient[1] = lastname;
        fullnamePatient[2] = middlename;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_patient_info_dialog, null);

        dialogBuilder.setView(dialogView);

        editTextName = (TextView) dialogView.findViewById(R.id.fullNameSeperate);
        final TextView editTextBday = (TextView) dialogView.findViewById(R.id.editTextBirthday);
        final Spinner spinnerGender = (Spinner) dialogView.findViewById(R.id.spinnerGender); //for the updating of teh gender
        final EditText editTextAddress = (EditText) dialogView.findViewById(R.id.editTextAddress);
        final Button updatePatientBtn = (Button) dialogView.findViewById(R.id.updatePatientBtn);

        if(middlename.equals(" ")){
            dialogBuilder.setTitle(lastname + ", " + firstname + " " + middlename);
        }else{
            dialogBuilder.setTitle(lastname + ", " + firstname + " " + middlename.charAt(0) + ".");
        }


        if(gender.equals("Female")) {
            spinnerGender.setSelection(1, true);
        }




        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        //Setting the values from the db to the fields
        if(middlename.equals(" ")){
            editTextName.setText(lastname + ", " + firstname + " " + middlename);
        }else{
            editTextName.setText(lastname + ", " + firstname + " " + middlename.charAt(0) + ".");
        }

        editTextBday.setText(bday);
        editTextAddress.setText(address);

        editTextBday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String[] dateParts = editTextBday.getText().toString().split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1;
                int day = Integer.parseInt(dateParts[2]);


                DatePickerDialog dialog = new DatePickerDialog(PatientRecords.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        editTextName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showFullNameSeperatorDialog(fullnamePatient[0], fullnamePatient[1], fullnamePatient[2]);
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
                boolean yearBeyondCurrentDate = false;
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

                if(checkBirthdateEqualCurrentDate(pbday) == true) {
                    yearBeyondCurrentDate = true;
                    toastMessage("Not Updated: Patient’s Birthdate must be below the current date");
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
                        yearBeyondCurrentDate == false &&
                        fullNameAllSpecial == false &&
                        fullNameAllNumbers == false &&
                        addressAllNumbers == false &&
                        addressAllSpecial == false &&
                        fullNameContainsNumber == false &&
                        addressContainsSpecial == false &&
                        fullNameContainsSpecial == false){

                    paddress = captializeFirstLetter(paddress);

                    updatePatient(pid, fullnamePatient[0], fullnamePatient[1], fullnamePatient[2], pbday, pAge, pgender, captializeFirstLetter(paddress), lastscan, status, bid, oldName);
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

    //show dialog box for seperating fullname (First Name, Last Name and Middle Name)
    public void showFullNameSeperatorDialog(String firstname, String lastname, final String middlename){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_fullname_seperation_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editFirstName = (EditText) dialogView.findViewById(R.id.firstNameSep);
        final EditText editLastName = (EditText) dialogView.findViewById(R.id.lastNameSep);
        final EditText editMiddleName = (EditText) dialogView.findViewById(R.id.midNameSep);
        final Button fullNameBtn = (Button) dialogView.findViewById(R.id.fullnameButton);

        editFirstName.setText(firstname);
        editLastName.setText(lastname);
        if(middlename.equals(" ")){
            editMiddleName.setText("");
        }else{
            editMiddleName.setText(middlename);
        }


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        fullNameBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean fullNameAllNumbers = false;
                boolean fullNameAllSpecial = false;
                boolean fullNameContainsNumber = false;
                boolean fullNameContainsSpecial = false;

                if(TextUtils.isEmpty(editFirstName.getText().toString())){
                    editFirstName.setError("First Name is required");
                    return;
                }else if(TextUtils.isEmpty(editLastName.getText().toString())){
                    editLastName.setError("Last Name is required");
                    return;
                }

                //fullname format
                final String fullNameFormat;
                if(TextUtils.isEmpty(editMiddleName.getText().toString())){
                    fullNameFormat = captializeFirstLetter(editLastName.getText().toString()) + ", " +
                            captializeFirstLetter(editFirstName.getText().toString()) + " ";
                }else{
                    fullNameFormat = captializeFirstLetter(editLastName.getText().toString()) + ", " +
                            captializeFirstLetter(editFirstName.getText().toString()) + " " +
                            captializeFirstLetter(editMiddleName.getText().toString());
                }


                //error handling confirmation
                if (checkFullNameAllNumbers(fullNameFormat)) {
                    fullNameAllNumbers = true;
                    toastMessage("Patient's name (First, Middle, or Last) is all digits");
                } else if (checkFullNameAllSpecial(fullNameFormat)) {
                    fullNameAllSpecial = true;
                    toastMessage("Patient's name (First, Middle, or Last) is all special characters");
                } else if (checkFullNameContainsNumber(fullNameFormat)) {
                    fullNameContainsNumber = true;
                    toastMessage("Patient’s name (First, Middle, or Last) contains a digit");
                } else if (checkFullNameContainsSpecial(fullNameFormat)) {
                    fullNameContainsSpecial = true;
                    toastMessage("Patient's name (First, Middle, or Last) contains an invalid special character");
                }

                if (fullNameAllSpecial == false &&
                        fullNameAllNumbers == false &&
                        fullNameContainsNumber == false &&
                        fullNameContainsSpecial == false) {

                    fullnamePatient[0] = captializeFirstLetter(editFirstName.getText().toString());
                    fullnamePatient[1] = captializeFirstLetter(editLastName.getText().toString());

                    if(TextUtils.isEmpty(editMiddleName.getText().toString())){
                        fullnamePatient[2] = " ";
                    }else{
                        fullnamePatient[2] = captializeFirstLetter(editMiddleName.getText().toString());
                    }

                    if(fullnamePatient[2].equals(" ")){
                        editTextName.setText(fullnamePatient[1] + ", " + fullnamePatient[0] + " " + fullnamePatient[2]);
                    }else{
                        editTextName.setText(fullnamePatient[1] + ", " + fullnamePatient[0] + " " + fullnamePatient[2].charAt(0) + ".");
                    }

                    alertDialog.dismiss();
                }
            }
        });
    }

    ////////////////////////ERROR HANDLINGS IN UPDATING///////////////////////////////////////////////
    public String captializeFirstLetter(String capitalize){
        String capitalized = null;
        String[] splitStr = capitalize.toLowerCase().split(" ");

        for(int i = 0; i<splitStr.length; i++){
            splitStr[i] = splitStr[i].toUpperCase().charAt(0) + splitStr[i].substring(1);
        }

        capitalized = TextUtils.join(" ", splitStr);

        return capitalized;
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

    public boolean checkBirthdateEqualCurrentDate(String bday){
        boolean ret = false;
        String[] date = bday.split("-");

        int agemonth = (Calendar.getInstance().get(Calendar.MONTH)+1) - Integer.parseInt(date[1]);
        int ageday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - Integer.parseInt(date[2]);
        int ageyear = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(date[0]);

        if(ageyear < 0 && (agemonth < 0 || (agemonth == 0 &&  ageday < 0))){
            ret = true;
        }


        return ret;
    }

    public boolean checkFullNameAllSpecial(String fullname){
        boolean ret = false;
        if(fullname.matches("[+×÷.-=/_€£¥₩!@#$%^&*()'\":;?`~<>¡¿]+")){
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

    public boolean checkAddressContainsNumber(String fullname){
        boolean ret = false;
        if(fullname.matches(".*[+×÷=/_€£¥₩!@#$%^&*()'\":;?`~<>¡¿]+.*")){
            ret = true;
        }
        return ret;
    }
    ////////////////////////ERROR HANDLINGS IN UPDATING///////////////////////////////////////////////

    //Actual updating in the database
    private boolean updatePatient(final String pid, final String firstname, final String lastname, final String middlename, final String bday, final String age, final String gender, final String address, final String lastscan, final String status, final String bid, final String oldName){
        final boolean[] ret = {false};

        db.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  boolean duplicatePatient = false;
                  for (DataSnapshot dsPatient : dataSnapshot.getChildren()) {
                      Patient patient = dsPatient.getValue(Patient.class);
                      if(firstname.equals(patient.getFirstname()) && lastname.equals(patient.getLastname()) && middlename.equals(patient.getMiddlename()) && patient.status.equals("1")){
                          if(!pid.equals(patient.getId())){
                              duplicatePatient = true;
                          }
                      }
                  }

                  if(duplicatePatient == false){
                      //finds the specific child given the pid
                      db = FirebaseDatabase.getInstance().getReference("person_information").child(pid);
                      dbLogs = FirebaseDatabase.getInstance().getReference("logs");

                      Patient patient = new Patient(pid, firstname, lastname, middlename, bday, age, gender, address, lastscan, status, bid);

                      //setting the value of the patient
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

    //search patient records based from the list within the barangay only
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
                        String pfullname;

                        //congesting the patient to become the basis of the search
                        if(patient.getMiddlename().equals(" ")){
                            pfullname = patient.getLastname() + ", " + patient.getFirstname() + " " + patient.getMiddlename();
                        }else{
                            pfullname = patient.getLastname() + ", " + patient.getFirstname() + " " + patient.getMiddlename().charAt(0) + ".";
                        }
                        //patient has to be in the same barangay and status is 1
                        if(pfullname.toLowerCase().contains(fullnameS.toLowerCase()) && bId.equals(patient.getbId()) && patient.getStatus().equals("1")){
                            pList.add(patient);
                        }
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

    //view all patient records from the specific barangay
    public void viewAllPatients(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the list of patients
                pList.clear();

                for (DataSnapshot pSnapshot : dataSnapshot.getChildren()){
                    Patient patient = pSnapshot.getValue(Patient.class);
//                    if(!patient.getFirstname().equals(null)){
                        if(patient.getStatus().equals("1") && bId.equals(patient.getbId())){
                            pList.add(patient);
                        }
//                    }
                }
                if(pList.isEmpty()){
                    patientErr.setVisibility(View.VISIBLE);
                }else{
                    sortPList();
                    PatientLists adapter = new PatientLists(PatientRecords.this, pList);

                    //setting the retrieved data to the listview
                    patient_record.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //sorting the patient record alphabetically
    public void sortPList(){
        if (pList.size() > 0) {
            Collections.sort(pList, new Comparator<Patient>() {
                @Override
                public int compare(final Patient object1, final Patient object2) {
                    return object1.getLastname().toLowerCase().compareTo(object2.getLastname().toLowerCase());
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
