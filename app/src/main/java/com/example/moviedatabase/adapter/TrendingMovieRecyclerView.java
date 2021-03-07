package com.example.moviedatabase.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviedatabase.R;
import com.example.moviedatabase.model.Movie;

import java.util.List;


/*recycler view adapter to store trending movie*/
public class TrendingMovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Movie> mMovies;  // list of movies
    private OnTrendingListener onMovieListener; // listener
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  //creating view holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false); // inflating view
        return new TrendingMovieHolder(view,onMovieListener);
    }

    public TrendingMovieRecyclerView(OnTrendingListener onMovieListener) {
        this.onMovieListener = onMovieListener;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { // putting data in view holder
        ((TrendingMovieHolder )holder).title.setText(mMovies.get(position).getOriginal_title());
        Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500/"+ mMovies.get(position).getPoster_path()).into(((TrendingMovieHolder)holder).movieImage);
    }

    @Override
    public int getItemCount() { // getting count of list

        if(mMovies != null){
            Log.v("set movies" , "movie size : " + mMovies.size());
            return mMovies.size();
        }
        else
            return 0;
    }

    public void setmMovies(List<Movie> mMovies) { // setting movie in the list
        this.mMovies = (mMovies);
        notifyDataSetChanged();
        Log.v("set movies" , "movie size : " + mMovies.toString());
    }

    public void setOnMovieListener(OnTrendingListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    public Movie getSelectedMovie(int position){  // getting selected movie
        if(mMovies !=  null){
            if(mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
        return null;
    }
}
