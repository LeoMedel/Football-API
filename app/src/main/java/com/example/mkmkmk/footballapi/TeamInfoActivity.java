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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkmkmk.footballapi.Adapter.PlayerAdapter;
import com.example.mkmkmk.footballapi.Adapter.TeamAdapter;
import com.example.mkmkmk.footballapi.ConnexionAPI.HttpFootballAPI;
import com.example.mkmkmk.footballapi.Model.Player;
import com.example.mkmkmk.footballapi.Model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamInfoActivity extends AppCompatActivity {

    private String TAG = TeamInfoActivity.class.getSimpleName();

    String urlPlayers;
    String teamName;

    TextView information, player, position, jerseyN, birthDate, nation, contractDays;
    ProgressBar progressPlayers;

    JSONObject jsonPlayers;
    PlayerAdapter adapter;

    ListView listViewPlayers;


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
            setContentView(R.layout.activity_team_info);

            teamName = getIntent().getStringExtra("team");
            urlPlayers = getIntent().getStringExtra("urlPlayers");

            information = (TextView) findViewById(R.id.txtInfo);
            player = (TextView) findViewById(R.id.txtName);
            position = (TextView) findViewById(R.id.txtPosition);
            jerseyN = (TextView) findViewById(R.id.txtNumber);
            birthDate = (TextView) findViewById(R.id.txtBirthday);
            nation = (TextView) findViewById(R.id.txtNationality);
            contractDays = (TextView) findViewById(R.id.txtContract);

            progressPlayers = (ProgressBar) findViewById(R.id.progressBarPlayers);
            listViewPlayers = (ListView) findViewById(R.id.ListPlayers);

            information.setText(teamName);

            player.setText("Choisissez un joueur");
            position.setText("'    '      '");
            jerseyN.setText("'     '     '");
            birthDate.setText("'     '     '");
            nation.setText("'     '     '");
            contractDays.setText("'     '     '");

            new downloadPlayers().execute(urlPlayers);
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
        alert.setTitle("Connexion. Teams Information");
        alert.setMessage("Imposible telecharge information de l'equipe. \r\n Verifier connexion d'Internet");

        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                verifierConnexion();

            }
        });
        return alert;

    }

    public void setUbicationTeam(View view)
    {
        Intent teams = new Intent(getApplicationContext(), MapsActivity.class);
        teams.putExtra("team", teamName);
        startActivity(teams);
    }



    public class downloadPlayers extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressPlayers.setVisibility(View.VISIBLE);
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
                jsonPlayers = jsonObj;

                return "Succesful !";
            }
            else {
                return "Error download !";
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            addListPlayers();
            progressPlayers.setVisibility(View.GONE);
        }
    }

    private void addListPlayers() {


        JSONArray teamsLigue = null;
        final List<Player> playerList = new ArrayList<>();

        try {

            teamsLigue = jsonPlayers.getJSONArray("players");

            for (int i = 0; i < teamsLigue.length(); i++)
            {
                Log.i("DEBUG EQUIPE", "Creation des equipes");

                String namePlayer = teamsLigue.getJSONObject(i).getString("name");
                Log.i("DEBUG PLAYERS", "Name "+namePlayer);
                String positionPlayer = teamsLigue.getJSONObject(i).getString("position");
                Log.i("DEBUG PLAYERS", "Position "+positionPlayer);
                String jerseyNumber = teamsLigue.getJSONObject(i).getString("jerseyNumber");
                Log.i("DEBUG PLAYERS", "Jesrsey "+jerseyNumber);
                String birthday = teamsLigue.getJSONObject(i).getString("dateOfBirth");
                Log.i("DEBUG PLAYERS", "birthday "+birthday);
                String nationality = teamsLigue.getJSONObject(i).getString("nationality");
                Log.i("DEBUG PLAYERS", "Nationality "+nationality);
                String contract = teamsLigue.getJSONObject(i).getString("contractUntil");
                Log.i("DEBUG PLAYERS", "Contract "+contract);

                playerList.add(new Player(namePlayer, positionPlayer, jerseyNumber+"", birthday, nationality, contract));

            }

            adapter = new PlayerAdapter(TeamInfoActivity.this, playerList);
            listViewPlayers.setAdapter(adapter);


            listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(TeamInfoActivity.this, ""+playerList.get(i).getPlayerName(), Toast.LENGTH_SHORT).show();

                    StringBuffer infoPlayer = new StringBuffer();

                    infoPlayer.append(playerList.get(i).getPlayerName()+" ("+playerList.get(i).getJerseyNumber()+")\n\r ");
                    infoPlayer.append(playerList.get(i).getNationality()+"\n\r");
                    infoPlayer.append("Position =>  "+playerList.get(i).getPositionPlayer()+"\n\r");
                    infoPlayer.append("BithDay : "+playerList.get(i).getBirthday()+"\n\r");
                    infoPlayer.append("Nationality : "+playerList.get(i).getNationality()+"\n\r");
                    infoPlayer.append("Contract : "+playerList.get(i).getContract()+"\n\r");

                    player.setText(playerList.get(i).getPlayerName().toString());
                    position.setText(playerList.get(i).getPositionPlayer().toString());
                    jerseyN.setText(playerList.get(i).getJerseyNumber().toString());
                    birthDate.setText(playerList.get(i).getBirthday().toString());
                    nation.setText(playerList.get(i).getNationality().toString());
                    contractDays.setText(playerList.get(i).getContract().toString());
                //information.setText(infoPlayer.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("DEBUG", "Error TEAMS"+e.getMessage());
        }
    }
}
