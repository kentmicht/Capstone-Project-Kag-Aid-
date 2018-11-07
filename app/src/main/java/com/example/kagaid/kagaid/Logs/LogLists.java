package com.example.kagaid.kagaid.Logs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Patient.Patient;
import com.example.kagaid.kagaid.R;
import com.example.kagaid.kagaid.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class LogLists extends ArrayAdapter<Log>{

    private Activity context;
    private List<Log> logList;
    DatabaseReference refUser;
    DatabaseReference refPatient;
    User u = new User();
    Patient p = new Patient();
    Patient logP = new Patient();
    Log log = new Log();
    Log logPatient = new Log();

    String patientName;


    public LogLists(Activity context, List logList){
        super(context, R.layout.activity_log_list_layout, logList);
        this.context = context;
        this.logList = logList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        final View listViewItem = inflater.inflate(R.layout.activity_log_list_layout, null, true);
        final TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView_log_name);
        final TextView textViewEmployee = (TextView) listViewItem.findViewById(R.id.textView_log_employee);
        final TextView textViewPatient = (TextView) listViewItem.findViewById(R.id.textView_log_patient);
        final TextView textViewDateTime = (TextView) listViewItem.findViewById(R.id.textView_log_datetime);


        log = logList.get(position);
        logPatient = log;
        refPatient = FirebaseDatabase.getInstance().getReference("person_information");
        refPatient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot patientSnapshot: dataSnapshot.getChildren()){
                    p = patientSnapshot.getValue(Patient.class);
//                    System.out.println(logPatient.getpId());
                    if(p.getPid().equals(logPatient.getpId())){
                        patientName = p.getFullname();
//                        System.out.println("Found it");
                    }

                }

                textViewPatient.setText("Patient: " + patientName);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textViewEmployee.setText("Employee: " + log.getEmployeeName());
        textViewName.setText( log.getLogdatetime());
        textViewDateTime.setText("Date and Time: " + log.getLogdatetime());


        return listViewItem;

    }
}
