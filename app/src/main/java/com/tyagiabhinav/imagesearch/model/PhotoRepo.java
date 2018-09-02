package com.tyagiabhinav.imagesearch.model;

import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tyagiabhinav.imagesearch.BuildConfig;
import com.tyagiabhinav.imagesearch.ImageSearch;
import com.tyagiabhinav.imagesearch.model.database.ImageDatabase;
import com.tyagiabhinav.imagesearch.model.database.Images;
import com.tyagiabhinav.imagesearch.model.database.ImagesDAO;
import com.tyagiabhinav.imagesearch.model.network.PhotoSearchAPI;
import com.tyagiabhinav.imagesearch.model.network.RetrofitClient;
import com.tyagiabhinav.imagesearch.model.pojo.FlickrAPI;
import com.tyagiabhinav.imagesearch.model.pojo.Photo;
import com.tyagiabhinav.imagesearch.util.Utils;
import com.tyagiabhinav.imagesearch.util.executor.AppExecutor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoRepo {

    private static final String TAG = PhotoRepo.class.getSimpleName();
    private ImagesDAO imagesDAO;
    private AppExecutor appExecutor = ImageSearch.getAppExecutor();


    PhotoRepo() {
        ImageDatabase db = ImageDatabase.getDatabase();
        imagesDAO = db.imagesDao();
    }

    public LiveData<List<Images>> getPhotos(String category, int pageIndex) {
        if (Utils.isOnline()) {
            getPhotosFromService(category, pageIndex);
            return getPhotosFromDB(category);
        } else {
            return getPhotosFromDB(category);
        }
    }

    private void getPhotosFromService(String category, int pageIndex) {
        Log.d(TAG, "getPhotosFromService");
        Runnable saveRunnable = () -> {
            PhotoSearchAPI photoSearchAPI = RetrofitClient.getInstance().getRetrofit();
            Call<FlickrAPI> call = photoSearchAPI.getAllPhotos(BuildConfig.ApiKey, category, pageIndex);
            call.enqueue(new Callback<FlickrAPI>() {
                @Override
                public void onResponse(Call<FlickrAPI> call, Response<FlickrAPI> response) {
                    FlickrAPI flickrAPI = response.body();
                    Log.d(TAG, "onResponse for page: " + pageIndex);

                    List<Photo> photoList = flickrAPI.getPhotos().getPhoto();

                    List<Images> imageList = new ArrayList<>(photoList.size());

                    for (Photo photo : photoList) {
                        StringBuilder imageUrlBuilder = new StringBuilder();
                        imageUrlBuilder.append("https://farm")
                                .append(photo.getFarm())
                                .append(".staticflickr.com/")
                                .append(photo.getServer())
                                .append("/")
                                .append(photo.getId())
                                .append("_")
                                .append(photo.getSecret());

                        Images image = new Images(imageUrlBuilder.toString(), category);
                        imageList.add(image);
                    }

                    savePhotosToDB(imageList);

                }

                @Override
                public void onFailure(Call<FlickrAPI> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    t.printStackTrace();
                }
            });
        };
        appExecutor.diskIO().execute(saveRunnable);
    }

    private LiveData<List<Images>> getPhotosFromDB(String category) {
        return imagesDAO.getImages(category);
    }

    private void savePhotosToDB(List<Images> imageList) {
        Runnable saveRunnable = () -> {
            ImageDatabase db = ImageDatabase.getDatabase();
            ImagesDAO imagesDAO = db.imagesDao();
            int insertedCount = 0;
            for (Images images : imageList) {
                try {
                    imagesDAO.insert(images);
                    insertedCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "savePhotosToDB: " + insertedCount);
        };
        appExecutor.diskIO().execute(saveRunnable);
    }
}
