package com.example.moviedatabase.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.example.moviedatabase.database.AppDatabase;
import com.example.moviedatabase.database.MovieDao;
import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.requests.MovieApiClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieRepository {

    private static MovieRepository instance;
    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;
    private MovieDao movieDao;
    private LiveData<List<Movie>> fav_movies;

    public static MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(Application application){
        AppDatabase database =AppDatabase.getInstance(application);
        movieDao = database.MovieDao();
        //fav_movies = movieDao.getAllMovies();
    }

    public void insert (Movie movie){

    }
    public void update (Movie movie){

    }

    public void delete (Movie movie){

    }

    public LiveData<List<Movie>> getFav_movies() {
        return fav_movies;
    }





    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<Movie>> getMovies(){
        return movieApiClient.getMovies();
    }

    public  LiveData<List<Movie>> getPlayingNowMovies(){
        return movieApiClient.getPlayingNowMovies();
    }
    public  LiveData<List<Movie>> getTrendingMovies(){
        return movieApiClient.getTrendingMovies();
    }


    public void  searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber =  pageNumber;
        movieApiClient.searchMovieApi(query,pageNumber);
    }

    public void  searchTrendingMovieApi(int pageNumber){
        mPageNumber =  pageNumber;
        movieApiClient.searchTrendingMovieApi(pageNumber);
    }
    public void  searchNowPlayingMovieApi(int pageNumber){
        mPageNumber =  pageNumber;
        movieApiClient.searchNowPlayingMovieApi(pageNumber);
    }
    public void searchMovieNextPageApi(){

        movieApiClient.searchMovieApi(mQuery,mPageNumber+1);
        mPageNumber +=1;
    }

    public void  searchTrendingMovieNextPageApi(){

        movieApiClient.searchTrendingMovieApi(mPageNumber+1);
        mPageNumber +=1;
    }
    public void  searchNowPlayingMovieNextPageApi(){
        movieApiClient.searchNowPlayingMovieApi(mPageNumber+1);
        mPageNumber +=1;
    }

}
