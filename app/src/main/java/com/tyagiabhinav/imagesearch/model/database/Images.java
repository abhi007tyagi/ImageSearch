package com.tyagiabhinav.imagesearch.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

@Entity(tableName = "images")
public class Images {


    @NonNull
    @ColumnInfo(name = "category")
    private final String category;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "url")
    private final String url;


    /**
     * Use this constructor to create a new Course.
     */
    public Images(@NonNull String url, @NonNull String category) {
        this.url = url;
        this.category = category;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Images images = (Images) o;
        return Objects.equals(category, images.category) &&
                Objects.equals(url, images.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url + category);
    }

    @Override
    public String toString() {
        return url;
    }
}