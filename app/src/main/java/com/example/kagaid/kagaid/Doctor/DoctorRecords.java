package com.example.kagaid.kagaid.Doctor;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DoctorRecords extends AppCompatActivity {

    DatabaseReference db;

    ListView doctor_record;
    List<Doctor> drList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_records);
        toastMessage("Long press to view more information");
        db = FirebaseDatabase.getInstance().getReference("doctor");

        doctor_record = (ListView) findViewById(R.id.listViewDoctors);
        drList = new ArrayList<>();

        doctor_record.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Doctor doctor = drList.get(position);

                showDoctorInfoDialog(doctor.getId(), doctor.getFullname(), doctor.getContact_details(), doctor.getSchedule(), doctor.getLocation());

                return false;
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void showDoctorInfoDialog(String id, String fullname, String contact, String sched, String location)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.activity_view_doctor_info_dialog, null);

        dialogBuilder.setView(dialogView);

        final TextView textViewName = (TextView) dialogView.findViewById(R.id.doctor_name);
        final TextView textViewLocation = (TextView) dialogView.findViewById(R.id.doctor_location);
        final TextView textViewContact = (TextView) dialogView.findViewById(R.id.doctor_contact);
        final TextView textViewSchedule = (TextView) dialogView.findViewById(R.id.doctor_schedule);

        dialogBuilder.setTitle("Doctor Information for: ");

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        textViewName.setText(fullname);
        textViewContact.setText(contact);
        textViewSchedule.setText(sched);
        textViewLocation.setText(location);
    }

    @Override
    protected void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                drList.clear();

                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    Doctor doctor = dSnapshot.getValue(Doctor.class);

                    drList.add(doctor);

                }
                DoctorLists adapter = new DoctorLists(DoctorRecords.this, drList);
                doctor_record.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
