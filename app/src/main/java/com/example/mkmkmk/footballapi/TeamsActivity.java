package com.example.mkmkmk.footballapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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

    TextView nameTeam;
    ProgressBar progressTeams;
    ListView listViewTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifierConnexion();

    }


    private void verifierConnexion()
    {
        if (! connexionInternet(this))
        {
            showAlert(this).show();
        }
        else
        {
            setContentView(R.layout.activity_teams);

            urlTeams = getIntent().getStringExtra("urlTeams");
            teamName = getIntent().getStringExtra("league");

            nameTeam = (TextView) findViewById(R.id.txtTeamName);
            progressTeams = (ProgressBar) findViewById(R.id.progressBarTeams);
            listViewTeams = (ListView) findViewById(R.id.ListTeams);

            nameTeam.setText(teamName);
            new downloadTeams().execute(urlTeams);
        }
    }

    public boolean connexionInternet(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            NetworkInfo wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mobile != null && mobile.isConnectedOrConnecting() || (wifi != null && wifi.isConnectedOrConnecting()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public AlertDialog.Builder showAlert(Context context)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Connexion. Equipes");
        alert.setMessage("Imposible telecharge des Equipes. \r\n Verifier la connexion d'Internet");

        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                verifierConnexion();

            }
        });
        return alert;

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
            listViewTeams.setVisibility(View.VISIBLE);

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

                    Intent info = new Intent(TeamsActivity.this, TeamInfoActivity.class);

                    //info.putExtra("team", teamList.get(i).getName()+" ("+teamList.get(i).getShotName()+")");
                    info.putExtra("team", teamList.get(i).getName());
                    info.putExtra("urlPlayers", teamList.get(i).getUrlPlayers());

                    startActivity(info);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("DEBUG", "Error TEAMS"+e.getMessage());
        }
    }
}
