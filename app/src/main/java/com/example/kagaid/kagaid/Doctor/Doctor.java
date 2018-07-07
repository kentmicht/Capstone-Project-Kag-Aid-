package com.example.kagaid.kagaid.Doctor;

public class Doctor {
    String id;
    String fullname;
    String contact_details;
    String schedule;
    String location;

    public Doctor() {
    }

    public Doctor(String id, String fullname, String contact_details, String schedule, String location) {
        this.id = id;
        this.fullname = fullname;
        this.contact_details = contact_details;
        this.schedule = schedule;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getContact_details() {
        return contact_details;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getLocation() {
        return location;
    }
}
