package com.example.kagaid.kagaid.Doctor;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class Doctor {
    String contact;
    String location;
    String name;

    public Doctor(String contact, String location, String name) {
        this.contact = contact;
        this.location = location;
        this.name = name;
    }

    public Doctor() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
