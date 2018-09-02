package com.tyagiabhinav.imagesearch.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tyagiabhinav.imagesearch.ImageSearch;

public class Utils {

    public static boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) ImageSearch.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
