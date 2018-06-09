package com.example.mkmkmk.footballapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
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
        alert.setTitle("Connexion. Table generale");
        alert.setMessage("Impossible telecharge Table de la Ligue. \r\n Verifier connexion d'Internet");

        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                verifierConnexion();

            }
        });
        return alert;

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
