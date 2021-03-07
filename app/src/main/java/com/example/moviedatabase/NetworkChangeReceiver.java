package com.example.moviedatabase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.viewModel.MovieViewModel;

import java.sql.Connection;

public class NetworkChangeReceiver extends BroadcastReceiver {
    MovieViewModel movieViewModel;
    @Override
    public void onReceive(Context context, Intent intent) {
       try{
           if(isOnline(context)){
              // intent = new Intent(HomePage.this,MovieDetails.class);
               Toast.makeText(context,"Network Connected !!",Toast.LENGTH_SHORT).show();

               searchTrendingMovieApi(1);
               searchNowPlayingMovieApi(1);
           }
           else{

               Toast.makeText(context,"Network Not Connected !!",Toast.LENGTH_SHORT).show();
           }
       }catch (NullPointerException e){
           e.printStackTrace();

       }
    }

    public boolean isOnline(Context context){
        try {

            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network nw = connectivityManager.getActiveNetwork();
                if (nw == null) return false;
                NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
                return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
            } else {
                NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
                return nwInfo != null && nwInfo.isConnected();
            }

        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public void  searchMovieApi(String query, int pageNumber){
        movieViewModel.searchMovieApi(query,pageNumber);
    }

    public void  searchTrendingMovieApi(int pageNumber){
        movieViewModel.searchTrendingMovieApi(pageNumber);
    }

    public void  searchNowPlayingMovieApi(int pageNumber){
        movieViewModel.searchNowPlayingMovieApi(pageNumber);
    }
}