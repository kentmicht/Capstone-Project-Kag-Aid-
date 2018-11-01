package com.example.kagaid.kagaid.SkinIllness;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Patient.ViewPatientInfo;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class TreatmentsPage extends AppCompatActivity {

    TextView skin_illness_name;
    TextView medicine_name;
    TextView brand;
    TextView dosage;
    ListView treatmentsListView;
    TextView treatmentErr;

    DatabaseReference refTreatments;
    DatabaseReference refSkinIllness;
    List<Treatments> treatmentsList;
    Treatments treatment, commonTreatment;

    String illnessName;
    String illnessID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            setContentView(R.layout.activity_treatments_page);
        }else {
            toastMessage("No Internet Connection");
        }

        treatmentErr = (TextView) findViewById(R.id.treatmentError);
        treatmentErr.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        illnessName = intent.getStringExtra("skin_illness_name");
        illnessID = intent.getStringExtra("skin_illness_id");

        treatmentsListView = (ListView) findViewById(R.id.ListViewTreatments);
        treatmentsList = new ArrayList<>();

        skin_illness_name = (TextView) findViewById(R.id.skin_illness_name);
        medicine_name = (TextView) findViewById(R.id.medicine_name);
        brand = (TextView) findViewById(R.id.brand);
        dosage = (TextView) findViewById(R.id.dosage);

        skin_illness_name.setText(illnessName);
    }

    @Override
    protected void onStart() {
        super.onStart();

        refTreatments = FirebaseDatabase.getInstance().getReference("otc_treatments");
        refSkinIllness = FirebaseDatabase.getInstance().getReference("skin_illnesses");

        refTreatments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                treatmentsList.clear();
                boolean ret = false;
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Treatments treat = ds.getValue(Treatments.class);
//                    toastMessage(illnessID);
                    if(illnessID.equals(treat.getSkin_illness_id())){
//                        toastMessage("here na si me!");
                        treatment = treat;
                        treatmentsList.add(treatment);
                        ret = true;
                    }

                }


                if(ret) {
//                    toastMessage("There's treatment");
                    commonTreatment = treatment;
                    medicine_name.setText(commonTreatment.getMedicine_name());
                    brand.setText("Brand: " + commonTreatment.getBrand());
                    dosage.setText("Dosage: " + commonTreatment.getDosage());

                    TreatmentsList adapter = new TreatmentsList(TreatmentsPage.this, treatmentsList);
                    treatmentsListView.setAdapter(adapter);
//
                }else{
//                    toastMessage("No treatment");
                    treatmentErr.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        refSkinIllness.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds: dataSnapshot.getChildren()){
//                    if(illnessID.equals(ds.child("siId").getValue().toString())){
//                        illnessName = ds.child("skin_illness_name").getValue().toString();
//                    }
//
//                }
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    public void back(View view){
        finish();
        CustomIntent.customType(TreatmentsPage.this, "fadein-to-fadeout");
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        CustomIntent.customType(TreatmentsPage.this, "fadein-to-fadeout");

    }

}
