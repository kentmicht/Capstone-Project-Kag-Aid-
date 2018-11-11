package com.example.kagaid.kagaid.SkinIllness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Maps.MapsActivity;
import com.example.kagaid.kagaid.Patient.ViewPatientInfo;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import maes.tech.intentanim.CustomIntent;

public class SkinIllnessPage extends AppCompatActivity {

    TextView skinIllness_ID;
    TextView skinIllness_name;
    TextView skinIllness_desc;
    TextView skinIllness_symptom;
    ImageView image;

    String illnessName;
    String illnessDesc;
    String illnessImage;
    String illnessSymptom;

    boolean existDetails = false;

    public static final String SKIN_ILLNESS_NAME = "SKIN_ILLNESS_NAME";
    public static final String SKIN_ILLNESS_ID  = "SKIN_ILLNESS_ID";

    DatabaseReference databaseSkinIllness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_illness_page);

        image = (ImageView) findViewById(R.id.image);
        skinIllness_name = (TextView) findViewById(R.id.skinIllness_name);
        skinIllness_desc = (TextView) findViewById(R.id.skinIllness_desc);
        skinIllness_symptom = (TextView) findViewById(R.id.symptoms);
//        skinIllness_ID = (TextView) findViewById(R.id.skinIllness_id);

        Intent intent = getIntent();
        illnessName = intent.getStringExtra("SKIN_ILLNESS_NAME");

//        toastMessage(illnessName);

        databaseSkinIllness = FirebaseDatabase.getInstance().getReference("skin_illnesses");
//
        databaseSkinIllness.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (illnessName.equals(ds.child("skin_illness_name").getValue().toString())) {
//                        toastMessage("Perfect found it!");

                        illnessName = ds.child("skin_illness_name").getValue().toString();
                        illnessDesc = ds.child("skin_illness_desc").getValue().toString();
                        illnessImage = ds.child("image").getValue().toString();
                        illnessSymptom = ds.child("skin_illness_symptoms").getValue().toString();
                        existDetails = true;
                    }
                }

                if(existDetails){
                    skinIllness_name.setText(illnessName);
                    skinIllness_desc.setText(illnessDesc);
                    skinIllness_symptom.setText(illnessSymptom);
                    Picasso.get().load(illnessImage).into(image);
                }else{
                    toastMessage("No details for " + illnessName);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        if(existDetails == true){
//            skinIllness_name.setText(illnessName);
//            skinIllness_desc.setText(illnessDesc);
//            skinIllness_symptom.setText("Symptoms: " + illnessSymptom);
//            Picasso.get().load(illnessImage).into(image);
//        }else{
//            toastMessage("No details for " + illnessName);
//        }


    }

    public void back(View view){
        finish();
        CustomIntent.customType(SkinIllnessPage.this, "fadein-to-fadeout");
    }

    @Override
    public void onBackPressed() {
        finish();
        CustomIntent.customType(SkinIllnessPage.this, "fadein-to-fadeout");
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
