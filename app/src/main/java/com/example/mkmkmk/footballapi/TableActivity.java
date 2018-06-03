package com.example.mkmkmk.footballapi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Adapter.TableAdapter;
import com.example.mkmkmk.footballapi.ConnexionAPI.HttpFootballAPI;
import com.example.mkmkmk.footballapi.Model.ChampionsTable;
import com.example.mkmkmk.footballapi.Model.TeamPosition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private String TAG = TableActivity.class.getSimpleName();

    String urlTable;
    String leagueName;
    TextView info;
    ProgressBar progressBarTable;
    CardView cTable;

    TableAdapter tableAdapter;
    ListView itemTable;
    JSONObject jsonTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        urlTable = getIntent().getStringExtra("urlTable");
        leagueName = getIntent().getStringExtra("league");

        info = (TextView) findViewById(R.id.txtDescription);
        progressBarTable = (ProgressBar) findViewById(R.id.progressTable);
        itemTable = (ListView) findViewById(R.id.itemsTable);
        cTable = (CardView) findViewById(R.id.cardTable);

        info.setText(leagueName);
        new downloadTable().execute(urlTable);

    }




    public class downloadTable extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarTable.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... values) {


            JSONObject jsonObj = null;
            String url = (String) values[0];

            HttpFootballAPI footAPI = new HttpFootballAPI();

            String jsonStr = footAPI.connexionAPI(url);
            try
            {
                if (jsonStr != null)
                {
                    jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "Response from JSON TABLE : " + jsonObj.toString());
                }

            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                e.printStackTrace();
            }
            if(jsonObj != null)
            {
                jsonTable = jsonObj;
                return "Download Table Succesful !";
            }
            else {
                return "Error download Table !";
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            addPositionItems();
            progressBarTable.setVisibility(View.GONE);
            cTable.setVisibility(View.VISIBLE);
        }
    }

    private void addPositionItems() {

        List<TeamPosition> teamPositions = new ArrayList<>();

        JSONArray arrayTable = null;
        try {

            arrayTable = jsonTable.getJSONArray("standing");

            for (int i = 0; i < arrayTable.length(); i++) {
                String team = arrayTable.getJSONObject(i).getString("teamName");
                int position = arrayTable.getJSONObject(i).getInt("position");
                int games = arrayTable.getJSONObject(i).getInt("playedGames");
                int points = arrayTable.getJSONObject(i).getInt("points");
                int goals = arrayTable.getJSONObject(i).getInt("goals");
                int gamesWins = arrayTable.getJSONObject(i).getInt("wins");
                int gamesDraws = arrayTable.getJSONObject(i).getInt("draws");
                int gamesLosses = arrayTable.getJSONObject(i).getInt("losses");

                teamPositions.add(new TeamPosition(team, position, games, points, goals, gamesWins, gamesDraws, gamesLosses));

            }
            tableAdapter = new TableAdapter(teamPositions, TableActivity.this);

            itemTable.setAdapter(tableAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
