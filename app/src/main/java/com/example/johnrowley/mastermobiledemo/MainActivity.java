package com.example.johnrowley.mastermobiledemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Starting up ", "Application has started");

        DownloadTask task = new DownloadTask();
        task.execute("https://college-movies.herokuapp.com/");
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                //Retrieve the movies - it is a list of objects, not a single object
                JSONArray arr = new JSONArray(result);

               //JSONObject jsonObject = new JSONObject(result);

               // String weatherInfo = jsonObject.getString("title");

                Log.i("Movie Title", "testing movie");

               // JSONArray arr = new JSONArray(weatherInfo);
//arr.length()

                //If we want to retreive all movies we would use: arr.length()
                //But for demonstration purposes, only retrieving 10 items
               for (int i = 0; i < 10; i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                   Log.i("Movie Ttle", jsonPart.getString("title"));
                   Log.i("Movie Genre", jsonPart.getString("genre"));

                   //Extract the running times - an object containing arrays

                   JSONObject runningTimes = jsonPart.getJSONObject("runningTimes");

                   //Extract Monday

                   JSONArray mondayRunningTimes = runningTimes.getJSONArray("mon");

                   for(int j=0; j < mondayRunningTimes.length(); j++) {

                       //Json Array, extract the running time based on j index
                       String runningTime = mondayRunningTimes.getString(j);
                       Log.i("Monday Time",  runningTime);
                   }


                }


            } catch (JSONException e) {
                Log.i("MovieError", "retrieval error");
                e.printStackTrace();
            }

git 

        }
    }

}
