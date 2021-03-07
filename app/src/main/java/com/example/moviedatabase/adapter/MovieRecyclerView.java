package com.example.moviedatabase.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviedatabase.MovieDetails;
import com.example.moviedatabase.R;
import com.example.moviedatabase.model.Movie;

import java.util.ArrayList;
import java.util.List;


/*Recycler view holder for Movie search results*/

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> mMovies; // list of movies in the view
    private OnMovieListener onMovieListener; // onclick listener
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false); // inflating movie object
        return new MovieViewHolder(view,onMovieListener); //adding listener
    }

    public MovieRecyclerView(OnMovieListener onMovieListener) { // Listener on Movie
        this.onMovieListener = onMovieListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { // putting data  at every position
        ((MovieViewHolder )holder).title.setText(mMovies.get(position).getOriginal_title());
        Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500/"+ mMovies.get(position).getPoster_path()).into(((MovieViewHolder)holder).movieImage);
    }

    @Override
    public int getItemCount() { // getting item count of list

        if(mMovies != null){
            Log.v("set movies" , "movie size : " + mMovies.size());
            return mMovies.size();
        }
        else
            return 0;
    }

    public void setmMovies(List<Movie> mMovies) { // setting movies in recycler view which  it gets from view model
        if(mMovies != null){
            this.mMovies = (mMovies);
            notifyDataSetChanged();
            Log.v("set movies", "movie size : " + mMovies.toString());
        }


    }

    public void setOnMovieListener(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    public Movie getSelectedMovie(int position){   // returns the movie at position position
        if(mMovies !=  null){
            if(mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
        return null;
    }
}
