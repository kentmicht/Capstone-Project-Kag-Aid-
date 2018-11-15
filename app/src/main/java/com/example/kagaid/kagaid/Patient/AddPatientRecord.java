package com.example.kagaid.kagaid.Patient;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class AddPatientRecord extends AppCompatActivity {

    EditText address;
    TextView birthdate, fullname;
    Spinner gender;
    Button patientAdd;
    String uId;
    String bId;

    String[] fullnamePatient = new String[3];

    DatabaseReference databasePatient;
    private android.app.DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "AddPatientRecord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_record);

        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        }else {
            toastMessage("No Internet Connection");
        }

        uId = (String) getIntent().getStringExtra("USER_ID");
        bId = (String) getIntent().getStringExtra("BARANGAY_ID");
        databasePatient = FirebaseDatabase.getInstance().getReference("person_information");


        fullname = (TextView) findViewById(R.id.fullNameSeperated);
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

        fullname.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                showFullNameSeperatorDialog(fullnamePatient[0], fullnamePatient[1], fullnamePatient[2]);
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

    //show dialog box for seperating fullname (First Name, Last Name and Middle Name)
    public void showFullNameSeperatorDialog(final String firstName, final String lastName, final String middleName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_fullname_seperation_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editFirstName = (EditText) dialogView.findViewById(R.id.firstNameSep);
        final EditText editLastName = (EditText) dialogView.findViewById(R.id.lastNameSep);
        final EditText editMiddleName = (EditText) dialogView.findViewById(R.id.midNameSep);
        final Button fullNameBtn = (Button) dialogView.findViewById(R.id.fullnameButton);

        editFirstName.setText(firstName);
        editLastName.setText(lastName);
        editMiddleName.setText(middleName);

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
                        fullname.setText(fullnamePatient[1] + ", " + fullnamePatient[0] + " " + fullnamePatient[2]);
                    }else{
                        fullname.setText(fullnamePatient[1] + ", " + fullnamePatient[0] + " " + fullnamePatient[2].charAt(0) + ".");
                    }
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void addPatient(){
        final String bdayP = birthdate.getText().toString();
        final String[] addressP = {address.getText().toString()};
        final String genderP = gender.getSelectedItem().toString();
        final String fullnameP = fullname.getText().toString();


        if(!fullnameP.matches("Full Name") && !bdayP.matches("Birthdate") && !TextUtils.isEmpty(addressP[0])){
            //checking if there's unique
            databasePatient.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //error handlings
                    boolean duplicatePatient = false;
                    boolean duplicateBarangayPatient = false;
                    boolean yearBeyondCurrent = false;
                    boolean yearBeyondCurrentEqual = false;
                    boolean addressAllNumbers = false;
                    boolean addressAllSpecial = false;
                    boolean addressContainsSpecial = false;

                    for (DataSnapshot dsPatient : dataSnapshot.getChildren()) {
                        Patient patient = dsPatient.getValue(Patient.class);
                        if(fullnamePatient[0].equals(patient.getFirstname()) && fullnamePatient[1].equals(patient.getLastname()) && fullnamePatient[2].equals(patient.getMiddlename())  && patient.status.equals("1")){
                            duplicatePatient = true;
                            if(!patient.getbId().equals(bId)){
                                duplicateBarangayPatient = true;
                            }
                        }
                    }

                    if(duplicatePatient == false) {

                        if (checkBirthdateYear(bdayP) == false) {
                            yearBeyondCurrent = true;
                            toastMessage("Patient’s Birthdate year exceeds the current year");
                            birthdate.setError("Patient’s Birthdate year exceeds the current year");
                        }

                        if (checkBirthdateYearEqual(bdayP) == true) {
                            yearBeyondCurrentEqual = true;
                            toastMessage("Patient’s Birthdate Year must not be equal the current year");
                            birthdate.setError("Patient’s Birthdate Year must not be equal the current year");
                        }

                        if (checkAddressAllNumbers(addressP[0])) {
                            addressAllNumbers = true;
                            address.setError("Patient’s address is all digits");
                        } else if (checkAddressSpecial(addressP[0])) {
                            addressAllSpecial = true;
                            address.setError("Patient's address contains all invalid special character");
                        } else if (checkAddressContainsNumber(addressP[0])) {
                            addressContainsSpecial = true;
                            address.setError("Patient's address contains an invalid special character");
                        }

                        if (yearBeyondCurrent == false &&
                                yearBeyondCurrentEqual == false &&
                                addressAllNumbers == false &&
                                addressAllSpecial == false &&
                                addressContainsSpecial == false) {
                            //generating a new id for new patient
                            String pid = databasePatient.push().getKey();
                            String age = calculateAge(bdayP);
                            String status = "1";

                            //capitalizing First Letter
                            addressP[0] = captializeFirstLetter(addressP[0]);
                            Patient patient = new Patient(pid, fullnamePatient[0], fullnamePatient[1], fullnamePatient[2], bdayP, age, genderP, addressP[0], "Not yet scanned", status, bId);

                            //adding a new patient based from the generated new pid
                            databasePatient.child(pid).setValue(patient);

                            progressDialog();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toastMessage("Patient Record Added");
                                    finish();
                                    CustomIntent.customType(AddPatientRecord.this, "fadein-to-fadeout");
                                }
                            }, 2000);
                        }
                    }else if(duplicateBarangayPatient == true){
                        toastMessage("Patient exists in another barangay");
                    }else{
                        toastMessage("Patient has been added already");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            toastMessage("Please don't leave any field empty.");
        }
    }

    ////////////////////////ERROR HANDLINGS IN ADDING PATIENT///////////////////////////////////////////////
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

    public boolean checkBirthdateYearEqual(String bday){
        boolean ret = false;
        String age = null;
        String year = bday.substring(0, 4);

        if(Calendar.getInstance().get(Calendar.YEAR) == Integer.parseInt(year)){
            ret = true;
        }


        return ret;
    }

    public boolean checkFullNameAllSpecial(String fullname){
        boolean ret = false;
        if(fullname.matches("[+.-×÷=/_€£¥₩!@#$%^&*()'\":;?`~<>¡¿]+")){
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
    ////////////////////////ERROR HANDLINGS IN ADDING PATIENT///////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        finish();
        CustomIntent.customType(AddPatientRecord.this, "fadein-to-fadeout");
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
        CustomIntent.customType(AddPatientRecord.this, "fadein-to-fadeout");
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void progressDialog(){
        ProgressDialog pd = new ProgressDialog(AddPatientRecord.this);
        pd.setMessage("Processing Patient's information...");
        pd.setCancelable(false);
        pd.show();
    }




}
