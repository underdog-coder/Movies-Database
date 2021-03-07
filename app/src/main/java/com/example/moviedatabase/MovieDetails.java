package com.example.moviedatabase;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.viewModel.MovieViewModel;

public class MovieDetails extends AppCompatActivity  {


    private ImageView imageView,favorite;
    private TextView title,overview;
    private int movie_id;
    private MovieViewModel movieViewModel;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        imageView =  findViewById(R.id.imageView_details);
        favorite =  findViewById(R.id.favourite);
        title =  findViewById(R.id.title_details);
        overview =  findViewById(R.id.overview);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        GetDataFromIntent();
        if(!FavouriteCheck()){
                favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
        else {
            favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!movieViewModel.isFavourite(movie_id)){
                    favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    movieViewModel.insert(movie);
                }
                else{
                    favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    movieViewModel.delete(movie);
                }
            }
        });

    }
    private boolean FavouriteCheck(){
        return movieViewModel.isFavourite(movie_id);
    }
    public void GetDataFromIntent(){
        if(getIntent().hasExtra("movie")){
            movie =  getIntent().getParcelableExtra("movie");
            title.setText(movie.getOriginal_title());
            overview.setText(movie.getOverview());
            movie_id = movie.getMovie_id();
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movie.getPoster_path()).into(imageView);
            Log.v("details", "intent : " + movie.getOriginal_title());
        }
    }


}