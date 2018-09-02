package com.tyagiabhinav.imagesearch.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ImagesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Images images);

    @Update
    void update(Images... images);

    @Delete
    void delete(Images... images);

    @Query("SELECT * FROM images WHERE category = :category")
    LiveData<List<Images>> getImages(String category);
}