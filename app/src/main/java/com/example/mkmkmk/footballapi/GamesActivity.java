package com.example.mkmkmk.footballapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Adapter.GameAdapter;
import com.example.mkmkmk.footballapi.ConnexionAPI.HttpFootballAPI;
import com.example.mkmkmk.footballapi.Model.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GamesActivity extends AppCompatActivity {

    private String TAG = GamesActivity.class.getSimpleName();

    String urlGames;
    String leagueName;
    TextView info;
    ProgressBar progresGames;

    GridView gridViewGames;
    GameAdapter adapter;

    JSONObject jsonGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifierConnexion();

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
        alert.setTitle("Connexion. Games");
        alert.setMessage("Impossible telecharge des matchs de la ligue. \r\n Verifier connexion d'Internet");

        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                verifierConnexion();

            }
        });
        return alert;

    }


    private void verifierConnexion()
    {
        if (! connexionInternet(this))
        {
            showAlert(this).show();
        }
        else
        {
            setContentView(R.layout.activity_games);

            urlGames = getIntent().getStringExtra("urlGames");
            leagueName = getIntent().getStringExtra("league");

            info = (TextView) findViewById(R.id.txtGames);
            progresGames = (ProgressBar) findViewById(R.id.progressBarGames);
            gridViewGames = (GridView) findViewById(R.id.gridGames);

            info.setText(leagueName+ "\r\n GAMES");

            new downloadGames().execute(urlGames);

        }
    }


    public class downloadGames extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresGames.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... value) {


            JSONObject jsonObj = null;

            // Making a request to url and getting response
            String url = (String) value[0];

            HttpFootballAPI footAPI = new HttpFootballAPI();


            String jsonStr = footAPI.connexionAPI(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            try
            {
                if (jsonStr != null)
                {
                    jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "Response from JSON GAMES : " + jsonObj.toString());
                }

            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                e.printStackTrace();
            }
            if(jsonObj != null)
            {
                jsonGames = jsonObj;
                return "Download Games Succesful !";
            }
            else {
                return "Error download Games !";
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            AddCardsGames();
            progresGames.setVisibility(View.GONE);
            gridViewGames.setVisibility(View.VISIBLE);
        }
    }

    private void AddCardsGames() {

        List<Game> gameList = new ArrayList<>();
        try {

            JSONArray games = jsonGames.getJSONArray("fixtures");

            for (int i = 0; i < games.length(); i++)
            {
                String status = games.getJSONObject(i).getString("status");
                String matchday = ""+games.getJSONObject(i).getInt("matchday");
                String homeTeam = games.getJSONObject(i).getString("homeTeamName");
                String awayTeam = games.getJSONObject(i).getString("awayTeamName");


                String goalsHome;
                String goalsAway;

                if (status.equals("TIMED"))
                {
                    goalsHome = "NOT ";
                    goalsAway = " DISPONIBLE";
                }
                else
                {
                    goalsHome = ""+games.getJSONObject(i).getJSONObject("result").getInt("goalsHomeTeam");
                    goalsAway =""+games.getJSONObject(i).getJSONObject("result").getInt("goalsAwayTeam");
                }

                gameList.add(new Game(status, matchday, homeTeam, awayTeam, goalsHome, goalsAway));
            }

            adapter = new GameAdapter(gameList, GamesActivity.this);

            gridViewGames.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
