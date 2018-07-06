package com.example.kagaid.kagaid.SkinIllness;

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

public class SkinIllnessList extends ArrayAdapter<SkinIllness> {
    private Activity context;
    private List<SkinIllness> skinIllnessList;

    public SkinIllnessList(Activity context, List<SkinIllness> skinIllnessList)
    {
        super(context, R.layout.skin_illness_info, skinIllnessList);
        this.context = context;
        this.skinIllnessList = skinIllnessList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.skin_illness_info, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.skinIllnessName);
        TextView textViewSymptoms = (TextView) listViewItem.findViewById(R.id.skinIllnessSymptoms);

        SkinIllness skinIllness = skinIllnessList.get(position);
        textViewName.setText(skinIllness.getSkin_illness_name());
        textViewSymptoms.setText(skinIllness.getSkin_illness_symptoms());

        return listViewItem;
    }
}
