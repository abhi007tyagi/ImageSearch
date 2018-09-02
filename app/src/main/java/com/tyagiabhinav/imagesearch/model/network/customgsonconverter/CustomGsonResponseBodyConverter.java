package com.tyagiabhinav.imagesearch.model.network.customgsonconverter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;


final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final String TAG = CustomGsonResponseBodyConverter.class.getSimpleName();

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        String resp = value.string();
//        Log.d(TAG, "Response: "+ resp);
        String jsonResponse = resp.substring(14, resp.length() - 1); // to remove "jsonFlickrApi(" and ")" from the response
        JsonReader jsonReader = gson.newJsonReader(new StringReader(jsonResponse));//value.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
}