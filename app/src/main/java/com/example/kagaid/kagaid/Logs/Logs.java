package com.example.kagaid.kagaid.Logs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.kagaid.kagaid.Homepage;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class Logs extends AppCompatActivity {

    ListView listViewLogs;
    DatabaseReference databaseLogs;

    List<Log> logList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        listViewLogs = (ListView) findViewById(R.id.listViewLogs);

        logList = new ArrayList<>();
        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseLogs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                logList.clear();
                for(DataSnapshot logsSnapshot: dataSnapshot.getChildren()){
                    Log log = logsSnapshot.getValue(Log.class);
                    logList.add(log);
                }

                LogLists adapter = new LogLists(Logs.this, logList);
                listViewLogs.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        openHomepage();

    }

    public void openHomepage(){
        Intent intent = new Intent(this, Homepage.class);
        finish();
        startActivity(intent);
        CustomIntent.customType(Logs.this, "fadein-to-fadeout");
    }
}
