package com.tyagiabhinav.imagesearch;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tyagiabhinav.imagesearch.util.executor.AppExecutor;
import com.tyagiabhinav.imagesearch.util.executor.DiskExecutors;

import java.util.concurrent.Executors;

public class ImageSearch extends Application {

    private static Context context;
    private static int THREAD_COUNT = 4;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }

    public static Context getContext() {
        return context;
    }

//    public static ImageDatabase getDatabase(){
//        return Room.databaseBuilder(context, ImageDatabase.class, "flickr_image.db")
//                .fallbackToDestructiveMigration()
//                .build();
//    }

    public static AppExecutor getAppExecutor() {
        return new AppExecutor(new DiskExecutors(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutor.MainThreadExecutor());
    }
}
