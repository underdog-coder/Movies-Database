package com.example.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedatabase.adapter.MovieRecyclerView;
import com.example.moviedatabase.adapter.OnMovieListener;
import com.example.moviedatabase.adapter.OnTrendingListener;
import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.viewModel.MovieViewModel;

import java.util.List;



/*This Activity show all your fav movies in a recycler view   and allows user to get their details and delete them from fav table */

public class FavouritePage  extends AppCompatActivity implements OnMovieListener{
    private MovieRecyclerView favouriteMovieRecyclerViewAdapter;
    private MovieViewModel movieViewModel;
    private RecyclerView favouriteMovies;
    private TextView favMoviesText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_page);

        favMoviesText =  findViewById(R.id.favMoviesText);
        favouriteMovies = findViewById(R.id.favMovies);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        ConfigureRecyclerView(); // configuring recycler view
        ObserveAnyChange();  // observing any changes on the the favorite movies
        searchFavMovies();  // to query DB for fav movies

    }


    private  void ConfigureRecyclerView() {   // configuring recycler view

        favouriteMovieRecyclerViewAdapter = new MovieRecyclerView(this);
        favouriteMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        favouriteMovies.setAdapter(favouriteMovieRecyclerViewAdapter);

    }


    private void ObserveAnyChange() {  // observing any changes on the the favorite movies

        movieViewModel.getFav_movies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(movies != null){
                    favouriteMovieRecyclerViewAdapter.setmMovies(movies);   // putting movies in the recycler view
                    for(Movie movie : movies){
                        Log.v("tag" , "onChanged : " + movie.getOriginal_title());
                    }
                }
            }
        });

    }

    public void  searchFavMovies(){
        movieViewModel.searchFavMovies();
    }    // to query fav movies

    @Override
    public void onMovieClick(int position) {    // To go to the movie details page
        Log.v("rohit","movie id : " + favouriteMovieRecyclerViewAdapter.getSelectedMovie(position).getMovie_id());
        Intent intent =  new Intent(this,MovieDetails.class);
        intent.putExtra("movie",favouriteMovieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);

    }
}
