package com.example.kagaid.kagaid.Logs;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class Logs extends AppCompatActivity {

    ListView listViewLogs;
    DatabaseReference databaseLogs;
    TextView logErr;
    EditText logS;
    Spinner logCateg;

    List<Log> logList;
    String uId;
    String bId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        }else {
            toastMessage("No Internet Connection");
        }

        logErr = (TextView) findViewById(R.id.logError);
        logErr.setVisibility(View.INVISIBLE);
        logS = (EditText) findViewById(R.id.logSearch);

        uId = (String) getIntent().getStringExtra("USER_ID");
        bId = (String) getIntent().getStringExtra("BARANGAY_ID");
        listViewLogs = (ListView) findViewById(R.id.listViewLogs);

        logList = new ArrayList<>();
        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");

        ImageView search = (ImageView) findViewById(R.id.logSearchBtn);
        logCateg= (Spinner) findViewById(R.id.logCategory);

//        search.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
////                toastMessage(logCategory);
//                String logCategory = logCateg.getSelectedItem().toString();
//                searchLog(logCategory);
//            }
//        });

        onSearchLogs(logS);
        logS.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            String logCategory = logCateg.getSelectedItem().toString();
                            searchLog(logCategory);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        viewAllLogs();


        //Show dialog for logs
        listViewLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log log = logList.get(position);

                showLogDialog(log.getLogId(), log.getPatientName(), log.getEmployeeName(), log.getLogdatetime(), log.getPercentage(), log.getSkinIllness(), log.getpId());

            }
        });
    }

    public void onSearchLogs(EditText logS){
        logS.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            String logCategory = logCateg.getSelectedItem().toString();
                            searchLog(logCategory);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void showLogDialog(String logId, String patientName, String empName, String logDateTime, String percentage, String skinIllness, final String pId){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_log_info_dialog, null);

        dialogBuilder.setView(dialogView);

        final TextView skinIdentify = (TextView) dialogView.findViewById(R.id.textView34);
        final TextView percent = (TextView) dialogView.findViewById(R.id.textView44);
        final TextView patient = (TextView) dialogView.findViewById(R.id.textView41);
        final TextView employee = (TextView) dialogView.findViewById(R.id.textView42);
        final TextView dateTime = (TextView) dialogView.findViewById(R.id.textView43);

        DatabaseReference refPatient;
        refPatient = FirebaseDatabase.getInstance().getReference("person_information");
        refPatient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String patientName = null;
                for(DataSnapshot patientSnapshot: dataSnapshot.getChildren()){
                    Patient p = patientSnapshot.getValue(Patient.class);
                    if(p.getPid().equals(pId)){
                        patientName = p.getFullname();
                    }

                }
                patient.setText(patientName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        skinIdentify.setText(skinIllness);
        percent.setText(percentage);
        employee.setText(empName);
        dateTime.setText(logDateTime);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        ImageView xButton = (ImageView) dialogView.findViewById(R.id.imageView18);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        CustomIntent.customType(Logs.this, "fadein-to-fadeout");
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


    public void searchLog(final String logCategory){
        final String logSearch = logS.getText().toString();

        if(logSearch.matches("")) {
            toastMessage("Nothing to search");
            viewAllLogs();
        }else{
            databaseLogs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    logList.clear();
                    String currentDate[] = currentDateTime().split("(?<=\\d{3})\\s");
                    for(DataSnapshot logsSnapshot: dataSnapshot.getChildren()){
                        Log log = logsSnapshot.getValue(Log.class);
                        String logDate[] = log.getLogdatetime().split("(?<=\\d{3})\\s");
                        switch(logCategory){
                            case "Date":
                                if(log.getLogdatetime().toLowerCase().contains(logSearch.toLowerCase()) && bId.equals(logsSnapshot.child("bId").getValue().toString())){
                                    if (currentDate[0].equals(logDate[0])) {
                                        logList.add(log);
                                    }
                                }
                                break;
                            case "Time":
                                if(log.getLogdatetime().toLowerCase().contains(logSearch.toLowerCase()) && bId.equals(logsSnapshot.child("bId").getValue().toString())){
                                    if (currentDate[0].equals(logDate[0])) {
                                        logList.add(log);
                                    }
                                }
                                break;
                            case "Patient Name":
                                if(log.getPatientName().toLowerCase().contains(logSearch.toLowerCase()) && bId.equals(logsSnapshot.child("bId").getValue().toString())){
                                    if (currentDate[0].equals(logDate[0])) {
                                        logList.add(log);
                                    }
                                }
                                break;
                            case "Employee Name":
//                                toastMessage(log.getEmployeeName());
                                if(!log.getEmployeeName().isEmpty()){
                                    if(log.getEmployeeName().toLowerCase().contains(logSearch.toLowerCase()) && bId.equals(logsSnapshot.child("bId").getValue().toString())){
                                        if (currentDate[0].equals(logDate[0])) {
                                            logList.add(log);
                                        }
                                    }
//                                    toastMessage(log.getEmployeeName());
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
                String currentDate[] = currentDateTime().split("(?<=\\d{3})\\s");
                for(DataSnapshot logsSnapshot: dataSnapshot.getChildren()){
                    Log log = logsSnapshot.getValue(Log.class);
                    String logDate[] = log.getLogdatetime().split("(?<=\\d{3})\\s");
//                    toastMessage(bId);
//                    if(!log.getbId().equals(null)) {
                        if (currentDate[0].equals(logDate[0])) {
//                        if(currentDate[0].equals(logDate[0]) && bId.equals(log.getbId())){
                            if(bId.equals(log.getbId())){
//                                toastMessage(log.getLogdatetime());
//                                toastMessage("Log Class:" + log.getbId());
//                                toastMessage("Passed: " + bId);
                                logList.add(log);
                            }

//
                        }
//                    }

                }

//                toastMessage(bId);

                if(logList.isEmpty()){
                    logErr.setVisibility(View.VISIBLE);
                }else{
                    LogLists adapter = new LogLists(Logs.this, logList);
                    listViewLogs.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String currentDateTime(){
        String datetime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        return datetime;
    }

    public void back(View view){
        finish();
        CustomIntent.customType(Logs.this, "fadein-to-fadeout");
    }


}
