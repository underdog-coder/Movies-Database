package com.example.moviedatabase.repositories;

import android.app.Application;
import android.content.Context;
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

    public static MovieRepository getInstance(Context context){
        if(instance == null){
            instance = new MovieRepository(context);
        }
        return instance;
    }

    private MovieRepository(Context context){
        movieDao = AppDatabase.getInstance(context).MovieDao();
        movieApiClient = MovieApiClient.getInstance();
    }

    public void insert (Movie movie){
            movieDao.insert(movie);
    }
    public void update (Movie movie){
            movieDao.update(movie);
    }

    public void delete (Movie movie){
            movieDao.delete(movie);
    }

    public boolean isFavourite(int movie_id){
        return movieDao.isFavourite(movie_id);
    }
    public void deleteAllMovies(){
        movieDao.deleteAllMovies();
    }

    public LiveData<List<Movie>> getFav_movies() {
        return movieDao.getAllMovies();
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
    public void searchFavMovies(){
        movieDao.getAllMovies();
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
