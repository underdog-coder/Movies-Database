package com.example.moviedatabase.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedatabase.R;

public class TrendingMovieHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView title;
    ImageView movieImage;
    OnTrendingListener onMovieListener;
    public TrendingMovieHolder(@NonNull View itemView, OnTrendingListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;
        title = itemView.findViewById(R.id.title);
        movieImage = itemView.findViewById(R.id.movie_img);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onMovieListener.ontrendingMovieClick(getAdapterPosition());
    }
}
