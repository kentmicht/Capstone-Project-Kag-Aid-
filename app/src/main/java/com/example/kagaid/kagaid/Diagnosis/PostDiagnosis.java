package com.example.kagaid.kagaid.Diagnosis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Homepage;
import com.example.kagaid.kagaid.LogIn;
import com.example.kagaid.kagaid.Patient.ViewPatientInfo;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDiagnosis extends AppCompatActivity {
    String uId;
    String pId;
    String skinIllness;
    String percentage;

    TextView question1;
    TextView question2;
    TextView question3;

    TextView identifiedIllness;
    TextView identifiedPercent;

    EditText ans1;
    EditText ans2;
    EditText ans3;

    Button submit;

    DatabaseReference databaseQuestions;
    DatabaseReference databaseQuestionAnswers;

    private static int TIME_OUT = 1000; //Time to launch the another activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_diagnosis);

        uId = (String) getIntent().getStringExtra("USER_ID");
        pId = (String) getIntent().getStringExtra("PATIENT_ID");
        skinIllness = (String) getIntent().getStringExtra("SKIN_ILLNESS");
        percentage = (String) getIntent().getStringExtra("PERCENTAGE");

        //for the questions
        question1 = (TextView) findViewById(R.id.question1);
        question2 = (TextView) findViewById(R.id.question2);
        question3 = (TextView) findViewById(R.id.question3);

        //for the skinIllness
        identifiedIllness = (TextView) findViewById(R.id.skinIllness);
        identifiedPercent = (TextView) findViewById(R.id.percentage);

        //submit answers
        submit = (Button) findViewById(R.id.submitAnswers);

        //answers
        ans1 = (EditText) findViewById(R.id.ans1);
        ans2 = (EditText) findViewById(R.id.ans2);
        ans3 = (EditText) findViewById(R.id.ans3);

        addQuestionsToPage();
        addResults();

        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ans1.getText().toString().matches("") || ans2.getText().toString().matches("") || ans3.getText().toString().matches("")) {
                    toastMessage("Please don't leave any question/s unanswered");
                } else if (!ans1.getText().toString().matches("") && !ans2.getText().toString().matches("") && !ans3.getText().toString().matches("")) {
                    addAnswers();
                    progressDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toastMessage("Diagnosis has been recorded");
                            finish();
                        }
                    }, TIME_OUT);
                }
            }
        });

        toastMessage("UserId: " + uId + "PatientId: " + pId + "Skin Illness: " + skinIllness + "Percentage: " + percentage);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void back(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    public void addQuestionsToPage(){
        //log all details percentage and skin illness identified most especially
        final DiagnosisQuestions[] dq = new DiagnosisQuestions[1];
        databaseQuestions = FirebaseDatabase.getInstance().getReference("diagnosis_questions");
        databaseQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot questionSnapshot: dataSnapshot.getChildren()){
                    dq[0] = questionSnapshot.getValue(DiagnosisQuestions.class);
                }
                question1.setText(dq[0].getQ1());
                question2.setText(dq[0].getQ2());
                question3.setText(dq[0].getQ3());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addResults(){
        identifiedIllness.setText(skinIllness);
        identifiedPercent.setText("Percentage: " + percentage);
    }

    public void addAnswers(){
        databaseQuestionAnswers = FirebaseDatabase.getInstance().getReference("patient_answers");
        String dqaId = databaseQuestionAnswers.push().getKey();
        DiagnosisQuestionAnswers dqa = new DiagnosisQuestionAnswers(dqaId, ans1.getText().toString(), ans2.getText().toString(), ans3.getText().toString(), pId, uId);
        databaseQuestionAnswers.child(dqaId).setValue(dqa);
    }

    private void progressDialog(){
        ProgressDialog pd = new ProgressDialog(PostDiagnosis.this);
        pd.setMessage("Processing Patient's answers...");
        pd.setCancelable(false);
        pd.show();
    }


}
