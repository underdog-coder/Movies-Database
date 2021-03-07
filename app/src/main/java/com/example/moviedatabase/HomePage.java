package com.example.moviedatabase;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import  androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedatabase.adapter.MovieRecyclerView;
import com.example.moviedatabase.adapter.OnMovieListener;
import com.example.moviedatabase.adapter.OnTrendingListener;
import com.example.moviedatabase.adapter.TrendingMovieRecyclerView;
import com.example.moviedatabase.database.AppDatabase;
import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.requests.MovieApi;
import com.example.moviedatabase.requests.Service;
import com.example.moviedatabase.response.MovieSearchResponse;
import com.example.moviedatabase.util.Constants;
import com.example.moviedatabase.viewModel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity  implements OnMovieListener, OnTrendingListener {

    private int waitingTime = 1000;
    private CountDownTimer cntr;
    private Intent intent;
    private boolean isSearch;
    private RecyclerView trendingMovies;
    private RecyclerView nowPlayingMovies;
    private RecyclerView searchMovies;
    private TextView nowPlayingText;
    private TextView trendingText;

    private MovieRecyclerView nowPlayingMovieRecyclerViewAdapter;
    //private MovieRecyclerView trendingMovieRecyclerViewAdapter;
    private TrendingMovieRecyclerView trendingMovieRecyclerViewAdapter;

   // private MovieRecyclerView searchMovieRecyclerViewAdapter;

    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        trendingMovies = findViewById(R.id.trendingMovie);
        nowPlayingText = findViewById(R.id.nowPlayingMoviesText);
        trendingText =  findViewById(R.id.trendingMoviesText);
        nowPlayingMovies = findViewById(R.id.nowPlayingMovies);

        isSearch = false;

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        ConfigureRecyclerView();
        trendingRecyclerView();
        //ConfigureSearchRecyclerView();
        ObserveAnyChange();
        ObserveAnyChangeNowPlaying();
        ObserveAnyChangeTrending();

        searchTrendingMovieApi(1);
        searchNowPlayingMovieApi(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.search_button);
        MenuItem favourite_bttm = menu.findItem(R.id.favourite_button);

        favourite_bttm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v("favourite button", "on click !!");
                startActivity(new Intent(HomePage.this,FavouritePage.class));
                return false;
            }
        });

     /*   searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                isSearch =  false;
                trendingText.setVisibility(View.VISIBLE);
                trendingMovies.setVisibility(View.VISIBLE);
                return false;
            }
        });*/

        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //ConfigureSearchRecyclerView();
                isSearch = true;
                searchMovieApi(query,1);
                nowPlayingText.setText("Search Results : " + query);
                trendingText.setVisibility(View.GONE);
                trendingMovies.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                waitingTime = (isSearch == true && newText.isEmpty()) ? 100 : 1000;

                if(cntr != null){
                    cntr.cancel();
                }
                cntr = new CountDownTimer(waitingTime, 500) {

                    public void onTick(long millisUntilFinished) {
                        Log.d("TIME","seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        if(newText.isEmpty()){
                            isSearch =  false;
                            nowPlayingText.setText("Now Playing Movies" );
                            searchNowPlayingMovieApi(1);
                            trendingText.setVisibility(View.VISIBLE);
                            trendingMovies.setVisibility(View.VISIBLE);
                            return;
                        }
                        isSearch = true;
                        searchMovieApi(newText,1);
                        nowPlayingText.setText("Search Results : " + newText);
                        trendingText.setVisibility(View.GONE);
                        trendingMovies.setVisibility(View.GONE);
                        Log.d("FINISHED","DONE");
                    }
                };
                cntr.start();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private  void ConfigureRecyclerView(){
        nowPlayingMovieRecyclerViewAdapter =  new MovieRecyclerView(this);
        nowPlayingMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        nowPlayingMovies.setAdapter(nowPlayingMovieRecyclerViewAdapter);
        nowPlayingMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollHorizontally(1)  && isSearch == false){
                    movieViewModel.searchNowPlayingMovieNextPageApi();
                }
                if(!recyclerView.canScrollHorizontally(1)  && isSearch == true){
                    movieViewModel.searchMovieNextPageApi();
                }
            }
        });

/*

        trendingMovieRecyclerViewAdapter =  new MovieRecyclerView(this);
        trendingMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        trendingMovies.setAdapter(trendingMovieRecyclerViewAdapter);
        trendingMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollHorizontally(1)){
                    movieViewModel.searchTrendingMovieNextPageApi();
                }
            }
        });

*/

    }

    private void trendingRecyclerView(){
        trendingMovieRecyclerViewAdapter =  new TrendingMovieRecyclerView(this);
        trendingMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        trendingMovies.setAdapter(trendingMovieRecyclerViewAdapter);
        trendingMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollHorizontally(1)){
                    movieViewModel.searchTrendingMovieNextPageApi();
                }
            }
        });

    }

  /*  private  void ConfigureSearchRecyclerView(){
        searchMovieRecyclerViewAdapter =  new MovieRecyclerView(this);
        searchMovies.setLayoutManager(new LinearLayoutManager(this));
        searchMovies.setAdapter(searchMovieRecyclerViewAdapter);

    }*/

    public void  searchMovieApi(String query, int pageNumber){
        movieViewModel.searchMovieApi(query,pageNumber);
    }

    public void  searchTrendingMovieApi(int pageNumber){
        movieViewModel.searchTrendingMovieApi(pageNumber);
    }

    public void  searchNowPlayingMovieApi(int pageNumber){
        movieViewModel.searchNowPlayingMovieApi(pageNumber);
    }

    public void  searchMovieNextPageApi(){
        movieViewModel.searchMovieNextPageApi();
    }

    public void  searchTrendingMovieNextPageApi(){
        movieViewModel.searchTrendingMovieNextPageApi();
    }

    public void  searchNowPlayingMovieNextPageApi(){
        movieViewModel.searchNowPlayingMovieNextPageApi();
    }

    private void ObserveAnyChange() {

        movieViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(movies != null){
                    nowPlayingMovieRecyclerViewAdapter.setmMovies(movies);
                    for(Movie movie : movies){
                        Log.v("tag" , "onChanged : " + movie.getOriginal_title());
                    }
                }
            }
        });

    }

    private void ObserveAnyChangeTrending() {

        movieViewModel.getTrendingMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(movies != null){
                   trendingMovieRecyclerViewAdapter.setmMovies(movies);
                    for(Movie movie : movies){
                        Log.v("trending" , "onChanged : " + movie.getOriginal_title());
                    }

                }
            }
        });
    }
    private void ObserveAnyChangeNowPlaying() {

        movieViewModel.getPlayingNowMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                nowPlayingMovieRecyclerViewAdapter.setmMovies(movies);
                if(movies != null){
                    for(Movie movie : movies){
                        Log.v("nowpl" , "onChanged : " + movie.getOriginal_title());
                    }
                }
            }
        });

    }
  /*  private void getRetrofitResponse(){
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi
                .searchMovie(Constants.API_KEY,"fast",1);

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if(response.code() ==200 ){
                    Log.v("rohit", "Api response"+response.body().toString());
                    List<Movie> movies = new ArrayList<>(response.body().getMovie());
                    for(Movie movie : movies){
                        Log.v("rohit", "title : " + movie.getOriginal_title());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });

    }*/


    @Override
    public void onMovieClick(int position) {
        //movieViewModel.insert(nowPlayingMovieRecyclerViewAdapter.getSelectedMovie(position));
        Log.v("rohit","movie id : " + nowPlayingMovieRecyclerViewAdapter.getSelectedMovie(position).getMovie_id());
            Intent intent =  new Intent(this,MovieDetails.class);
            intent.putExtra("movie",nowPlayingMovieRecyclerViewAdapter.getSelectedMovie(position));
            startActivity(intent);

    }

    @Override
    public void ontrendingMovieClick(int position) {
      // movieViewModel.delete(nowPlayingMovieRecyclerViewAdapter.getSelectedMovie(position));
        Log.v("rohit","movie id : " + trendingMovieRecyclerViewAdapter.getSelectedMovie(position).getMovie_id());
        Intent intent =  new Intent(this,MovieDetails.class);
        intent.putExtra("movie",trendingMovieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);


    }
}