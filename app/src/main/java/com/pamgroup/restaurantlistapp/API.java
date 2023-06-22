package com.pamgroup.restaurantlistapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pamgroup.restaurantlistapp.model.Emoji;

import org.json.JSONException;

import java.util.List;

public class API {
    private static API instance = null;
    private static final String URL = "https://emojihub.yurace.pro/api/random/category/food-and-drink";
    public static RequestQueue requestQueue;

    API(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized API getInstance(Context context) {
        if (null == instance) instance = new API(context);
        return instance;
    }

    public static synchronized API getInstance() {
        if (null == instance)
            throw new IllegalStateException(API.class.getSimpleName() + "belum diinisialisasi");
        return instance;
    }

    public interface DataCallback {
        void onDataReceived(Emoji emoji);

        void onError(Exception e);
    }


    public Emoji getEmoji(final DataCallback callback) {
        final Emoji emoji = new Emoji();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        String name = response.getString("name");
                        String unicode = response.getJSONArray("unicode").getString(0);

                        emoji.setName(name);
                        emoji.setUniCode(unicode);

                        callback.onDataReceived(emoji);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                },
                error -> {
                    callback.onError(new Exception(error.getMessage()));
                });

        requestQueue.add(jsonObjectRequest);

        return emoji;
    }

}
