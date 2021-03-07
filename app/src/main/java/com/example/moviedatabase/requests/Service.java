package com.example.moviedatabase.requests;

import com.example.moviedatabase.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*This is the retrofit service calss , here we initialise retrofit object for fetching data from internet  */
public class Service {
    private static Retrofit.Builder retrofitBuilder =       // defining retrofit object
            new Retrofit.Builder()
                    .baseUrl(Constants.MOVIE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();  //building retrofit object

    private static MovieApi movieApi = retrofit.create(MovieApi.class);  // creating instance of movieApi

    public static MovieApi getMovieApi(){
        return movieApi;
    }    // returning instance of movieApi
}
