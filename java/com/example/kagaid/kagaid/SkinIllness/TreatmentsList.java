package com.example.kagaid.kagaid.SkinIllness;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kagaid.kagaid.R;

import org.w3c.dom.Text;

import java.util.List;

public class TreatmentsList extends ArrayAdapter<Treatments>{
    private Activity context;
    private List<Treatments> treatmentsList;

    public TreatmentsList(Activity context, List<Treatments> treatmentsList)
    {
        super(context, R.layout.treatment_info, treatmentsList);
        this.context = context;
        this.treatmentsList = treatmentsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.treatment_info, null, true);

        TextView textViewMedicineName = (TextView) listViewItem.findViewById(R.id.medicineName);
        TextView textViewDosage = (TextView) listViewItem.findViewById(R.id.medicineDosage);
        TextView textViewBrand = (TextView) listViewItem.findViewById(R.id.medicineBrand);

        Treatments treatments = treatmentsList.get(position);
        textViewMedicineName.setText(treatments.getMedicine_name());
        textViewDosage.setText("Dosage: " + treatments.getDosage());
        textViewBrand.setText("Brand: " + treatments.getBrand());

        return listViewItem;
    }
}
