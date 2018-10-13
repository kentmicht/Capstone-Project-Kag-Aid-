package com.example.kagaid.kagaid.Diagnosis;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class DiagnosisQuestions {
    String q1;
    String q2;
    String q3;

    public String getQ1() {
        return q1;
    }

    public void setQ1(String q1) {
        this.q1 = q1;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public String getQ3() {
        return q3;
    }

    public void setQ3(String q3) {
        this.q3 = q3;
    }

    public DiagnosisQuestions(String q1, String q2, String q3) {
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public DiagnosisQuestions() {
    }
}
