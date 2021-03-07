package com.example.moviedatabase.database;

import android.media.midi.MidiOutputPort;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moviedatabase.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE  FROM favourite_table")
    void deleteAllMovies();

    @Query(" Select * from favourite_table")
    LiveData<List<Movie>> getAllMovies();

    @Query("Select count(*) from  favourite_table where movie_id = :movie_id")
    boolean isFavourite(int movie_id);
}
