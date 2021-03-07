package com.example.moviedatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.viewModel.MovieViewModel;

public class MovieDetails extends AppCompatActivity {


    private ImageView imageView;
    private TextView title,overview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageView =  findViewById(R.id.imageView_details);
        title =  findViewById(R.id.title_details);
        overview =  findViewById(R.id.overview);

        GetDataFromIntent();
    }
    public void GetDataFromIntent(){
        if(getIntent().hasExtra("movie")){
            Movie movie =  getIntent().getParcelableExtra("movie");
            title.setText(movie.getOriginal_title());
            overview.setText(movie.getOverview());

            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movie.getPoster_path()).into(imageView);

            Log.v("details", "intent : " + movie.getOriginal_title());
        }
    }
}