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

        ConfigureRecyclerView();
        ObserveAnyChange();
        searchFavMovies();

    }


    private  void ConfigureRecyclerView() {

        favouriteMovieRecyclerViewAdapter = new MovieRecyclerView(this);
        favouriteMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        favouriteMovies.setAdapter(favouriteMovieRecyclerViewAdapter);

    }


    private void ObserveAnyChange() {

        movieViewModel.getFav_movies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(movies != null){
                    favouriteMovieRecyclerViewAdapter.setmMovies(movies);
                    for(Movie movie : movies){
                        Log.v("tag" , "onChanged : " + movie.getOriginal_title());
                    }
                }
            }
        });

    }

    public void  searchFavMovies(){
        movieViewModel.searchFavMovies();
    }

    @Override
    public void onMovieClick(int position) {
        Log.v("rohit","movie id : " + favouriteMovieRecyclerViewAdapter.getSelectedMovie(position).getMovie_id());
        Intent intent =  new Intent(this,MovieDetails.class);
        intent.putExtra("movie",favouriteMovieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);

    }
}
