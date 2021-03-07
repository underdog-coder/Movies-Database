package com.example.moviedatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutor  {

    private static  AppExecutor instance;

    public static  AppExecutor getInstance(){
        if(instance == null){
            instance = new AppExecutor();
        }
        return instance;
    }

    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(5);

    public ScheduledExecutorService networkIO(){
            return mNetworkIO;
    }


}


