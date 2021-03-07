package com.example.moviedatabase.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel  extends AndroidViewModel {
    private MovieRepository movieRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application);
    }

    public LiveData<List<Movie>> getMovies(){
        return  movieRepository.getMovies();
    }

    public LiveData<List<Movie>> getPlayingNowMovies(){
        return  movieRepository.getPlayingNowMovies();
    }
    public LiveData<List<Movie>> getTrendingMovies(){
        return  movieRepository.getTrendingMovies();
    }
    public LiveData<List<Movie>> getFav_movies(){
        return  movieRepository.getFav_movies();
    }

    public void  searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query,pageNumber);
    }

    public void  searchTrendingMovieApi(int pageNumber){
        movieRepository.searchTrendingMovieApi(pageNumber);
    }

    public void  searchNowPlayingMovieApi(int pageNumber){
        movieRepository.searchNowPlayingMovieApi(pageNumber);
    }

    public void searchMovieNextPageApi(){
        movieRepository.searchMovieNextPageApi();
    }

    public void  searchTrendingMovieNextPageApi(){
        movieRepository.searchTrendingMovieNextPageApi();
    }
    public void  searchNowPlayingMovieNextPageApi(){
        movieRepository.searchNowPlayingMovieNextPageApi();
    }

    public void searchFavMovies(){
        movieRepository.searchFavMovies();
    }
    public boolean isFavourite(int movie_id){
        return movieRepository.isFavourite(movie_id);
    }


    public void insert (Movie movie){
        movieRepository.insert(movie);
    }
    public void update (Movie movie){
        movieRepository.update(movie);
    }

    public void delete (Movie movie){
        movieRepository.delete(movie);
    }
    public void deleteAllMovies(){
        movieRepository.deleteAllMovies();
    }

}

