package com.example.johnrowley.mastermobiledemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {


    ListView myMovieListing = null;

    ArrayList moviesLoaded = new ArrayList<String>();

    ArrayList<MovieObject> listOfMovieObjects =  new ArrayList<MovieObject>();

    /**
     * Called when the activity is first created.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myMovieListing = (ListView) findViewById(R.id.movieListing);
//Setting this up as a test to confirm listings work
        /*

        ArrayList listOfMovies = new ArrayList<String>();
        listOfMovies.add("Spiderman");
        listOfMovies.add("Superman");
        listOfMovies.add("Batman");
        listOfMovies.add("The Green Lantern 2");
        listOfMovies.add("Wonder Woman");

        Log.i("Array Listing Test ", listOfMovies.get(0).toString());


        ArrayAdapter<String> arrayAdapterMovie = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listOfMovies);

       myMovieListing.setAdapter(arrayAdapterMovie);

      */
        myMovieListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   //Testing  myFriends.get(position)
              //  Toast.makeText(getApplicationContext(), "Hello " + moviesLoaded.get(position) , Toast.LENGTH_LONG).show();

                Intent movieDetailsIntent = new Intent(getApplicationContext(),MovieDetails.class);
                movieDetailsIntent.putExtra("SelectedMovie", moviesLoaded.get(position).toString());

                //Create a movie object, then pass each of its details over to the intent
                //This could be improved by passing an object to the Intent - look up parcelling in android
                MovieObject chosenMovie =  listOfMovieObjects.get(position);
                movieDetailsIntent.putExtra("MovieTitle", chosenMovie.Title);
                movieDetailsIntent.putExtra("MovieGenre", chosenMovie.Genre);

                startActivity(movieDetailsIntent);

            }
        });



        Log.i("Starting up ", "Application has started");

        DownloadTask task = new DownloadTask();
        task.execute("https://college-movies.herokuapp.com/");
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog = null;

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


        protected void onPreExecute() {
            Log.i("Starting up ", "pre execute has started");
            dialog = ProgressDialog.show(MainActivity.this, null, "Loading Movie Data");
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("Starting up ", "post execute has started");
            dialog.dismiss();
            String message = "";

            try {

                JSONArray listOfMoviesFromJSON = new JSONArray(result);


                Log.i("Movie Title", "Parsing Movie JSON Data");


                for (int i = 0; i < listOfMoviesFromJSON.length(); i++) {

                    JSONObject jsonPart = listOfMoviesFromJSON.getJSONObject(i);

                    Log.i("Movie Title", jsonPart.getString("title"));
                    Log.i("Movie Genre", jsonPart.getString("genre"));

                    moviesLoaded.add(jsonPart.getString("title"));


                    //Create a movie object
                    String title = jsonPart.getString("title");
                    String genre = jsonPart.getString("genre");

                    MovieObject m = new MovieObject(title, genre);

                    listOfMovieObjects.add(m);

                }


                Log.i("Movie Time", "list of movies");

                ArrayAdapter<String> arrayAdapterMovie = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, moviesLoaded);

                myMovieListing.setAdapter(arrayAdapterMovie);

                Log.i("Movie Time", "Adapter");


            } catch (JSONException e) {
                Log.i("MovieError", "retrieval error");
                e.printStackTrace();
            }
            Log.i("Movie ", "post execute has ended");
        }
    }

}
