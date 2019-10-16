package com.epicture.request;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class PostImageCallback implements okhttp3.Callback {

    public PostImageCallback() {
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("PostImageCallback", "onFailure");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //todo: The image is posted, what now?
        Log.d("PostImageCallback", "onResponse");
    }
}
