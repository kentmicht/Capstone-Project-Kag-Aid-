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
    String skinIllness;
    String percentage;
    String bId;

    public Log(String logId, String logdatetime, String pId, String uId, String patientName, String employeeName, String skinIllness, String percentage, String bId) {
        this.logId = logId;
        this.logdatetime = logdatetime;
        this.pId = pId;
        this.uId = uId;
        this.patientName = patientName;
        this.employeeName = employeeName;
        this.skinIllness = skinIllness;
        this.percentage = percentage;
        this.bId = bId;
    }

    public Log(String logId, String logdatetime, String pId, String uId, String patientName, String employeeName, String skinIllness, String percentage) {
        this.logId = logId;
        this.logdatetime = logdatetime;
        this.pId = pId;
        this.uId = uId;
        this.patientName = patientName;
        this.employeeName = employeeName;
        this.skinIllness = skinIllness;
        this.percentage = percentage;
    }

    public String getSkinIllness() {
        return skinIllness;
    }

    public void setSkinIllness(String skinIllness) {
        this.skinIllness = skinIllness;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

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

    public Log() {
    }

    public static void e(String rest_response, String s) {
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }
}
