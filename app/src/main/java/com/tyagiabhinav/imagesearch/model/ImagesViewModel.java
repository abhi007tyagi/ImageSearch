package com.tyagiabhinav.imagesearch.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.tyagiabhinav.imagesearch.ImageSearch;
import com.tyagiabhinav.imagesearch.model.database.Images;

import java.util.List;

public class ImagesViewModel extends AndroidViewModel {

    private PhotoRepo photoRepo;

    public ImagesViewModel(Application application) {
        super(application);
        photoRepo = new PhotoRepo();
    }

    public LiveData<List<Images>> getPhotos(String category, int pageIndex) {
        return photoRepo.getPhotos(category, pageIndex);
    }
}