package com.example.kagaid.kagaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SkinIllness extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_illness);

        Button launchPage = (Button) findViewById(R.id.launchPage);
        launchPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SkinIllnessPage.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}
