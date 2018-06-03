package com.example.mkmkmk.footballapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TeamsActivity extends AppCompatActivity {

    String urlTeams;

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        urlTeams = getIntent().getStringExtra("urlTeams");

        info = (TextView) findViewById(R.id.txtTeams);

        info.setText(urlTeams);
    }
}
