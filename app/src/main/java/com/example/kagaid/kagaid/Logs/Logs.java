package com.example.kagaid.kagaid.Logs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kagaid.kagaid.Homepage;
import com.example.kagaid.kagaid.Patient.Patient;
import com.example.kagaid.kagaid.Patient.PatientLists;
import com.example.kagaid.kagaid.Patient.PatientRecords;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class Logs extends AppCompatActivity {

    ListView listViewLogs;
    DatabaseReference databaseLogs;

    List<Log> logList;
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        uId = (String) getIntent().getStringExtra("USER_ID");
        listViewLogs = (ListView) findViewById(R.id.listViewLogs);

        logList = new ArrayList<>();
        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");
    }

    @Override
    protected void onStart() {
        super.onStart();


        viewAllLogs();

        ImageView search = (ImageView) findViewById(R.id.logSearchBtn);
        search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                searchLog();
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
        intent.putExtra("USER_ID", uId);
        startActivity(intent);
        CustomIntent.customType(Logs.this, "fadein-to-fadeout");
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


    public void searchLog(){
        Spinner logCateg= (Spinner) findViewById(R.id.logCategory);
        final String logCategory = logCateg.getSelectedItem().toString();

        EditText logS = (EditText) findViewById(R.id.logSearch);
        final String logSearch = logS.getText().toString();

        if(logSearch.matches("")) {
            toastMessage("Nothing to search");
            viewAllLogs();
        }else{
            databaseLogs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    logList.clear();
                    for(DataSnapshot logsSnapshot: dataSnapshot.getChildren()){
                        Log log = logsSnapshot.getValue(Log.class);
                        switch(logCategory){
                            case "Date":
                                if(log.getLogdatetime().toLowerCase().contains(logSearch.toLowerCase())){
                                    logList.add(log);
                                }
                                break;
                            case "Time":
                                if(log.getLogdatetime().toLowerCase().contains(logSearch.toLowerCase())){
                                    logList.add(log);
                                }
                                break;
                            case "Patient Name":
                                if(log.getPatientName().toLowerCase().contains(logSearch.toLowerCase())){
                                    logList.add(log);
                                }
                                break;
                            case "Employee Name":
                                if(log.getEmployeeName().toLowerCase().contains(logSearch.toLowerCase())){
                                    logList.add(log);
                                }
                                break;
                        }

                    }

                    if(logList.isEmpty()) {
                        toastMessage("No Match");
                        viewAllLogs();
                    }else {
                        LogLists adapter = new LogLists(Logs.this, logList);
                        listViewLogs.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public void viewAllLogs(){
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

    public void back(View view){
        finish();
    }
}
