package com.example.moviedatabase;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviedatabase.model.Movie;
import com.example.moviedatabase.viewModel.MovieViewModel;



/*THIS IS MOVIE DETAILS ACTIVITY
* It will display overview of a movie and gives a option to make any movie favourite and that will be stored in the the DB
* for displaying fav movies
* */

public class MovieDetails extends AppCompatActivity  {


    private ImageView imageView,favorite;
    private TextView title,overview;
    private int movie_id;
    private MovieViewModel movieViewModel;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        imageView =  findViewById(R.id.imageView_details);
        favorite =  findViewById(R.id.favourite);
        title =  findViewById(R.id.title_details);
        overview =  findViewById(R.id.overview);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        GetDataFromIntent();     //getting data from intent
        if(!FavouriteCheck()){      // for displaying favorite button . If it is favourite it will show filled button else only bordered button
                favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
        else {
            favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        favorite.setOnClickListener(new View.OnClickListener() {   // Implementation of inserting and deleting favorite movies
            @Override
            public void onClick(View v) {
                if(!movieViewModel.isFavourite(movie_id)){
                    favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    movieViewModel.insert(movie);    // adding to favorite
                }
                else{
                    favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    movieViewModel.delete(movie); // deleting from favorite
                }
            }
        });

    }
    private boolean FavouriteCheck(){
        return movieViewModel.isFavourite(movie_id);
    }  // for checking if a movie is favorite or not

    public void GetDataFromIntent(){
        if(getIntent().hasExtra("movie")){            //getting data from intent
            movie =  getIntent().getParcelableExtra("movie");
            title.setText(movie.getOriginal_title());
            overview.setText(movie.getOverview());
            movie_id = movie.getMovie_id();
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movie.getPoster_path()).into(imageView);
            Log.v("details", "intent : " + movie.getOriginal_title());
        }
    }


}