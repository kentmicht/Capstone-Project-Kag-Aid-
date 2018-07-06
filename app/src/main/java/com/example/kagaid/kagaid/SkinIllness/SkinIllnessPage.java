package com.example.kagaid.kagaid.SkinIllness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kagaid.kagaid.Maps.MapsActivity;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SkinIllnessPage extends AppCompatActivity {

    TextView skinIllness_ID;
    TextView skinIllness_name;
    TextView skinIllness_desc;
    TextView skinIllness_los;
    ImageView image;

    String illnessName;
    String illnessDesc;
    String illnessImage;
    String illnessID;
    String illnessLOS;

    public static final String SKIN_ILLNESS_NAME = "skin_illness_name";
    public static final String SKIN_ILLNESS_ID  = "skin_illness_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_illness_page);

        image = (ImageView) findViewById(R.id.image);
        skinIllness_name = (TextView) findViewById(R.id.skinIllness_name);
        skinIllness_desc = (TextView) findViewById(R.id.skinIllness_desc);
        skinIllness_ID = (TextView) findViewById(R.id.skinIllness_id);
        skinIllness_los = (TextView) findViewById(R.id.level_of_severity);

        Intent intent = getIntent();

        illnessID = intent.getStringExtra(SkinIllnessActivity.SKIN_ILLNESS_ID);
        illnessName = intent.getStringExtra(SkinIllnessActivity.SKIN_ILLNESS_NAME);
        illnessDesc = intent.getStringExtra(SkinIllnessActivity.SKIN_ILLNESS_DESC);
        illnessLOS = intent.getStringExtra(SkinIllnessActivity.LEVEL_OF_SEVERITY);
        illnessImage = intent.getStringExtra(SkinIllnessActivity.IMAGE);

        skinIllness_ID.setText(illnessID);
        skinIllness_name.setText(illnessName);
        skinIllness_desc.setText(illnessDesc);
        skinIllness_los.setText(illnessLOS);
        Picasso.get().load(illnessImage).into(image);

    }

    public void openTreatments(View view){
        Intent treatments = new Intent(this, TreatmentsPage.class);
        treatments.putExtra(SKIN_ILLNESS_NAME, illnessName);
        treatments.putExtra(SKIN_ILLNESS_ID, illnessID);
        startActivity(treatments);
    }
    public void openMaps(View view){
        Intent maps = new Intent(this, MapsActivity.class);
        startActivity(maps);
    }
}
