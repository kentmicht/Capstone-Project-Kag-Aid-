package com.example.kagaid.kagaid.Logs;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class Log {
    String logId;
    String logdatetime;
    String pId;
    String uId;

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

    public Log(String logId, String logdatetime, String pId, String uId) {
        this.logId = logId;
        this.logdatetime = logdatetime;
        this.pId = pId;
        this.uId = uId;
    }

    public Log() {
    }
}
