package com.example.kagaid.kagaid.SkinIllness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TreatmentsPage extends AppCompatActivity {

    TextView skin_illness_name;
    TextView medicine_name;
    TextView brand;
    TextView dosage;
    ListView treatmentsListView;

    DatabaseReference refTreatments;
    DatabaseReference refSkinIllness;
    List<Treatments> treatmentsList;
    Treatments treatment, commonTreatment;

    String illnessName;
    String illnessID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatments_page);

        Intent intent = getIntent();
        illnessName = intent.getStringExtra(SkinIllnessPage.SKIN_ILLNESS_NAME);
        illnessID = intent.getStringExtra(SkinIllnessPage.SKIN_ILLNESS_ID);

        treatmentsListView = (ListView) findViewById(R.id.ListViewTreatments);
        treatmentsList = new ArrayList<>();

        skin_illness_name = (TextView) findViewById(R.id.skin_illness_name);
        medicine_name = (TextView) findViewById(R.id.medicine_name);
        brand = (TextView) findViewById(R.id.brand);
        dosage = (TextView) findViewById(R.id.dosage);
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
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if(illnessID.equals(ds.child("skin_illness_id").getValue().toString())){
                        treatment = ds.getValue(Treatments.class);
                        treatmentsList.add(treatment);
                    }

                }
                commonTreatment = treatment;
                medicine_name.setText(commonTreatment.getMedicine_name());
                brand.setText("Brand: " + commonTreatment.getBrand());
                dosage.setText("Dosage: " + commonTreatment.getDosage());

                TreatmentsList adapter = new TreatmentsList(TreatmentsPage.this, treatmentsList);
                treatmentsListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refSkinIllness.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if(illnessID.equals(ds.child("siId").getValue().toString())){
                        illnessName = ds.child("skin_illness_name").getValue().toString();
                    }

                }
                skin_illness_name.setText(illnessName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
