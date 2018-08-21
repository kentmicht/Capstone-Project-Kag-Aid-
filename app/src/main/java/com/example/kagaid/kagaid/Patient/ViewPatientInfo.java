package com.example.kagaid.kagaid.Patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Camera.Gallery;
import com.example.kagaid.kagaid.Camera.GalleryDbHelper;
import com.example.kagaid.kagaid.Camera.GalleryImg;
import com.example.kagaid.kagaid.R;

import java.io.InputStream;

import maes.tech.intentanim.CustomIntent;

public class ViewPatientInfo extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 200;
    private ImageView selectedImageView;

    TextView textViewPatientName;
    TextView textViewPatientBday;
    TextView textviewPatientGender;
    TextView textViewPatientAddress;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_info);
        userName = (String) getIntent().getStringExtra("USERNAME");

        textViewPatientName = (TextView) findViewById(R.id.textViewPatientName);
        textViewPatientBday = (TextView) findViewById(R.id.textViewPatientBday);
        textviewPatientGender = (TextView) findViewById(R.id.textViewPatientGender);
        textViewPatientAddress = (TextView) findViewById(R.id.textViewPatientAddress);

        Intent intent = getIntent();

        String pfullname = intent.getStringExtra(PatientRecords.PATIENT_FULLNAME);
        String pbday = intent.getStringExtra(PatientRecords.PATIENT_BIRTHDAY);
        String pgender = intent.getStringExtra(PatientRecords.PATIENT_GENDER);
        String paddress = intent.getStringExtra(PatientRecords.PATIENT_ADDRESS);

        textViewPatientName.setText(pfullname);
        textViewPatientBday.setText(pbday);
        textviewPatientGender.setText(pgender);
        textViewPatientAddress.setText(paddress);
    }

    @Override
    public void onBackPressed() {
        openPatientRecords();

    }

    public void openPatientRecords(){
        Intent intent = new Intent(this, PatientRecords.class);
        intent.putExtra("USERNAME", userName);
        finish();
        startActivity(intent);
        CustomIntent.customType(ViewPatientInfo.this, "right-to-left");
    }

    public void openCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }
}
