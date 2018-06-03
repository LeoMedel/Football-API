package com.example.mkmkmk.footballapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkmkmk.footballapi.Model.League;

import java.util.ArrayList;
import java.util.List;

public class LeagueActivity extends AppCompatActivity {

    TextView info;

    private String nameLeague;
    private String urlTable;
    private String urlGames;
    private String urlTeams;

    CardView cTable;
    CardView cGames;
    CardView cEquipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);

        info = (TextView) findViewById(R.id.txtLigue);

        nameLeague = getIntent().getStringExtra("league");
        urlTable = getIntent().getStringExtra("urlTable");
        urlGames = getIntent().getStringExtra("urlGames");
        urlTeams = getIntent().getStringExtra("urlTeams");

        cTable = (CardView) findViewById(R.id.cardTable);
        cGames = (CardView) findViewById(R.id.cardGames);
        cEquipes = (CardView) findViewById(R.id.cardEquipes);

        info.setText(nameLeague);
    }



    public void setTable(View view)
    {
        if (nameLeague.equals("Champions League 2017/18 (CL)"))
        {
            Intent table = new Intent(getApplicationContext(), ChampionsTableActivity.class);
            table.putExtra("urlTable", urlTable);
            table.putExtra("league", nameLeague);
            startActivity(table);
        }
        else if (nameLeague.equals("DFB-Pokal 2017/18 (DFB)"))
        {
            Toast.makeText(this, "Table Indispnible", Toast.LENGTH_SHORT).show();
        }
        else if (nameLeague.equals("Australian A-League (AAL)"))
        {
            Toast.makeText(this, "Table Indispnible", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent table = new Intent(getApplicationContext(), TableActivity.class);
            table.putExtra("urlTable", urlTable);
            table.putExtra("league", nameLeague);
            startActivity(table);
        }


    }

    public void setGames(View view)
    {
        Intent games = new Intent(getApplicationContext(), GamesActivity.class);
        games.putExtra("urlGames", urlGames);
        games.putExtra("league", nameLeague);
        startActivity(games);
    }

    public void setTeams(View view)
    {
        Intent teams = new Intent(getApplicationContext(), TeamsActivity.class);
        teams.putExtra("urlTeams", urlTeams);
        teams.putExtra("league", nameLeague);
        startActivity(teams);
    }
}
