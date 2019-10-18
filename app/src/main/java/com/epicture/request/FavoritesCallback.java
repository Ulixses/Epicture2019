package com.epicture.request;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class FavoritesCallback implements okhttp3.Callback{
    Activity context;
    public FavoritesCallback(Activity context) {
        this.context = context;
    }

    public FavoritesCallback() { }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        //Log.e("PostImageCallback", "onFailure");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //todo: generate the favorite recyclerView
        Log.d("PostImageCallback", "onResponse");
    }
}