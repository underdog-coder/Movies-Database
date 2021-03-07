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


/*View Model class for movie . It helps  in view to gt data from repository */

public class MovieViewModel  extends AndroidViewModel {
    private MovieRepository movieRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application);
    }

    public LiveData<List<Movie>> getMovies(){      //returning live data to the views movie search results
        return  movieRepository.getMovies();
    }

    public LiveData<List<Movie>> getPlayingNowMovies(){    //returning now playing live data
        return  movieRepository.getPlayingNowMovies();
    }
    public LiveData<List<Movie>> getTrendingMovies(){  //returning trending movie live data
        return  movieRepository.getTrendingMovies();
    }
    public LiveData<List<Movie>> getFav_movies(){
        return  movieRepository.getFav_movies();
    }  //returning favourite movies live data

    public void  searchMovieApi(String query, int pageNumber){   // calling repository for a movie search
        movieRepository.searchMovieApi(query,pageNumber);
    }

    public void  searchTrendingMovieApi(int pageNumber){    // calling repository for getting trending movies
        movieRepository.searchTrendingMovieApi(pageNumber);
    }

    public void  searchNowPlayingMovieApi(int pageNumber){ // calling repository for getting now playing movies
        movieRepository.searchNowPlayingMovieApi(pageNumber);
    }

    public void  searchTrendingMovieNextPageApi(){    //to implement pagination on trending movies
        movieRepository.searchTrendingMovieNextPageApi();
    }
    public void  searchNowPlayingMovieNextPageApi(){    //to implement pagination on now playing movies
        movieRepository.searchNowPlayingMovieNextPageApi();
    }

    public void searchFavMovies(){
        movieRepository.searchFavMovies();
    }  // calling repository for getting fav movies

    public boolean isFavourite(int movie_id){
        return movieRepository.isFavourite(movie_id);
    }   //  // calling repository checking if a movie is favorite


    public void insert (Movie movie){
        movieRepository.insert(movie);
    }    // calling repository checking for inserting a movie in favorite


    public void delete (Movie movie){
        movieRepository.delete(movie);
    }   // calling repository checking for deleting a movie in favorite
    public void deleteAllMovies(){
        movieRepository.deleteAllMovies();
    }  // calling repository checking for deleting all movies in favorite

}

