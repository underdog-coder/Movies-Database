package com.example.moviedatabase.requests;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.example.moviedatabase.AppExecutor;
import com.example.moviedatabase.R;
import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.response.MovieSearchResponse;
import com.example.moviedatabase.response.NowPlayingMovieResponse;
import com.example.moviedatabase.response.TrendingMovieResponse;
import com.example.moviedatabase.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;




/*This class Acts a Api client for repostory to get data from  and returns Live data that can be then observerd in the view*/
public class MovieApiClient {

    //Live Data for searching of Movies
    private MutableLiveData<List<Movie>> mMovies;

    private RetrieveMoviesRunnable retrieveMoviesRunnable; // implementation of runnable

    private static MovieApiClient instance;

    //Live Data for Popular/trending Movies
    private MutableLiveData<List<Movie>> mTrendingMovies;

    private RetrieveTrendingMoviesRunnable retrieveTrendingMoviesRunnable;  // implementation of runnable

    //Live Data for Popular/trending Movies
    private MutableLiveData<List<Movie>> mPlayingNowMovies;

    private RetrievePlayingNowMoviesRunnable retrievePlayingNowMoviesRunnable; // implementation of runnable


    public static MovieApiClient getInstance() {   // implementation of singleton pattern for Api client
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;   // returning the instance
    }


    private MovieApiClient() {                        // constructor to instantiate live data for movie search, trending and now playing movies
        mMovies = new MutableLiveData<>();           //search movie result
        mPlayingNowMovies = new MutableLiveData<>();   //now playing movie result
        mTrendingMovies = new MutableLiveData<>();  //trending movie result
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return mMovies;
    }  //returning livedata

    public MutableLiveData<List<Movie>> getPlayingNowMovies() {
        return mPlayingNowMovies;
    }  //returning livedata

    public MutableLiveData<List<Movie>> getTrendingMovies() {
        return mTrendingMovies;
    }  //returning livedata

    public void searchMovieApi(String query, int pageNumber) {    //getting search result from api on background thread
        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }
        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);  // making runnable item for handler

        final Future myHandler = AppExecutor.getInstance().networkIO().submit(retrieveMoviesRunnable); //handler to get data in background

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {   //time out after 3 seconds
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);
    }


    //Trending Movies
    public void searchTrendingMovieApi(int pageNumber) {   //getting search result from api on background thread
        if (retrieveTrendingMoviesRunnable != null) {
            retrieveTrendingMoviesRunnable = null;
        }
        retrieveTrendingMoviesRunnable = new RetrieveTrendingMoviesRunnable(pageNumber);  // making runnable item for handler

        final Future myHandler = AppExecutor.getInstance().networkIO().submit(retrieveTrendingMoviesRunnable); //handler to get data in background

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {  //time out after 3 seconds
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);
    }


    //Now Playing movies
    public void searchNowPlayingMovieApi(int pageNumber) {  //getting search result from api on background thread
        if (retrievePlayingNowMoviesRunnable != null) {
            retrievePlayingNowMoviesRunnable = null;
        }
        retrievePlayingNowMoviesRunnable = new RetrievePlayingNowMoviesRunnable(pageNumber);  // making runnable item for handler

        final Future myHandler = AppExecutor.getInstance().networkIO().submit(retrievePlayingNowMoviesRunnable); //handler to get data in background

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {   //time out after 3 seconds
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);
    }


    //Search Movie
    private class RetrieveMoviesRunnable implements Runnable {
        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {   // constructor for the class
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(query, pageNumber).execute();  // to store response
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {  // on success
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse) response.body()).getMovie()); // for storing responses
                    // mMovies.postValue(list);
                    if (pageNumber == 1) {
                        mMovies.postValue(list);        // putting values in livedata
                    } else {
                        List<Movie> currentMovies = mMovies.getValue();
                        if (currentMovies == null) {
                            currentMovies = new ArrayList<>(); // putting values in livedata
                        }
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().toString();
                    Log.v("Tag", "Error" + error);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {  // calling for getting the response for search
            return Service.getMovieApi().searchMovie(
                    Constants.API_KEY,
                    query,
                    pageNumber);
        }

        private void setCancelRequest() {
            Log.v("Tag", "Cancelling Search request");
            cancelRequest = true;
        }

    }


    //Now Playing
    private class RetrievePlayingNowMoviesRunnable implements Runnable {

        private int pageNumber;
        private boolean cancelRequest;

        public RetrievePlayingNowMoviesRunnable(int pageNumber) { //constructor
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(pageNumber).execute(); // to store response
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Movie> list = new ArrayList<>(((NowPlayingMovieResponse) response.body()).getMovies());  // // for storing responses
                    if (pageNumber == 1) {
                        mPlayingNowMovies.postValue(list);  // putting values in livedata
                    } else {
                        List<Movie> currentMovies = mPlayingNowMovies.getValue();
                        if (currentMovies == null) {
                            currentMovies = new ArrayList<>();
                        }
                        currentMovies.addAll(list);
                        mPlayingNowMovies.postValue(currentMovies); // putting values in livedata
                    }
                } else {
                    String error = response.errorBody().toString();
                    Log.v("Tag", "Error" + error);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mPlayingNowMovies.postValue(null);
            }


        }

        private Call<NowPlayingMovieResponse> getMovies(int pageNumber) {  //calling api from retrofit
            return Service.getMovieApi().nowPlayingMovie(
                    Constants.API_KEY,
                    pageNumber);
        }

        private void setCancelRequest() {
            Log.v("Tag", "Cancelling Search request");
            cancelRequest = true;
        }

    }


    //trending
    private class RetrieveTrendingMoviesRunnable implements Runnable {

        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveTrendingMoviesRunnable(int pageNumber) {  //constructor
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Movie> list = new ArrayList<>(((TrendingMovieResponse) response.body()).getMovies()); // list to store response

                    if (pageNumber == 1) {
                        mTrendingMovies.postValue(list);  //adding data to live data
                    } else {
                        List<Movie> currentMovies = mTrendingMovies.getValue();
                        if (currentMovies == null) {
                            currentMovies = new ArrayList<>();
                        }
                        currentMovies.addAll(list);
                        mTrendingMovies.postValue(currentMovies);  //adding data to live data
                    }
                } else {
                    String error = response.errorBody().toString();
                    Log.v("Tag", "Error" + error);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mTrendingMovies.postValue(null);
            }


        }

        private Call<TrendingMovieResponse> getMovies(int pageNumber) { //calling api from retrofit
            return Service.getMovieApi().trendingMovie(
                    Constants.API_KEY,
                    pageNumber);
        }

        private void setCancelRequest() {
            Log.v("Tag", "Cancelling Search request");
            cancelRequest = true;
        }

    }

}
