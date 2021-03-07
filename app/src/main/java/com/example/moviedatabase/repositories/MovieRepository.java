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


/* repository for the movies, It gets data from MovieAPi client and gives data to the view model*/
public class MovieRepository {

    private static MovieRepository instance; //  instance of repository
    private MovieApiClient movieApiClient;  // api client

    private String mQuery; //query
    private int mPageNumber; // pagenumber
    private MovieDao movieDao; // Dao for DB

    private LiveData<List<Movie>> fav_movies;  // list for fav movies

    public static MovieRepository getInstance(Context context) {  // singleton implementation
        if (instance == null) {
            instance = new MovieRepository(context); // to get instance
        }
        return instance;
    }

    private MovieRepository(Context context) {     // constructor
        movieDao = AppDatabase.getInstance(context).MovieDao(); // Dao instance
        movieApiClient = MovieApiClient.getInstance(); // Apiclient instance
    }

    public void insert(Movie movie) {
        movieDao.insert(movie);
    } // for inserting in fav table

    public void delete(Movie movie) {
        movieDao.delete(movie);
    } // for deleting movie from fav table

    public boolean isFavourite(int movie_id) {
        return movieDao.isFavourite(movie_id);
    } // for checking if present in fav table

    public void deleteAllMovies() {
        movieDao.deleteAllMovies();
    } // for deleting all movies

    public LiveData<List<Movie>> getFav_movies() {
        return movieDao.getAllMovies();
    } // live data for fav movies

    public LiveData<List<Movie>> getMovies() {
        return movieApiClient.getMovies();    // livedata for moviesearch
    }

    public LiveData<List<Movie>> getPlayingNowMovies() { // livedata for nowplaying movies
        return movieApiClient.getPlayingNowMovies();
    }

    public LiveData<List<Movie>> getTrendingMovies() {
        return movieApiClient.getTrendingMovies();
    } //live data for trending movies

    public void searchFavMovies() {
        movieDao.getAllMovies();
    } // to get fav movies from DB


    public void searchMovieApi(String query, int pageNumber) {  // for getting data search movie date from api client
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMovieApi(query, pageNumber);
    }

    public void searchTrendingMovieApi(int pageNumber) {  // for getting data trending movie date from api client
        mPageNumber = pageNumber;
        movieApiClient.searchTrendingMovieApi(pageNumber);
    }

    public void searchNowPlayingMovieApi(int pageNumber) {  // for getting data now playing movie date from api client
        mPageNumber = pageNumber;
        movieApiClient.searchNowPlayingMovieApi(pageNumber);
    }

    public void searchMovieNextPageApi() {   // for implementation of paging on search results

        movieApiClient.searchMovieApi(mQuery, mPageNumber + 1);
        mPageNumber += 1;
    }

    public void searchTrendingMovieNextPageApi() { // for implementation of paging on trending movie results

        movieApiClient.searchTrendingMovieApi(mPageNumber + 1);
        mPageNumber += 1;
    }

    public void searchNowPlayingMovieNextPageApi() { // for implementation of paging on now playing movie results
        movieApiClient.searchNowPlayingMovieApi(mPageNumber + 1);
        mPageNumber += 1;
    }


}
