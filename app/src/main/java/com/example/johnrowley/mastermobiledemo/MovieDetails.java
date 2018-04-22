package com.example.johnrowley.mastermobiledemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent movieDetailsReceived = getIntent();

        //Note, the ids are in the resource file
        TextView textTitle = (TextView) findViewById(R.id.movieTitle);
        TextView textGenre = (TextView) findViewById(R.id.movieGenre);

        String movieTitle = movieDetailsReceived.getStringExtra("MovieTitle");
        String movieGenre = movieDetailsReceived.getStringExtra("MovieGenre");

        textTitle.setText(movieTitle);
        textGenre.setText(movieGenre);

        //Can you add year, times etc? to the detail view by amending the table?


        Log.i("Movie", movieTitle);
        Log.i("Movie", movieGenre);

    }
}
