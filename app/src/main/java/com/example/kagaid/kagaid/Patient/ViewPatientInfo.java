package com.example.kagaid.kagaid.Patient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.kagaid.kagaid.Maps.MapsActivity;
import com.example.kagaid.kagaid.Patient.ScanningModule.MyHttpURLConnection;
import com.example.kagaid.kagaid.Patient.ScanningModule.RequestPackage;
import com.example.kagaid.kagaid.R;
import com.example.kagaid.kagaid.SkinIllness.TreatmentsPage;
import com.example.kagaid.kagaid.User;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
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

    AlertDialog.Builder dialogBuilder;
    private Button buttonActive;

    String employeeName = null;
    String pfullname = null;
    String pbday = null;
    String pgender = null;
    String paddress = null;

    DatabaseReference databaseLogs;
    DatabaseReference databasePatient;
    DatabaseReference databaseEmployee;

    User u = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_info);
        //uId = (String) getIntent().getStringExtra("USER_ID");

        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewPatientBday = (TextView) findViewById(R.id.textViewPatientBday);
        textviewPatientGender = (TextView) findViewById(R.id.textViewPatientGender);
        textViewPatientAddress = (TextView) findViewById(R.id.textViewPatientAddress);

        //for enable/disable button
        buttonActive = (Button) findViewById(R.id.active_status);
        buttonActive.setEnabled(scannedResult != null);

        Intent intent = getIntent();

        pfullname = intent.getStringExtra(PatientRecords.PATIENT_FULLNAME);
        pbday = intent.getStringExtra(PatientRecords.PATIENT_BIRTHDAY);
        pgender = intent.getStringExtra(PatientRecords.PATIENT_GENDER);
        paddress = intent.getStringExtra(PatientRecords.PATIENT_ADDRESS);
        pId = intent.getStringExtra(PatientRecords.PATIENT_ID);
        uId = intent.getStringExtra(PatientRecords.USER_ID);
        //String lastscan = intent.getStringExtra(PatientRecords.PATIENT_LAST_SCAN);

        textViewPatientName.setText(pfullname);
        textViewPatientBday.setText(pbday);
        textviewPatientGender.setText(pgender);
        textViewPatientAddress.setText(paddress);

        toastMessage("User Id:" + uId + ", Patient Id: " + pId );
        //", Last Scan: " + lastscan

        //Enable & Disable Button


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

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }


//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, _imagefileUri);


//        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");
//        databasePatient = FirebaseDatabase.getInstance().getReference("person_information");
//
//        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");
//        databasePatient = FirebaseDatabase.getInstance().getReference("person_information");
//        databaseEmployee = FirebaseDatabase.getInstance().getReference("users");
//
//        databaseEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    //toastMessage(ds.child());
////                    u = ds.getValue(User.class);
//                    if (uId.equals(ds.child("uId").getValue().toString())) {
//                        //if (.equals(u.getUId()))
//                        employeeName = ds.child("firstname").getValue().toString() + " " + ds.child("lastname").getValue().toString();
////
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        if(employeeName==null){
//            Toast.makeText(this, "Pres Scan Again", Toast.LENGTH_LONG).show();
//        }else{
//            //add Logs
//            String logId = databaseLogs.push().getKey();
//            Log logSingle = new Log(logId, currentDateTime(), pId, uId, pfullname, employeeName);
//            String status = "1";
//            String age = calculateAge(pbday);
//            Patient patient = new Patient(pId, pfullname, pbday, pgender, paddress, currentDateTime(), status, age);
//
//            databasePatient.child(pId).setValue(patient);
//            databaseLogs.child(logId).setValue(logSingle);
//            Toast.makeText(this, "Logged", Toast.LENGTH_LONG).show();
//
//            //camera setup
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//
//            }
//
//
//
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_PIC_REQUEST){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
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
                buttonActive.setEnabled(scannedResult != null);
                showDiagnosisResults(scannedResult[0],scannedResult[1]);
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
        }
    }

    private void showDiagnosisResults(String skinIllnessName, String percentage){
//        toastMessage("Skin Illness: " + skinIllnessName + "Percentage: " + percentage);

        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_diagnosis_results_dialog, null);

        dialogBuilder.setView(dialogView);

        final TextView skinIllnessTextName = (TextView) dialogView.findViewById(R.id.skin_illness_name);
        final TextView skinIllnessTextPercentage = (TextView) dialogView.findViewById(R.id.skin_illness_accuracy);
        final TextView lastScannedTextDatetime = (TextView) dialogView.findViewById(R.id.datetime_scanned);
        final Button okButton = (Button) dialogView.findViewById(R.id.ok_button);

        //Gets the current date and time from the currentdatetime() function
        String lastScannedDateTime;
        lastScannedDateTime = currentDateTime();

        skinIllnessTextName.setText(skinIllnessName);
        skinIllnessTextPercentage.setText(percentage);
        lastScannedTextDatetime.setText(lastScannedDateTime);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
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
