package com.example.mkmkmk.footballapi.GetDataStadium;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mkmkmk on 06/06/2018.
 */


public class DataParser {

    public List<HashMap<String , String>> parserData(String dataJson)
    {
        JSONObject jsonObject;
        JSONArray jsonArray = null;
        String reponse="";

        try {
            jsonObject = new JSONObject(dataJson);

            reponse = jsonObject.getString("status");
            jsonArray = jsonObject.getJSONArray("results");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArray != null)
            return getStadeUbication(jsonArray, reponse);
        else
        {
            List<HashMap<String, String>> stadeTeam = new ArrayList<>();
            return stadeTeam;
        }
    }

    private List<HashMap<String,String>> getStadeUbication(JSONArray jsonArray, String status) {

        int count = jsonArray.length();

        List<HashMap<String, String>> stadeTeam = new ArrayList<>();

        HashMap<String, String> placeMap = null;


        for (int i = 0; i < count; i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i), status);
                stadeTeam.add(placeMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return stadeTeam;

    }

    private HashMap<String, String> getPlace(JSONObject stadeInfoJson, String status)
    {
        HashMap<String, String> stadePlaceMap = new HashMap<>();

        //String city = "-NA-";
        //String country = "-NA-";
        String address = "-NA-";
        String name = "-NA-";
        String latitude = "";
        String longitude = "";


        try {
            if(status.equals("OK"))
            {
                address = stadeInfoJson.getString("formatted_address");
                name = stadeInfoJson.getString("name");
                latitude = stadeInfoJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = stadeInfoJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            }


            stadePlaceMap.put("address", address);
            stadePlaceMap.put("name", name);
            stadePlaceMap.put("latitude", latitude);
            stadePlaceMap.put("longitude", longitude);

            Log.i("DEBUG STADE LIST", "address  "+stadePlaceMap.get("address"));
            Log.i("DEBUG STADE LIST", "name  "+stadePlaceMap.get("name"));
            Log.i("DEBUG STADE LIST", "latitude  "+stadePlaceMap.get("latitude"));
            Log.i("DEBUG STADE LIST", "longitude  "+stadePlaceMap.get("longitude"));



        } catch (JSONException e) {
            e.printStackTrace();

            return stadePlaceMap;
        }

        return stadePlaceMap;

    }
}