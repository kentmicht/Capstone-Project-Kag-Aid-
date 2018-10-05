package com.example.kagaid.kagaid.Patient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Logs.Log;
import com.example.kagaid.kagaid.Maps.MapsActivity;
import com.example.kagaid.kagaid.Patient.ScanningModule.MyHttpURLConnection;
import com.example.kagaid.kagaid.Patient.ScanningModule.RequestPackage;
import com.example.kagaid.kagaid.R;
import com.example.kagaid.kagaid.SkinIllness.TreatmentsPage;
import com.example.kagaid.kagaid.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import maes.tech.intentanim.CustomIntent;

public class ViewPatientInfo extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1111;
    private ImageView selectedImageView;
    private static final String UPLOAD_URL = "https://kag-aid.000webhostapp.com/uploads/uploadimage.php";
    private static final String RETRIEVE_URL = "https://kag-aid.000webhostapp.com/uploads/resultFile.txt";
    private static String _bytes64String, _imageFileName;
    private static String[] scannedResult;

    TextView textViewPatientName;
    TextView textViewPatientBday;
    TextView textviewPatientGender;
    TextView textViewPatientAddress;
    private Button btnCamera;
    String uId;
    String pId;

    //Image Bitmap
    Bitmap imageBitmap;

    AlertDialog.Builder dialogBuilder;
    String skinIllness;
    String skinIllnessId;
    String percentage;
    String currentDateTimeStored;
    String lastscan;

    String employeeName = null;
    String pfullname = null;
    String pbday = null;
    String pgender = null;
    String paddress = null;

    DatabaseReference databaseLogs;
    DatabaseReference databasePatient;
    DatabaseReference databaseEmployee;
    DatabaseReference databaseSkinIllness;
    DatabaseReference databaseScanResult;

    User u = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_info);

        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        }else {
            toastMessage("No Internet Connection");
        }

        //uId = (String) getIntent().getStringExtra("USER_ID");

        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewPatientBday = (TextView) findViewById(R.id.textViewPatientBday);
        textviewPatientGender = (TextView) findViewById(R.id.textViewPatientGender);
        textViewPatientAddress = (TextView) findViewById(R.id.textViewPatientAddress);

        Intent intent = getIntent();

        pfullname = intent.getStringExtra(PatientRecords.PATIENT_FULLNAME);
        pbday = intent.getStringExtra(PatientRecords.PATIENT_BIRTHDAY);
        pgender = intent.getStringExtra(PatientRecords.PATIENT_GENDER);
        paddress = intent.getStringExtra(PatientRecords.PATIENT_ADDRESS);
        pId = intent.getStringExtra(PatientRecords.PATIENT_ID);
        uId = intent.getStringExtra(PatientRecords.USER_ID);
        lastscan = intent.getStringExtra(PatientRecords.PATIENT_LAST_SCAN);

        toastMessage(lastscan);

        textViewPatientName.setText(pfullname);
        textViewPatientBday.setText(pbday);
        textviewPatientGender.setText(pgender);
        textViewPatientAddress.setText(paddress);

        toastMessage("User Id:" + uId + ", Patient Id: " + pId );

        _imageFileName = currentDateTime().replaceAll("\\s+","").replaceAll(",","").replaceAll(":","");

        btnCamera = (Button) findViewById(R.id.button2);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
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
        CustomIntent.customType(ViewPatientInfo.this, "right-to-left");
    }


    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//

        //camera setup
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_PIC_REQUEST){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
//                toastMessage("Laban besh! nakasulod oks" + _imagefileUri.getPath());
                uploadImage(imageBitmap);
            }
        }else if(resultCode == RESULT_CANCELED){
            toastMessage("User cancelled the image capture");
        }else{
            toastMessage("Sorry! Failed to capture image");
        }

    }

    private void uploadImage(Bitmap picture){
//        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] byteArray = bao.toByteArray();
        _bytes64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
        RequestPackage rp = new RequestPackage();
        rp.setMethod("POST");
        rp.setUri(UPLOAD_URL);
        rp.setSingleParam("base64", _bytes64String);
        rp.setSingleParam("ImageName", _imageFileName + ".jpg");

        uploadToServer uploadServer = new uploadToServer();
        uploadServer.execute(rp);
    }

    public class uploadToServer extends AsyncTask<RequestPackage, Void, String> {

        private ProgressDialog pd = new ProgressDialog(ViewPatientInfo.this);
        protected void onPreExecute() {
            super.onPreExecute();
//            resultText = (TextView) findViewById(R.id.textView);
//            resultText.setText("New file "+_imageFileName+".jpg created\n");
            pd.setMessage("Image uploading! please wait..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(RequestPackage... params) {

            String content = MyHttpURLConnection.getData(params[0]);
            return content;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();

            try {
                // Create a URL for the desired page
                URL url = new URL(RETRIEVE_URL);

                // Read all the text returned by the server
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = in.readLine();
                in.close();

                scannedResult = str.split(Pattern.quote("?"));
//                toastMessage("Skin Illness: " + scannedResult[0] + "Percentage: " + scannedResult[1]);

                showDiagnosisResults(scannedResult[1], scannedResult[0]);
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
        }
    }

    private void showDiagnosisResults(String skinIdentify, String percent){
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_diagnosis_results_dialog, null);

        dialogBuilder.setView(dialogView);


        final TextView skinIllnessTextName = (TextView) dialogView.findViewById(R.id.skin_illness_name);
        final TextView skinIllnessTextPercentage = (TextView) dialogView.findViewById(R.id.skin_illness_accuracy);
        final TextView lastScannedTextDatetime = (TextView) dialogView.findViewById(R.id.datetime_scanned);
        final ImageView okButton = (ImageView) dialogView.findViewById(R.id.ok_button);
        final ImageView mapsButton = (ImageView) dialogView.findViewById(R.id.find_nearby_doctors2);
        final ImageView treatmentButton = (ImageView) dialogView.findViewById(R.id.common_treatments2);

        //log all details percentage and skin illness identified most especially
        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");
        databasePatient = FirebaseDatabase.getInstance().getReference("person_information");
        databaseEmployee = FirebaseDatabase.getInstance().getReference("users");

        databaseEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (uId.equals(ds.child("uId").getValue().toString())) {
                        employeeName = ds.child("firstname").getValue().toString() + " " + ds.child("lastname").getValue().toString();
//
                    }
                }
                toastMessage(employeeName);
                String logId = databaseLogs.push().getKey();
                Log logSingle = new Log(logId, currentDateTime(), pId, uId, pfullname, employeeName, scannedResult[0], scannedResult[1]);
                String status = "1";
                String age = calculateAge(pbday);
                currentDateTimeStored = currentDateTime();
                Patient patient = new Patient(pId, pfullname, pbday, pgender, paddress, currentDateTimeStored, status, age);

                databasePatient.child(pId).setValue(patient);
                databaseLogs.child(logId).setValue(logSingle);
                toastMessage("Logged");

                skinIllnessTextName.setText(skinIllness);
                skinIllnessTextPercentage.setText(percentage);
                lastScannedTextDatetime.setText(currentDateTimeStored);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        skinIllness = skinIdentify;
        percentage = percent;

        databaseSkinIllness = FirebaseDatabase.getInstance().getReference("skin_illnesses");
        databaseSkinIllness.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (scannedResult[0].equals(ds.child("skin_illness_name").getValue().toString())) {
                        skinIllnessId = ds.child("siId").getValue().toString();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMaps(v);
            }
        });

        treatmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTreatments(v);
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void openTreatments(View view){
        final String SKIN_ILLNESS_NAME = "skin_illness_name";
        final String SKIN_ILLNESS_ID  = "skin_illness_id";
        toastMessage(skinIllnessId);

        Intent treatments = new Intent(this, TreatmentsPage.class);
        treatments.putExtra(SKIN_ILLNESS_NAME, skinIllness);
        treatments.putExtra(SKIN_ILLNESS_ID, skinIllnessId);
        startActivity(treatments);
    }
    public void openMaps(View view){
        Intent maps = new Intent(this, MapsActivity.class);
        startActivity(maps);
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
