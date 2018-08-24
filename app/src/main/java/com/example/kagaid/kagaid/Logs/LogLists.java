package com.example.kagaid.kagaid.Logs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
    DatabaseReference db;

    public LogLists(Activity context, List logList){
        super(context, R.layout.activity_log_list_layout, logList);
        this.context = context;
        this.logList = logList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String[] firstName = new String[1];
        final String[] lastName = new String[1];
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_log_list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView_log_name);
        TextView textViewEmployee = (TextView) listViewItem.findViewById(R.id.textView_log_employee);
        TextView textViewPatient = (TextView) listViewItem.findViewById(R.id.textView_log_patient);
        TextView textViewDateTime = (TextView) listViewItem.findViewById(R.id.textView_log_datetime);

        Log log = logList.get(position);

//        //Firebase Database
//        db = FirebaseDatabase.getInstance().getReference("users");
//
//        db.child(log.pId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                firstName[0] = dataSnapshot.getValue(User.class).getFirstname();
//                lastName[0] = dataSnapshot.getValue(User.class).getLastname();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        textViewName.setText(log.getLogId());
        textViewEmployee.setText("Employee: " + log.getuId());
        textViewPatient.setText("Patient: ");
        textViewDateTime.setText("Date and Time: " + log.getLogdatetime());

        return listViewItem;

    }
}
