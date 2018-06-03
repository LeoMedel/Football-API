package com.example.mkmkmk.footballapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    TextView information;
    ProgressBar progressPlayers;

    JSONObject jsonPlayers;
    PlayerAdapter adapter;

    ListView listViewPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        teamName = getIntent().getStringExtra("team");
        urlPlayers = getIntent().getStringExtra("urlPlayers");

        information = (TextView) findViewById(R.id.txtInfo);
        progressPlayers = (ProgressBar) findViewById(R.id.progressBarPlayers);
        listViewPlayers = (ListView) findViewById(R.id.ListPlayers);

        information.setText(teamName+"\r\n "+urlPlayers);

        new downloadPlayers().execute(urlPlayers);
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
                String positionPlayer = teamsLigue.getJSONObject(i).getString("position");
                String jerseyNumber = teamsLigue.getJSONObject(i).getString("jerseyNumber");
                String birthday = teamsLigue.getJSONObject(i).getString("dateOfBirth");
                String nationality = teamsLigue.getJSONObject(i).getString("nationality");
                String contract = teamsLigue.getJSONObject(i).getString("contractUntil");

                playerList.add(new Player(namePlayer, positionPlayer, jerseyNumber+"", birthday, nationality, contract));

            }

            adapter = new PlayerAdapter(TeamInfoActivity.this, playerList);

            listViewPlayers.setAdapter(adapter);


            listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(TeamInfoActivity.this, ""+playerList.get(i).getPlayerName(), Toast.LENGTH_SHORT).show();

                    StringBuffer infoPlayer = new StringBuffer();

                    infoPlayer.append(playerList.get(i).getPlayerName()+" ("+playerList.get(i).getJerseyNumber()+")\n\r ");
                    infoPlayer.append(playerList.get(i).getNationality()+"\n\r");
                    infoPlayer.append("Position =>  "+playerList.get(i).getPositionPlayer()+"\n\r");
                    infoPlayer.append("BithDay : "+playerList.get(i).getBirthday()+"\n\r");
                    infoPlayer.append("Nationality : "+playerList.get(i).getNationality()+"\n\r");
                    infoPlayer.append("Contract : "+playerList.get(i).getContract()+"\n\r");



                information.setText(infoPlayer.toString());

                    //Intent info = new Intent(TeamInfoActivity.this, Player.class);

                    //info.putExtra("team", teamList.get(i).getName()+" ("+teamList.get(i).getShotName()+")");
                    //info.putExtra("urlPlayers", teamList.get(i).getUrlPlayers());

                    //startActivity(info);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("DEBUG", "Error TEAMS"+e.getMessage());
        }
    }
}
