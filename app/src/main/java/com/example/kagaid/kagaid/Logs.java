package com.example.kagaid.kagaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class Logs extends AppCompatActivity {
    // Initializing a new String Array
    String[] web = {
            "Log 1",
            "Log 2",
            "Log 3",
            "Log 4"
    } ;
    Integer[] imageId = {
            R.drawable.kag_aid_logo_raw,
            R.drawable.kag_aid_logo_raw,
            R.drawable.kag_aid_logo_raw,
            R.drawable.kag_aid_logo_raw

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        // Get reference of widgets from XML layout
        final ListView logsList = (ListView) findViewById(R.id.listViewLogs);

        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.asList(web));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        // DataBind ListView with items from ArrayAdapter
        logsList.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        openHomepage();

    }

    public void openHomepage(){
        Intent intent = new Intent(this, Homepage.class);
        //intent.putExtra("USERNAME", userName);
        finish();
        startActivity(intent);
        CustomIntent.customType(Logs.this, "fadein-to-fadeout");
    }
}
