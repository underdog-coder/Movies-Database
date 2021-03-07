package com.example.moviedatabase.response;


import com.example.moviedatabase.model.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//This class is for getting now playing movies (List of movies) as a response
public class NowPlayingMovieResponse {

    @SerializedName("total_results") // for gson to serialize json and get total results
    @Expose()
    private int totalCount;

    @SerializedName("results") // for gson to serialize json and get movie results
    @Expose()
    private List<Movie> movies;

    public int getTotalCount() {
        return totalCount;
    } //  return count of movies from response

    public List<Movie> getMovies() {
        return movies;
    } // return list of movies from response
}
