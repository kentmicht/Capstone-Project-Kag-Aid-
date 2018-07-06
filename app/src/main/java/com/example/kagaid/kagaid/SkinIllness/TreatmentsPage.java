package com.example.kagaid.kagaid.SkinIllness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TreatmentsPage extends AppCompatActivity {

    TextView skin_illness_name;
    TextView medicine_name;
    TextView brand;
    TextView dosage;
    ListView treatmentsListView;

    DatabaseReference ref;
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

        ref = FirebaseDatabase.getInstance().getReference().child("skin_illnesses").child(illnessID).child("treatments");
        treatmentsListView = (ListView) findViewById(R.id.ListViewTreatments);
        treatmentsList = new ArrayList<>();

        skin_illness_name = (TextView) findViewById(R.id.skin_illness_name);
        medicine_name = (TextView) findViewById(R.id.medicine_name);
        brand = (TextView) findViewById(R.id.brand);
        dosage = (TextView) findViewById(R.id.dosage);

        skin_illness_name.setText("Most common for " + illnessName);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                treatmentsList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    treatment = ds.getValue(Treatments.class);
                    treatmentsList.add(treatment);
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
    }
}
