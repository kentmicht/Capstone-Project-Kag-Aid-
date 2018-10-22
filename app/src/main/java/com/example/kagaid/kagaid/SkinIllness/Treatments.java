package com.example.kagaid.kagaid.SkinIllness;

public class Treatments {
    private String medicine_name;
    private String brand;
    private String dosage;
    private String id;
    private String skin_illness_id;



    public Treatments() {
    }

    public Treatments(String medicine_name, String brand, String dosage) {
        this.medicine_name = medicine_name;
        this.brand = brand;
        this.dosage = dosage;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkin_illness_id() {
        return skin_illness_id;
    }

    public void setSkin_illness_id(String skin_illness_id) {
        this.skin_illness_id = skin_illness_id;
    }
}
