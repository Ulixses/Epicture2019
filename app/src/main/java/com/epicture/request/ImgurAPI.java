package com.epicture.request;

import android.app.Activity;
import android.util.Base64;
import com.epicture.LoginParameters;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ImgurAPI {
    public static String client_id = "baaabc6b57cadba";
    private Activity currentContext;
    String access_token;
    OkHttpClient client;

    public ImgurAPI(Activity currentcontext) {
        currentContext = currentcontext;
        access_token = LoginParameters.retrieveValues(currentcontext).getAccess_token();
        client = new OkHttpClient();
    }

    public void uploadImage(byte[] file){
            String encodeImage = Base64.encodeToString(file, Base64.DEFAULT);
            //todo: add more part to the body(Title, description ...)
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", encodeImage)
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgur.com/3/upload")
                    .header("Authorization", "Client-ID " + client_id)
                    .header("Authorization", "Bearer " + access_token)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new PostImageCallback());
    }
}
