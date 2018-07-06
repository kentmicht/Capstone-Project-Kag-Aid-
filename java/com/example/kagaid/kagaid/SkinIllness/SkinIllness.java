package com.example.kagaid.kagaid.SkinIllness;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SkinIllness {
    String skin_illness_name;
    String skin_illness_desc;
    String image;
    String skin_illness_symptoms;
    String level_of_severity;

    public SkinIllness() {
    }

    public SkinIllness(String skin_illness_name, String skin_illness_desc, String image, String skin_illness_symptoms, String level_of_severity) {
        this.skin_illness_name = skin_illness_name;
        this.skin_illness_desc = skin_illness_desc;
        this.image = image;
        this.skin_illness_symptoms = skin_illness_symptoms;
        this.level_of_severity = level_of_severity;
    }

    public String getSkin_illness_name() {
        return skin_illness_name;
    }

    public String getSkin_illness_desc() {
        return skin_illness_desc;
    }

    public String getImage() {
        return image;
    }

    public String getSkin_illness_symptoms() {
        return skin_illness_symptoms;
    }

    public String getLevel_of_severity() {
        return level_of_severity;
    }

    public void setLevel_of_severity(String level_of_severity) {
        this.level_of_severity = level_of_severity;
    }

    public void setSkin_illness_symptoms(String skin_illness_symptoms) {
        this.skin_illness_symptoms = skin_illness_symptoms;
    }

    public void setSkin_illness_name(String skin_illness_name) {
        this.skin_illness_name = skin_illness_name;
    }

    public void setSkin_illness_desc(String skin_illness_desc) {
        this.skin_illness_desc = skin_illness_desc;
    }

    public void setImage(String image) {
        this.image = image;

    }
}
