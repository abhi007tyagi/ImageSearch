package com.tyagiabhinav.imagesearch.model.network;

import com.tyagiabhinav.imagesearch.model.pojo.FlickrAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotoSearchAPI {
    @GET("/services/rest/?method=flickr.photos.search&format=json")
    Call<FlickrAPI> getAllPhotos(
            @Query("api_key") String apiKey,
            @Query("text") String searchText,
            @Query("page ") int pageIndex);//, @Query("api_key") String key);
}
