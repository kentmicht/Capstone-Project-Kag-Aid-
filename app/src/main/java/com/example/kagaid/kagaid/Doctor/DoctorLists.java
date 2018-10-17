package com.example.kagaid.kagaid.Doctor;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kagaid.kagaid.Logs.Log;
import com.example.kagaid.kagaid.Patient.Patient;
import com.example.kagaid.kagaid.R;
import com.example.kagaid.kagaid.User;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class DoctorLists extends ArrayAdapter<Doctor> {

    private Activity context;
    private List<Doctor> doctorList;
    Doctor doctor = new Doctor();


    public DoctorLists(Activity context, List doctorList){
        super(context, R.layout.nearby_doctors_layout, doctorList);
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        final View listViewItem = inflater.inflate(R.layout.nearby_doctors_layout, null, true);
        final TextView textViewName = (TextView) listViewItem.findViewById(R.id.doctorName);
        final TextView textViewContact = (TextView) listViewItem.findViewById(R.id.doctorNumber);
        doctor = doctorList.get(position);

        textViewName.setText(doctor.getName());
        textViewContact.setText(doctor.getContact());

        return listViewItem;

    }
}
