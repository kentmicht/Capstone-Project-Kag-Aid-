package com.example.kagaid.kagaid.Patient;

public class Patient {
    String pid;
    String fullname;
    String birthday;
    String gender;
    String address;
    //String lastscan;

    public Patient(){

    }

    public Patient(String pid, String fullname, String birthday, String gender, String address) {
        this.pid = pid;
        this.fullname = fullname;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
    }

    public Patient(String pid, String fullname, String birthday, String gender, String address, String lastscan) {
        this.pid = pid;
        this.fullname = fullname;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        //this.lastscan = lastscan;
    }



    public String getId() {
        return pid;
    }

    public String getFullname() {
        return fullname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

//    public String getLastScan() {
//        return lastscan;
//    }

    public void setId(String pid) { this.pid = pid; }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public void setLastScan(String lastscan) {
//        this.lastscan = lastscan;
//    }
}
