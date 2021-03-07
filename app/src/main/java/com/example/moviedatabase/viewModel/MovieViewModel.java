package com.example.moviedatabase.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel  extends ViewModel {
    private MovieRepository movieRepository;

    public MovieViewModel() {
            movieRepository = MovieRepository.getInstance();
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

}

