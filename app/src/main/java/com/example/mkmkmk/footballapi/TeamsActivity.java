package com.example.mkmkmk.footballapi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkmkmk.footballapi.Adapter.TeamAdapter;
import com.example.mkmkmk.footballapi.ConnexionAPI.HttpFootballAPI;
import com.example.mkmkmk.footballapi.Model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    private String TAG = TeamsActivity.class.getSimpleName();

    String urlTeams;
    String teamName;

    JSONObject jsonTeams;

    TeamAdapter adapter;

    TextView info;
    TextView nameTeam;
    ProgressBar progressTeams;
    ListView listViewTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        urlTeams = getIntent().getStringExtra("urlTeams");
        teamName = getIntent().getStringExtra("league");

        info = (TextView) findViewById(R.id.txtTeams);
        nameTeam = (TextView) findViewById(R.id.txtTeamName);
        progressTeams = (ProgressBar) findViewById(R.id.progressBarTeams);
        listViewTeams = (ListView) findViewById(R.id.ListTeams);

        info.setText(urlTeams);
        nameTeam.setText(teamName);
        new downloadTeams().execute(urlTeams);
    }


    public class downloadTeams extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressTeams.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... value) {

            JSONObject jsonObj = null;

            // Making a request to url and getting response
            String url = (String) value[0];
            HttpFootballAPI footballAPI = new HttpFootballAPI();


            Log.i("DEBUG TEAMS", "Conection URL"+url);
            String jsonStr = footballAPI.connexionAPI(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            try
            {
                if (jsonStr != null)
                {
                    jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "Response from JSON TEAMS : " + jsonObj.toString());
                }

            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                e.printStackTrace();
            }
            if(jsonObj != null)
            {
                jsonTeams = jsonObj;

                return "Succesful !";
            }
            else {
                return "Error download !";
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            addcardsTeams();
            progressTeams.setVisibility(View.GONE);

        }
    }

    private void addcardsTeams() {

        JSONArray teamsLigue = null;
        final List<Team> teamList = new ArrayList<>();

        try {
            teamsLigue = jsonTeams.getJSONArray("teams");

            Log.i("DEBUG TEAMS", "SetTeams "+teamsLigue.toString());
            //StringBuffer teams = new StringBuffer();
            //teams.append("Equipes \r\n \r\n");
            Log.i("DEBUG EQUIPE", "BOUCLE DES EQUIPES ");
            for (int i = 0; i < teamsLigue.length(); i++)
            {
                Log.i("DEBUG EQUIPE", "Creation des equipes");

                String name = teamsLigue.getJSONObject(i).getString("name");
                Log.i("DEBUG EQUIPE", "NAME : "+name);

                String courtNom = teamsLigue.getJSONObject(i).getString("code");
                Log.i("DEBUG EQUIPE", "CODE : "+courtNom);

                String urlImg = teamsLigue.getJSONObject(i).getString("crestUrl");
                Log.i("DEBUG EQUIPE", "IMG : "+urlImg);

                String urlPlayers = teamsLigue.getJSONObject(i).getJSONObject("_links").getJSONObject("players").getString("href");
                Log.i("DEBUG EQUIPE", "PLAYERS : "+urlPlayers);

                String urlGames = teamsLigue.getJSONObject(i).getJSONObject("_links").getJSONObject("fixtures").getString("href");

                if (courtNom.equals("null") || courtNom ==null)
                {
                    courtNom =teamsLigue.getJSONObject(i).getString("shortName");
                }


                if(courtNom.equals("null") || courtNom ==null)
                {
                    courtNom = name;
                }

                teamList.add(new Team(name, courtNom, urlImg, urlPlayers, urlGames));
            }

            adapter = new TeamAdapter(TeamsActivity.this, teamList);

            listViewTeams.setAdapter(adapter);


            listViewTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(TeamsActivity.this, ""+teamList.get(i).getName(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("DEBUG", "Error TEAMS"+e.getMessage());
        }
    }
}
