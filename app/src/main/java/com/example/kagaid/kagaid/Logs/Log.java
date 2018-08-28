package com.example.kagaid.kagaid.Logs;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class Log {
    String logId;
    String logdatetime;
    String pId;
    String uId;
    String patientName;
    String employeeName;

    public String getLogdatetime() {
        return logdatetime;
    }

    public void setLogdatetime(String logdatetime) {
        this.logdatetime = logdatetime;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Log(String logId, String logdatetime, String pId, String uId, String patientName, String employeeName) {
        this.logId = logId;
        this.logdatetime = logdatetime;
        this.pId = pId;
        this.uId = uId;
        this.patientName = patientName;
        this.employeeName = employeeName;
    }

    public Log() {
    }
}