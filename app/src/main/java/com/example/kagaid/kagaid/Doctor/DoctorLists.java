package com.example.kagaid.kagaid.Doctor;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kagaid.kagaid.R;

import java.util.List;

public class DoctorLists extends ArrayAdapter<Doctor> {

    private Activity context;
    private List<Doctor> drList;

    public DoctorLists(Activity context, List<Doctor> drList){

        super(context, R.layout.activity_doctor_list_layout, drList);
        this.context = context;
        this.drList = drList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_doctor_list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView_doctor_name);
        TextView textViewContact = (TextView) listViewItem.findViewById(R.id.textView_doctor_contact);
        TextView textViewLocation = (TextView) listViewItem.findViewById(R.id.textView_doctor_location);

        Doctor doctor = drList.get(position);

        textViewName.setText(doctor.getFullname());
        textViewLocation.setText(doctor.getLocation());
        textViewContact.setText(doctor.getContact_details());

        return listViewItem;
    }
}
