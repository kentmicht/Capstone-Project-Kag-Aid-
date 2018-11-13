package com.example.kagaid.kagaid.Patient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.RequestQueue;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kagaid.kagaid.Logs.Log;
import com.example.kagaid.kagaid.Maps.MapsActivity;
import com.example.kagaid.kagaid.Patient.ScanningModule.MyHttpURLConnection;
import com.example.kagaid.kagaid.Patient.ScanningModule.RequestPackage;
import com.example.kagaid.kagaid.R;
import com.example.kagaid.kagaid.SkinIllness.SkinIllnessPage;
import com.example.kagaid.kagaid.SkinIllness.TreatmentsPage;
import com.example.kagaid.kagaid.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.stream.JsonReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import maes.tech.intentanim.CustomIntent;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ViewPatientInfo extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1111;

    private static String _bytes64String, _imageFileName;
    private static String[] scannedResult = new String[2];

    //Custom Vision Prediction API
    private static final String predictionKey = "1289ea1f967b43c0ba970bc485e1c869";
    String customVisionURL =
            "https://southcentralus.api.cognitive.microsoft.com/customvision/v2.0/Prediction/c3c28515-756c-42f1-bdae-0a86197064b5/image";



    TextView textViewPatientName;
    TextView textViewPatientBday;
    TextView textviewPatientGender;
    TextView textViewPatientAddress;
    TextView barangay;
    private Button btnCamera;

    String uId;
    String pId;
    String bId;
    String bName;

    //Image Bitmap
    Bitmap imageBitmap;
    byte[] byteArray;

    AlertDialog.Builder dialogBuilder;
    String skinIllness;
    String skinIllnessId;
    String percentage;
    String currentDateTimeStored;
    String lastscan;

    String employeeName = null;
    String pfullname = null;
    String pfirstname = null;
    String plastname = null;
    String pmiddlename = null;
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

        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewPatientBday = (TextView) findViewById(R.id.textViewPatientBday);
        textviewPatientGender = (TextView) findViewById(R.id.textViewPatientGender);
        textViewPatientAddress = (TextView) findViewById(R.id.textViewPatientAddress);
        barangay = (TextView) findViewById(R.id.barangayName);

        Intent intent = getIntent();

        pfirstname = intent.getStringExtra("PATIENT_FIRSTNAME");
        plastname = intent.getStringExtra("PATIENT_LASTNAME");
        pmiddlename = intent.getStringExtra("PATIENT_MIDDLENAME");
        pbday = intent.getStringExtra(PatientRecords.PATIENT_BIRTHDAY);
        pgender = intent.getStringExtra(PatientRecords.PATIENT_GENDER);
        paddress = intent.getStringExtra(PatientRecords.PATIENT_ADDRESS);
        pId = intent.getStringExtra(PatientRecords.PATIENT_ID);
        uId = intent.getStringExtra(PatientRecords.USER_ID);
        lastscan = intent.getStringExtra(PatientRecords.PATIENT_LAST_SCAN);
        bId = intent.getStringExtra("BARANGAY_ID");
        bName = intent.getStringExtra("BARANGAY_NAME");

        if(pmiddlename.equals(" ")){
            pfullname = plastname + ", " + pfirstname + " " + pmiddlename;
        }else{
            pfullname = plastname + ", " + pfirstname + " " + pmiddlename.charAt(0) + ".";
        }


        textViewPatientName.setText(pfullname);
        textViewPatientBday.setText(pbday);
        textviewPatientGender.setText(pgender);
        textViewPatientAddress.setText(paddress);
        barangay.setText(bName);

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

    //open the Individual Patient Records page
    public void openPatientRecords(){
        Intent intent = new Intent(this, PatientRecords.class);
        intent.putExtra("USER_ID", uId);
        intent.putExtra("BARANGAY_ID", bId);
        intent.putExtra("BARANGAY_NAME", bName);
        finish();
        startActivity(intent);
        CustomIntent.customType(ViewPatientInfo.this, "fadein-to-fadeout");
    }

    //opens the camera for image capture
    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//

        //camera setup
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
            CustomIntent.customType(ViewPatientInfo.this, "fadein-to-fadeout");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_PIC_REQUEST){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                uploadImage(imageBitmap);
            }
        }else if(resultCode == RESULT_CANCELED){
            toastMessage("User cancelled the image capture");
        }else{
            toastMessage("Sorry! Failed to capture image");
        }

    }

    private void uploadImage(Bitmap picture) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byteArray = bao.toByteArray();
        _bytes64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

        new HttpAsyncTask().execute(customVisionURL);

    }

    public String POST(String url)
    {
        String result = "";
        int responseCode=0;
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

                //httpPost Entity
                httpPost.setEntity(new ByteArrayEntity(byteArray));

                //set some headers to inform server about the type of the content
                httpPost.setHeader("Content-Type", "application/octet-stream");
                httpPost.setHeader("Prediction-Key", predictionKey);

                //execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);
                Log.e("MINION", "Post request successful");
                HttpEntity entity = httpResponse.getEntity();

                //retrieveing the response of the server api
                String responseText = EntityUtils.toString(entity);

                responseCode = httpResponse.getStatusLine().getStatusCode();
                System.out.println("Response Code: " + responseCode);
                System.out.println("Response Message: " + responseText);

            //retrieving the json file of the skin illness results
            result = responseText;
        } catch (Exception e) {
            Log.e("InputStream", e.toString());
        }
        System.out.println("Result: " + result);
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pd = new ProgressDialog(ViewPatientInfo.this);

        //showing the dialog on retrieving the result of the captured image
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Identifying, please wait..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask and the identified skin illness.
        @Override
        protected void onPostExecute(String result) {
            //retrieving the json file and getting the highest confidence
            try {
                JSONObject json = new JSONObject(result);
                JSONArray skinResults = json.getJSONArray("predictions");
                JSONObject skinRes = skinResults.getJSONObject(0);
                double percentageNum = Double.parseDouble(skinRes.getString("probability")) * 100.00;
                String percentage = String.format("%.2f%%", percentageNum);
                String skinName = skinRes.getString("tagName");

                scannedResult[0] = percentage;
                scannedResult[1] = skinName;

                showDiagnosisResults(scannedResult[1], scannedResult[0]);
                pd.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //showing the diagnosis result of the identified skin illness
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
        final TextView accuracyNote = (TextView) dialogView.findViewById(R.id.note);
        final TextView scan = (TextView) dialogView.findViewById(R.id.scan_again);

        accuracyNote.setVisibility(View.INVISIBLE);
        scan.setVisibility(View.INVISIBLE);

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
//                toastMessage(employeeName);
                String logId = databaseLogs.push().getKey();

                Log logSingle = new Log(logId, currentDateTime(), pId, uId, pfullname, employeeName, scannedResult[1], scannedResult[0], bId);
                String status = "1";
                String age = calculateAge(pbday);
                currentDateTimeStored = currentDateTime();
                Patient patient = new Patient(pId, pfirstname, plastname, pmiddlename, pbday, age, pgender, paddress, currentDateTimeStored, status,  bId);

                databasePatient.child(pId).setValue(patient);
                databaseLogs.child(logId).setValue(logSingle);
                toastMessage("Logged");



                skinIllnessTextName.setText(skinIllness);
                skinIllnessTextPercentage.setText(percentage);
                lastScannedTextDatetime.setText(currentDateTimeStored);

                if(Double.parseDouble(scannedResult[0].split("%")[0]) < 80.0){
                    accuracyNote.setVisibility(View.VISIBLE);
                    scan.setVisibility(View.VISIBLE);

                }else{
                    scan.setTextSize(1, 1);
                    accuracyNote.setVisibility(View.VISIBLE);
                    accuracyNote.setText("Confidence is good and may refer to the over-the-counter treatments");
                }

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
                    if (scannedResult[1].equals(ds.child("skin_illness_name").getValue().toString())) {
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
                if(Double.parseDouble(scannedResult[0].split("%")[0]) < 80.0) {
                    toastMessage("Confidence is below 80% and may not recommend an over-the-counter treatment");
                }else{
                    openTreatments(v);
                }
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSkinIllness(v);
            }
        });
    }

    //open the treatments page based from the skin illness result name
    public void openTreatments(View view){
        final String SKIN_ILLNESS_NAME = "skin_illness_name";
        final String SKIN_ILLNESS_ID  = "skin_illness_id";

        Intent treatments = new Intent(this, TreatmentsPage.class);
        treatments.putExtra("skin_illness_name", scannedResult[1]);
        treatments.putExtra("skin_illness_id", skinIllnessId);
        startActivity(treatments);
        CustomIntent.customType(ViewPatientInfo.this, "fadein-to-fadeout");
    }

    //open the skin illness page based from the skin illness result name
    public void openSkinIllness(View view){
        final String SKIN_ILLNESS_NAME = "SKIN_ILLNESS_NAME";
        final String SKIN_ILLNESS_ID  = "SKIN_ILLNESS_ID";

        Intent skinIllness = new Intent(this, SkinIllnessPage.class);
        skinIllness.putExtra("SKIN_ILLNESS_NAME", scannedResult[1]);
        skinIllness.putExtra(SKIN_ILLNESS_ID, skinIllnessId);
        startActivity(skinIllness);
        CustomIntent.customType(ViewPatientInfo.this, "fadein-to-fadeout");
    }

    //open the maps page
    public void openMaps(View view){
        Intent maps = new Intent(this, MapsActivity.class);
        startActivity(maps);
        CustomIntent.customType(ViewPatientInfo.this, "fadein-to-fadeout");
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
        openPatientRecords();
    }
}
