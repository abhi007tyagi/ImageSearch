package com.tyagiabhinav.imagesearch.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.tyagiabhinav.imagesearch.ImageSearch;

/**
 * The Room Database that contains the Images table.
 */

@Database(entities = {Images.class}, version = 1, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {

    public abstract ImagesDAO imagesDao();
    private static ImageDatabase INSTANCE;

    public static ImageDatabase getDatabase() {
        if (INSTANCE == null) {
            synchronized (ImageDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ImageSearch.getContext(), ImageDatabase.class, "flickr_image.db")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}