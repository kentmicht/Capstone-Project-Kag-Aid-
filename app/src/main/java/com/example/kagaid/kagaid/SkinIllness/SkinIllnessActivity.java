package com.example.kagaid.kagaid.SkinIllness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SkinIllnessActivity extends AppCompatActivity {

    public static final String SKIN_ILLNESS_ID = "skin_illness_id";
    public static final String SKIN_ILLNESS_NAME = "skin_illness_name";
    public static final String SKIN_ILLNESS_DESC = "skin_illness_desc";
    public static final String LEVEL_OF_SEVERITY = "level_of_severity";
    public static final String IMAGE = "image";

    DatabaseReference ref;
    ListView skinIllnessListView;
    List<SkinIllness> illnessList;
    List<String> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_illness);

        ref = FirebaseDatabase.getInstance().getReference().child("skin_illnesses");
        skinIllnessListView = (ListView) findViewById(R.id.skinIllnessListView);
        illnessList = new ArrayList<>();
        idList = new ArrayList<>();

        skinIllnessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SkinIllness illness = illnessList.get(position);
                Intent intent = new Intent(getApplicationContext(), SkinIllnessPage.class);

                intent.putExtra(SKIN_ILLNESS_ID, idList.get(position));
                intent.putExtra(SKIN_ILLNESS_NAME, illness.getSkin_illness_name());
                intent.putExtra(SKIN_ILLNESS_DESC, illness.getSkin_illness_desc());
                intent.putExtra(LEVEL_OF_SEVERITY, illness.getLevel_of_severity());
                intent.putExtra(IMAGE, illness.getImage());

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                illnessList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    SkinIllness illness = ds.getValue(SkinIllness.class);
                    illnessList.add(illness);
                    idList.add(ds.getKey());
                }

                SkinIllnessList adapter = new SkinIllnessList(SkinIllnessActivity.this, illnessList);
                skinIllnessListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
