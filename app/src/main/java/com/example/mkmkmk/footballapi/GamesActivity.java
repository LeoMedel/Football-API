package com.example.mkmkmk.footballapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GamesActivity extends AppCompatActivity {

    String urlGames;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        urlGames = getIntent().getStringExtra("urlGames");

        info = (TextView) findViewById(R.id.txtGames);
        info.setText(urlGames);
    }
}
