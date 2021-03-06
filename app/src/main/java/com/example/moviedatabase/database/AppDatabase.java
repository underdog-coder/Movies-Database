package com.example.moviedatabase.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.moviedatabase.model.Movie;


/*Database instance class for room implementation*/

@Database(entities = Movie.class, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;  //instance for DB
    public static final String DATABASE_NAME = "favourite_table";  //table name

    public abstract MovieDao MovieDao();  //dao object

    public static synchronized AppDatabase getInstance(Context context) { // getting instance
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build(); // creating Db on first run
        }
        return instance;
    }

}
