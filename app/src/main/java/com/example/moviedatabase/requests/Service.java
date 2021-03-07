package com.example.moviedatabase.requests;

import com.example.moviedatabase.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.moviedatabase.util.Constants.CONNECTION_TIMEOUT;
import static com.example.moviedatabase.util.Constants.READ_TIMEOUT;
import static com.example.moviedatabase.util.Constants.WRITE_TIMEOUT;

public class Service {

    private static OkHttpClient client = new OkHttpClient.Builder()

            // establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(false)

            .build();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.MOVIE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    public static MovieApi getMovieApi(){
        return movieApi;
    }
}
