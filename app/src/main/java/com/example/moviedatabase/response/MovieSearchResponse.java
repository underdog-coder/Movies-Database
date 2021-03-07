package com.example.moviedatabase.response;


import com.example.moviedatabase.model.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//This class is for response for  single movie request
public class MovieSearchResponse {


    @SerializedName("results")  // for gson to serialize json and get movie results
    @Expose
    private List<Movie> movie;

    public List<Movie> getMovie() {
        return movie;
    }  // return list of movies from response

    @Override
    public String toString() {      //toString method
        return "MovieSearchResponse{" +
                "movie=" + movie.toString() +
                '}';
    }
}

