package com.example.kagaid.kagaid.Diagnosis;

/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
public class DiagnosisQuestionAnswers {
    String dqaId;
    String answer1;
    String answer2;
    String answer3;
    String pId;
    String uId;

    public DiagnosisQuestionAnswers(String dqaId, String answer1, String answer2, String answer3, String pId, String uId) {
        this.dqaId = dqaId;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.pId = pId;
        this.uId = uId;
    }

    public DiagnosisQuestionAnswers(String answer1, String answer2, String answer3, String pId, String uId) {
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = this.answer3;
        this.pId = pId;
        this.uId = uId;
    }

    public DiagnosisQuestionAnswers() {
    }

    public String getDqaId() {
        return dqaId;
    }

    public void setDqaId(String dqaId) {
        this.dqaId = dqaId;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
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
}
