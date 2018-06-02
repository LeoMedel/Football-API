package com.example.mkmkmk.footballapi.ConnexionAPI;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkmkmk.footballapi.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mkmkmk on 02/06/2018.
 */

public class GetInformationAPI {

    private String TAG = MainActivity.class.getSimpleName();
    String jsonResult = "";
    Context context;
    TextView info;

    public String jsonResult(String url, Context context, TextView text, JSONObject jjssoonn)
    {
        info = text;
        this.context = context;
        Toast.makeText(context.getApplicationContext(), "Start GetInfo ", Toast.LENGTH_SHORT).show();

        new getJson().execute(url);

        return jsonResult;

    }

    public class getJson extends AsyncTask<String, String, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... values) {

            JSONObject jsonObj = null;

            HttpFootballAPI sh = new HttpFootballAPI();

            // Making a request to url and getting response
            String url = (String) values[0];

            String jsonStr = sh.connexionAPI(url);

            Log.e(TAG, "Response from url: " + jsonStr);
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
                return jsonObj.toString();
            }
            else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(context.getApplicationContext(), "Finish download GetInfo "+result, Toast.LENGTH_SHORT).show();
            info.setText(result);
        }
    }
}
