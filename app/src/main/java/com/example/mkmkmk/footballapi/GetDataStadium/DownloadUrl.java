package com.example.mkmkmk.footballapi.GetDataStadium;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mkmkmk on 06/06/2018.
 */



public class DownloadUrl {

    public String readUrl(String myUrl)
    {
        String dataFinal="";

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {


            URL url = null;

            url = new URL(myUrl);


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBr = new StringBuffer();
            String line= "";

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBr.append(line);
            }


            dataFinal = stringBr.toString();
            bufferedReader.close();

        } catch (MalformedURLException e) {
            Log.i("DEBUG read URL Stade", "Error 0: "+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("DEBUG read URL Stade", "Error 1: "+e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }

        return dataFinal;

    }
}
