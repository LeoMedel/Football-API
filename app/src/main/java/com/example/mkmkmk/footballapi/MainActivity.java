package com.example.mkmkmk.footballapi;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkmkmk.footballapi.Adapter.LeagueAdapter;
import com.example.mkmkmk.footballapi.ConnexionAPI.HttpFootballAPI;
import com.example.mkmkmk.footballapi.Model.League;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    GridView listLeagues;
    ProgressBar progress;
    TextView description;
    LeagueAdapter leagueAdapter;

    JSONArray leaguesArray;

    private String URL_API = "http://api.football-data.org/v1/competitions/?season=2017";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listLeagues = (GridView) findViewById(R.id.leagueList);
        progress = (ProgressBar) findViewById(R.id.progressLeague);
        description = (TextView) findViewById(R.id.txtDescription);


        new downloadLeagues().execute(URL_API);

    }


    public class downloadLeagues extends AsyncTask<String, String, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... value) {

            JSONArray jsonArrayLeagues = null;

            //On ajoute l'url de l'API depuis le parametre
            String url = value[0];

            //on fait une Varibale de la Classe qui fait la connexion de telechargement de l'API REST
            HttpFootballAPI footAPI = new HttpFootballAPI();

            //On fait un variable qui contient l'information des Ligues
            String dataLeagues = footAPI.connexionAPI(url);

            //condition pour savoir s'il y a des information apres de l'API
            if (dataLeagues != null)
            {
                try {

                    //on change l'infomation en JSONArray pour l'implementation plus facile
                    jsonArrayLeagues = new JSONArray(dataLeagues);

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                    e.printStackTrace();
                    return "Download Failed";
                }
            }
            else
            {
                return "Download Failed";
            }
            //on ajoute toute la variable avec l'info dans la varibale leagueArray et l'utiliser apres
            leaguesArray = jsonArrayLeagues;


            return "Download Succesful";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            addCardsLeagues();

            listLeagues.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }

    /**
     * Methode qui va creer les CardVIew de chaque ligue
     * **/
    private void addCardsLeagues() {

        final List<League> leagueList = new ArrayList<>();


        try {
            //Boucle pour parcourir chaque Ligue dans le JSON des Ligues
            for (int i = 0; i < leaguesArray.length(); i++)
            {
                JSONObject leagues = null;

                leagues = leaguesArray.getJSONObject(i);

                JSONObject jsonLeague = (JSONObject) leaguesArray.getJSONObject(i).get("_links");

                String tableLeague = jsonLeague.getJSONObject("leagueTable").getString("href");
                String gamesLeague = jsonLeague.getJSONObject("fixtures").getString("href");
                String teamsLeague = jsonLeague.getJSONObject("teams").getString("href");
                String leagueName = leagues.getString("caption")+" ("+leagues.getString("league") + ")";
                String year = leagues.getString("year");
                int numberGames = leagues.getInt("numberOfGames");
                int numberTeams = leagues.getInt("numberOfTeams");




                leagueList.add(new League(tableLeague, gamesLeague, teamsLeague, leagueName, year, numberGames,numberTeams));

            }

            leagueAdapter = new LeagueAdapter(leagueList, MainActivity.this);

            listLeagues.setAdapter(leagueAdapter);

            listLeagues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (leagueList.get(i).getLeague().equals("Australian A-League (AAL)"))
                    {
                        Toast.makeText(MainActivity.this, "League Indispnible", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, ""+leagueList.get(i).getLeague(), Toast.LENGTH_SHORT).show();

                        Intent splash = new Intent(getApplicationContext(), LeagueActivity.class);

                        splash.putExtra("league", leagueList.get(i).getLeague());
                        splash.putExtra("urlTable", leagueList.get(i).getUrlTable());
                        splash.putExtra("urlGames", leagueList.get(i).getUrlAllGames());
                        splash.putExtra("urlTeams", leagueList.get(i).getUrlAllTeams());

                        startActivity(splash);
                    }


                }
            });

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
