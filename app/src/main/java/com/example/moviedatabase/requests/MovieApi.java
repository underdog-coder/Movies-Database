package com.example.moviedatabase.requests;

import com.example.moviedatabase.response.MovieSearchResponse;
import com.example.moviedatabase.response.NowPlayingMovieResponse;
import com.example.moviedatabase.response.TrendingMovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

     @GET("3/search/movie")
     Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
     );

     @GET("3/movie/now_playing")
     Call<TrendingMovieResponse> trendingMovie(
             @Query("api_key") String key,
             @Query("page") int page
     );

     @GET("3/movie/popular")
     Call<NowPlayingMovieResponse> nowPlayingMovie(
             @Query("api_key") String key,
             @Query("page") int page
     );
}
