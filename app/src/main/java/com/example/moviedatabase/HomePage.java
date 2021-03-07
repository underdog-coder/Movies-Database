package com.example.moviedatabase;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

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


/*  This is the Home page of the App in this Activity, We are using two recycler views to populate trending and Now Playing movies

 *   On the top right corner  Option for search is provided and for getting all the favourite movies also.
 * */
public class HomePage extends AppCompatActivity implements OnMovieListener, OnTrendingListener {

    private int waitingTime = 1000; // min time before text in search view will get searched with the API
    private CountDownTimer cntr;

    private boolean isSearch; // If search view is open
    private RecyclerView trendingMovies; // Recycler view to hold trending movies
    private RecyclerView nowPlayingMovies; // Recycler view to hold now Playing movies

    private TextView nowPlayingText;
    private TextView trendingText;

    private MovieRecyclerView nowPlayingMovieRecyclerViewAdapter;    // adapter for recycler view

    private TrendingMovieRecyclerView trendingMovieRecyclerViewAdapter;   // adapter for recycler view


    private MovieViewModel movieViewModel;   //View model implemntation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {          //registering broadcast receiver
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));    //checking for internet
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {        //registering broadcast receiver
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)); //checking for internet
        }

        setContentView(R.layout.home_page);
        trendingMovies = findViewById(R.id.trendingMovie);
        nowPlayingText = findViewById(R.id.nowPlayingMoviesText);
        trendingText = findViewById(R.id.trendingMoviesText);
        nowPlayingMovies = findViewById(R.id.nowPlayingMovies);

        isSearch = false;

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        ConfigureRecyclerView();
        trendingRecyclerView();

        ObserveAnyChange();
        ObserveAnyChangeNowPlaying();
        ObserveAnyChangeTrending();

        searchTrendingMovieApi(1);
        searchNowPlayingMovieApi(1);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {       // Broadcast receiver to check internet connectivity
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (isOnline(context)) {             // when connectivity is there

                    Toast.makeText(context, "Network Connected !!", Toast.LENGTH_SHORT).show();
                    nowPlayingText.setText("Now Playing Movies");
                    trendingText.setVisibility(View.VISIBLE);
                    searchTrendingMovieApi(1);
                    searchNowPlayingMovieApi(1);
                    trendingMovies.setVisibility(View.VISIBLE);
                    nowPlayingMovies.setVisibility(View.VISIBLE);
                } else {
                    nowPlayingText.setText("No Internet Connection :(");  // when no internet connection
                    trendingMovies.setVisibility(View.GONE);
                    nowPlayingMovies.setVisibility(View.GONE);
                    trendingText.setVisibility(View.GONE);
                    Toast.makeText(context, "Network Not Connected !!", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    };

    public boolean isOnline(Context context) {     // method to check for connectivity
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network nw = connectivityManager.getActiveNetwork();
                if (nw == null) return false;
                NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
                return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
            } else {
                NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
                return nwInfo != null && nwInfo.isConnected();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }



    protected void unregisterNetworkBroadcastReceiver() {   // unregistering broadcast receiver
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {      // unregistering broadcast receiver
        super.onDestroy();
        unregisterNetworkBroadcastReceiver();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {           // creating search and favourite button
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_button);
        MenuItem favourite_bttm = menu.findItem(R.id.favourite_button);

        favourite_bttm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {      // clicking favourite button to start new activity
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v("favourite button", "on click !!");
                startActivity(new Intent(HomePage.this, FavouritePage.class));
                return false;
            }
        });


        SearchView searchView = (SearchView) searchItem.getActionView();    //search view

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {             // when query is submitted

                isSearch = true;
                searchMovieApi(query, 1);
                nowPlayingText.setText("Search Results : " + query);
                trendingText.setVisibility(View.GONE);
                trendingMovies.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {            // when query is typed and is searched for automatically

                waitingTime = (isSearch == true && newText.isEmpty()) ? 100 : 1000;

                if (cntr != null) {
                    cntr.cancel();
                }
                cntr = new CountDownTimer(waitingTime, 500) {

                    public void onTick(long millisUntilFinished) {
                        Log.d("TIME", "seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        if (newText.isEmpty() && isOnline(HomePage.this)) {
                            isSearch = false;
                            nowPlayingText.setText("Now Playing Movies");
                            searchNowPlayingMovieApi(1);
                            searchTrendingMovieApi(1);
                            trendingText.setVisibility(View.VISIBLE);
                            trendingMovies.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (newText.isEmpty() && !isOnline(HomePage.this)) {
                            isSearch = false;
                            nowPlayingText.setText("No Internet Connection :(");
                            searchNowPlayingMovieApi(1);
                            searchTrendingMovieApi(1);
                            return;
                        }
                        isSearch = true;
                        searchMovieApi(newText, 1);
                        nowPlayingText.setText("Search Results : " + newText);
                        trendingText.setVisibility(View.GONE);
                        trendingMovies.setVisibility(View.GONE);
                        Log.d("FINISHED", "DONE");
                    }
                };
                cntr.start();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void ConfigureRecyclerView() {      //configuring recycler view for now playing movies
        nowPlayingMovieRecyclerViewAdapter = new MovieRecyclerView(this);
        nowPlayingMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nowPlayingMovies.setAdapter(nowPlayingMovieRecyclerViewAdapter);
        nowPlayingMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {    //pagination implemented
                if (!recyclerView.canScrollHorizontally(1) && isSearch == false) {
                    movieViewModel.searchNowPlayingMovieNextPageApi();
                }

            }
        });


    }

    private void trendingRecyclerView() {      //configuring recycler view for trending movies
        trendingMovieRecyclerViewAdapter = new TrendingMovieRecyclerView(this);
        trendingMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingMovies.setAdapter(trendingMovieRecyclerViewAdapter);
        trendingMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {     //pagination implemented
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieViewModel.searchTrendingMovieNextPageApi();
                }
            }
        });

    }


    public void searchMovieApi(String query, int pageNumber) {      //search keyword
        movieViewModel.searchMovieApi(query, pageNumber);
    }

    public void searchTrendingMovieApi(int pageNumber) {     //search trending movies
        movieViewModel.searchTrendingMovieApi(pageNumber);
    }

    public void searchNowPlayingMovieApi(int pageNumber) {    //search now playing movies
        movieViewModel.searchNowPlayingMovieApi(pageNumber);
    }


    private void ObserveAnyChange() {        // observing changes on search movie query response

        movieViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null) {
                    nowPlayingMovieRecyclerViewAdapter.setmMovies(movies); //putting in recycler view
                    for (Movie movie : movies) {
                        Log.v("tag", "onChanged : " + movie.getOriginal_title());
                    }
                }
            }
        });

    }

    private void ObserveAnyChangeTrending() { // observing changes on trending movie query response

        movieViewModel.getTrendingMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null) {
                    trendingMovieRecyclerViewAdapter.setmMovies(movies);  //putting in recycler view
                    for (Movie movie : movies) {
                        Log.v("trending", "onChanged : " + movie.getOriginal_title());
                    }

                }
            }
        });
    }

    private void ObserveAnyChangeNowPlaying() { // observing changes on now playing movie query response

        movieViewModel.getPlayingNowMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                nowPlayingMovieRecyclerViewAdapter.setmMovies(movies); //putting in recycler view
                if (movies != null) {
                    for (Movie movie : movies) {
                        Log.v("nowpl", "onChanged : " + movie.getOriginal_title());
                    }
                }
            }
        });

    }


    @Override
    public void onMovieClick(int position) {        // implmentation of on click on now playing movie, will create new activity with details of that movie

        Log.v("rohit", "movie id : " + nowPlayingMovieRecyclerViewAdapter.getSelectedMovie(position).getMovie_id());
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", nowPlayingMovieRecyclerViewAdapter.getSelectedMovie(position)); // putting extra details for the next activity
        startActivity(intent);

    }

    @Override
    public void ontrendingMovieClick(int position) {    // implmentation of on click on trending movie, will create new activity with details of that movie

        Log.v("rohit", "movie id : " + trendingMovieRecyclerViewAdapter.getSelectedMovie(position).getMovie_id());
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", trendingMovieRecyclerViewAdapter.getSelectedMovie(position)); // putting extra details for the next activity
        startActivity(intent);


    }
}