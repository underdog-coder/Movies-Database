package com.example.moviedatabase.database;

import android.media.midi.MidiOutputPort;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moviedatabase.model.Movie;



/*this interface represent DAO and is used  for querying data base*/
import java.util.List;

@Dao // annotaion for room DB
public interface MovieDao {
    @Insert   //insert query
    void insert(Movie movie);

    @Update //update query
    void update(Movie movie);

    @Delete //delete query
    void delete(Movie movie);

    @Query("DELETE  FROM favourite_table") //delete all  query
    void deleteAllMovies();

    @Query(" Select * from favourite_table") // getting all movies
    LiveData<List<Movie>> getAllMovies();

    @Query("Select count(*) from  favourite_table where movie_id = :movie_id") // for getting if movie exist in db or not
    boolean isFavourite(int movie_id);
}
