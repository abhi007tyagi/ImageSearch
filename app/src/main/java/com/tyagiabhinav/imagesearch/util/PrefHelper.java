package com.tyagiabhinav.imagesearch.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tyagiabhinav.imagesearch.ImageSearch;
import com.tyagiabhinav.imagesearch.R;
import com.tyagiabhinav.imagesearch.model.network.RetrofitClient;

public class PrefHelper {


    private static final String TAG = PrefHelper.class.getSimpleName();


    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;


    private static PrefHelper instance = null;

    public static synchronized PrefHelper getInstance() {
        if (instance == null) {
            instance = new PrefHelper(PreferenceManager.getDefaultSharedPreferences(ImageSearch.getContext()));
        }
        return instance;
    }

    private PrefHelper(@NonNull SharedPreferences pref) {
        this.prefs = pref;
        editor = prefs.edit();
        editor.apply();
        Log.d(TAG, "PrefHelper initialized");
    }


    /**
     * Set grid columns
     */
    public void setGridColumns(int columns) {
        editor.putInt(ImageSearch.getContext().getString(R.string.columns), columns);
        editor.commit();
        Log.d(TAG, "setGridColumns: "+columns);
    }

    /**
     * Get saved grid columns
     *
     * @return String
     */
    public int getGridColumns() {
        return prefs.getInt(ImageSearch.getContext().getString(R.string.columns), 2);
    }

}