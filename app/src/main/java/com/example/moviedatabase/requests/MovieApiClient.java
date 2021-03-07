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

public class MovieApiClient {

    //Live Data for searching of Movies
    private MutableLiveData<List<Movie>> mMovies;

    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    private static MovieApiClient instance;

    //Live Data for Popular/trending Movies
    private MutableLiveData<List<Movie>> mTrendingMovies;

    private RetrieveTrendingMoviesRunnable retrieveTrendingMoviesRunnable;

    //Live Data for Popular/trending Movies
    private MutableLiveData<List<Movie>> mPlayingNowMovies;

    private RetrievePlayingNowMoviesRunnable retrievePlayingNowMoviesRunnable;


    public static MovieApiClient getInstance(){
        if (instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }


    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mPlayingNowMovies =  new MutableLiveData<>();
        mTrendingMovies =  new MutableLiveData<>();
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return mMovies;
    }
    public MutableLiveData<List<Movie>> getPlayingNowMovies() {
        return mPlayingNowMovies;
    }
    public MutableLiveData<List<Movie>> getTrendingMovies() {
        return mTrendingMovies;
    }

    public void  searchMovieApi(String query, int pageNumber){
        if(retrieveMoviesRunnable != null ){
            retrieveMoviesRunnable = null;
        }
        retrieveMoviesRunnable =  new RetrieveMoviesRunnable(query,pageNumber);

        final Future myHandler = AppExecutor.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                    myHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);
    }





    //Trending Movies
    public void  searchTrendingMovieApi( int pageNumber){
        if(retrieveTrendingMoviesRunnable != null ){
            retrieveTrendingMoviesRunnable = null;
        }
        retrieveTrendingMoviesRunnable =  new RetrieveTrendingMoviesRunnable (pageNumber);

        final Future myHandler = AppExecutor.getInstance().networkIO().submit(retrieveTrendingMoviesRunnable);

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);
    }



    //Now Playing movies
    public void  searchNowPlayingMovieApi( int pageNumber){
        if(retrievePlayingNowMoviesRunnable != null ){
            retrievePlayingNowMoviesRunnable = null;
        }
        retrievePlayingNowMoviesRunnable =  new RetrievePlayingNowMoviesRunnable(pageNumber);

        final Future myHandler = AppExecutor.getInstance().networkIO().submit(retrievePlayingNowMoviesRunnable);

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);
    }




//Search Movie
    private class RetrieveMoviesRunnable implements  Runnable{
        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try{
                Response response = getMovies(query,pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovie());
                   // mMovies.postValue(list);
                    if(pageNumber == 1){
                        mMovies.postValue(list);
                    }
                    else {
                        List<Movie> currentMovies = mMovies.getValue();
                        if(currentMovies == null){
                            currentMovies = new ArrayList<>();
                        }
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }
                else{
                    String error = response.errorBody().toString();
                    Log.v("Tag", "Error" + error);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getMovies(String query,int pageNumber){
                return Service.getMovieApi().searchMovie(
                        Constants.API_KEY,
                        query,
                        pageNumber);
        }
        private void setCancelRequest(){
            Log.v("Tag","Cancelling Search request");
            cancelRequest = true;
        }

    }



    //Now Playing
    private class RetrievePlayingNowMoviesRunnable implements  Runnable{

        private int pageNumber;
        private boolean cancelRequest;

        public RetrievePlayingNowMoviesRunnable( int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try{
                Response response = getMovies(pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<Movie> list = new ArrayList<>(((NowPlayingMovieResponse)response.body()).getMovies());
                    if(pageNumber == 1){
                        mPlayingNowMovies.postValue(list);
                    }
                    else {
                        List<Movie> currentMovies = mPlayingNowMovies.getValue();
                        if(currentMovies == null){
                            currentMovies = new ArrayList<>();
                        }
                        currentMovies.addAll(list);
                        mPlayingNowMovies.postValue(currentMovies);
                    }
                }
                else{
                    String error = response.errorBody().toString();
                    Log.v("Tag", "Error" + error);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mPlayingNowMovies.postValue(null);
            }


        }

        private Call<NowPlayingMovieResponse> getMovies(int pageNumber){
            return Service.getMovieApi().nowPlayingMovie(
                    Constants.API_KEY,
                    pageNumber);
        }
        private void setCancelRequest(){
            Log.v("Tag","Cancelling Search request");
            cancelRequest = true;
        }

    }


    //trending
    private class RetrieveTrendingMoviesRunnable implements  Runnable{

        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveTrendingMoviesRunnable(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try{
                Response response = getMovies(pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<Movie> list = new ArrayList<>(((TrendingMovieResponse)response.body()).getMovies());
                    // mMovies.postValue(list);
                    if(pageNumber == 1){
                        mTrendingMovies.postValue(list);
                    }
                    else {
                        List<Movie> currentMovies = mTrendingMovies.getValue();
                        if(currentMovies == null){
                            currentMovies = new ArrayList<>();
                        }
                        currentMovies.addAll(list);
                        mTrendingMovies.postValue(currentMovies);
                    }
                }
                else{
                    String error = response.errorBody().toString();
                    Log.v("Tag", "Error" + error);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mTrendingMovies.postValue(null);
            }


        }

        private Call<TrendingMovieResponse> getMovies(int pageNumber){
            return Service.getMovieApi().trendingMovie(
                    Constants.API_KEY,
                    pageNumber);
        }
        private void setCancelRequest(){
            Log.v("Tag","Cancelling Search request");
            cancelRequest = true;
        }

    }

}
