package com.tyagiabhinav.imagesearch.model.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tyagiabhinav.imagesearch.ImageSearch;
import com.tyagiabhinav.imagesearch.model.network.customgsonconverter.CustomGsonConverterFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.flickr.com";

    private static RetrofitClient instance = null;

    private RetrofitClient() {
        // Exists only to defeat instantiation.
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public PhotoSearchAPI getRetrofit() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(ImageSearch.getContext().getCacheDir(), cacheSize);

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .cache(cache)
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .client(okHttpClient)
                .addConverterFactory(CustomGsonConverterFactory.create(gson))
                .build();

        return retrofit.create(PhotoSearchAPI.class);

    }
}
