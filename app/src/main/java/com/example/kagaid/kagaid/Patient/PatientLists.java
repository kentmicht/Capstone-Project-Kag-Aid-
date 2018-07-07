package com.example.kagaid.kagaid.Patient;

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

public class PatientLists extends ArrayAdapter<Patient> {

    private Activity context;
    private List<Patient> patientList;

    public PatientLists(Activity context, List patientList){

        super(context, R.layout.activity_patient_list_layout, patientList);
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_patient_list_layout, null, true);
        TextView pname = (TextView) listViewItem.findViewById(R.id.textview_patient_name);

        Patient patient = patientList.get(position);

        pname.setText(patient.getFullname());

        return listViewItem;
    }
}
