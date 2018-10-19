package com.example.kagaid.kagaid.Patient;

public class Patient {
    String pid;
    String fullname;
    String birthday;
    String age;
    String gender;
    String address;
    String lastscan;
    String status;
    String bId;


    public Patient(String pid, String fullname, String birthday, String age, String gender, String address, String lastscan, String status, String bId) {
        this.pid = pid;
        this.fullname = fullname;
        this.birthday = birthday;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.lastscan = lastscan;
        this.status = status;
        this.bId = bId;
    }

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
        this.lastscan = lastscan;
    }

    public Patient(String pid, String fullname, String birthday, String gender, String address, String lastscan, String status) {
        this.pid = pid;
        this.fullname = fullname;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.lastscan = lastscan;
        this.status = status;
    }

    public Patient(String pid, String fullname, String birthday, String gender, String address, String lastscan, String status, String age) {
        this.pid = pid;
        this.fullname = fullname;
        this.birthday = birthday;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.lastscan = lastscan;
        this.status = status;
    }

    public Patient(String lastscan) {
        this.lastscan = lastscan;
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

    public String getLastscan() {
        return lastscan;
    }

    public void setLastscan(String lastscan) {
        this.lastscan = lastscan;
    }

    public String getPid() {
        return pid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }
}
