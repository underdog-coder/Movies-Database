package com.example.moviedatabase.response;


import com.example.moviedatabase.model.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//This class is for getting Trending movies (List of movies)
public class TrendingMovieResponse {

    @SerializedName("total_results")
    @Expose()
    private int totalCount;

    @SerializedName("results")
    @Expose()
    private List<Movie> movies;

    public int getTotalCount() {
        return totalCount;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
