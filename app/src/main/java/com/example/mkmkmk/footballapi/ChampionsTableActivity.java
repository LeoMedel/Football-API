package com.example.mkmkmk.footballapi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Adapter.ChampionsTableAdapter;
import com.example.mkmkmk.footballapi.ConnexionAPI.HttpFootballAPI;
import com.example.mkmkmk.footballapi.Model.ChampionsTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChampionsTableActivity extends AppCompatActivity {

    private String TAG = ChampionsTableActivity.class.getSimpleName();

    String urlTableChampions;
    String championsName;
    ProgressBar progressBarChampions;
    TextView title;

    ChampionsTableAdapter championsTableAdapter;
    GridView gridChampionsGroup;

    JSONObject jsonTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champions_table);

        urlTableChampions = getIntent().getStringExtra("urlTable");
        championsName = getIntent().getStringExtra("league");

        title = (TextView) findViewById(R.id.txtChampions);
        progressBarChampions = (ProgressBar) findViewById(R.id.progressChampions);
        gridChampionsGroup = (GridView) findViewById(R.id.gridChampions);

        title.setText(championsName);

        new downloadTable().execute(urlTableChampions);
    }

    public class downloadTable extends AsyncTask<String, String, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarChampions.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... value) {


            JSONObject jsonObj = null;
            String url = (String) value[0];

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
                return "Download Champions Table Succesful !";
            }
            else {
                return "Error download Champions Table !";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            addGroupsChampions();
            progressBarChampions.setVisibility(View.GONE);
        }
    }

    private void addGroupsChampions() {

        if (championsName.equals("Champions League 2017/18 (CL)")) {

            List<ChampionsTable> championsTeams = new ArrayList<>();
            String[] groupes = {"A", "B", "C", "D", "E", "F", "G", "H"};
            JSONObject arrayTableChampions = null;
            try {
                arrayTableChampions = jsonTable.getJSONObject("standings");

                String[] arrayTeam = new String[4];
                String[] arrayUrls = new String[4];

                for (int i = 0; i < 8; i++)
                {
                    String [] teamsArray = new String[4];
                    String [] urlsImages = new String[4];

                    teamsArray[3] = "";
                    urlsImages[3] = "";

                    JSONArray groupe = arrayTableChampions.getJSONArray(groupes[i]);
                    String group = "";
                    for (int e =0; e < groupe.length(); e++) {

                        JSONObject team = groupe.getJSONObject(e);

                        String name = team.getString("team");
                        group = team.getString("group");
                        String urlImg = team.getString("crestURI");

                        teamsArray[e] = name;
                        urlsImages[e] = urlImg;
                        arrayTeam = teamsArray;
                        arrayUrls = urlsImages;
                    }
                    championsTeams.add(new ChampionsTable(group, arrayTeam, arrayUrls));

                }

                championsTableAdapter = new ChampionsTableAdapter(championsTeams, ChampionsTableActivity.this);

                gridChampionsGroup.setAdapter(championsTableAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
