package com.example.moviedatabase;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/*This is the executor class to execute task on background threads  such as api call and DB querying*/
public class AppExecutor  {

    private static  AppExecutor instance; // instance of executor
    private final Executor mDiskIO = Executors.newSingleThreadExecutor();   //  for IO  on Disk
    private final Executor mMainThreadExecutor = new MainThreadExecutor();

    public static  AppExecutor getInstance(){       // Singleton pattern implementation
        if(instance == null){
            instance = new AppExecutor();
        }
        return instance;
    }

    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(5);

    public ScheduledExecutorService networkIO(){
            return mNetworkIO;
    } // for network calls

    public Executor diskIO(){
        return mDiskIO;
    }  //for disk Io

    public Executor mainThread(){
        return mMainThreadExecutor;
    }

    private static class MainThreadExecutor implements Executor{

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}


