package com.example.pray.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpConnect {

    public static ArrayList<PrayData> doInBackground(String url) {
        String data = httpConnecting(url);
        return getJson(data);
    }

    public static String httpConnecting(String url) {
        HttpURLConnection httpURLConnection;
        String data = "";
        try {
            httpURLConnection = (HttpURLConnection) getUrl(url).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            data = getStream(httpURLConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getStream(HttpURLConnection httpURLConnection) {
        String line = "";
        InputStream inputStream = null;
        StringBuilder fullData = new StringBuilder();
        try {
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            line = bufferedReader.readLine();
            while (line != null) {
                fullData.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fullData.toString();
    }

    public static ArrayList<PrayData> getJson(String json) {
        Log.v("JSON=\n",json);
        ArrayList<PrayData> timeDataArray = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject results = root.getJSONObject("results");
            JSONArray datetime = results.getJSONArray("datetime");

            for (int i = 0; i < datetime.length(); i++) {
                JSONObject currentDay = datetime.getJSONObject(i);
                //Timings
                JSONObject times = currentDay.getJSONObject("times");
                String fajr = times.getString("Fajr");
                String duhr = times.getString("Dhuhr");
                String asr = times.getString("Asr");
                String maghrib = times.getString("Maghrib");
                String isha = times.getString("Isha");
                //Date
                JSONObject currentDate = currentDay.getJSONObject("date");

                String date = currentDate.getString("gregorian");

                String[] prayNames = {"Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"};
                String[] prayTimes = {fajr, duhr, asr, maghrib, isha};

                timeDataArray.add(new PrayData(date, prayNames, prayTimes));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timeDataArray;
    }

    public static URL getUrl(String url) throws MalformedURLException {
        return new URL(url);
    }
}
