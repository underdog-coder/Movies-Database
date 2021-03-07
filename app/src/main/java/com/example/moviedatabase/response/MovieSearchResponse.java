package com.example.moviedatabase.response;


import com.example.moviedatabase.model.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//This class is for single movie request
public class MovieSearchResponse {


    @SerializedName("results")
    @Expose
    private List<Movie> movie;

    public List<Movie> getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "movie=" + movie.toString() +
                '}';
    }
}

