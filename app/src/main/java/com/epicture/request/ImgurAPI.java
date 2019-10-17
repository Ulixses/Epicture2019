package com.epicture.request;

import android.app.Activity;
import android.util.Base64;
import com.epicture.LoginParameters;
import com.epicture.OAuth2Values;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ImgurAPI {
    public static String client_id = "ad42168a6373bd7";
    private Activity currentContext;
    OkHttpClient client;
    OAuth2Values values;

    public ImgurAPI(Activity currentcontext) {
        currentContext = currentcontext;
        values = LoginParameters.retrieveValues(currentcontext);
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
                    .header("Authorization", "Bearer " + values.getAccess_token())
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new PostImageCallback());
    }

    public void generateFavorites(){
        OAuth2Values values =  LoginParameters.retrieveValues(currentContext.getApplicationContext());
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/" + values.getAccount_username() + "/favorites")
                .header("Authorization", "Bearer " + values.getAccess_token())
                .build();

        client.newCall(request).enqueue(new FavoritesCallback(currentContext));
    }
}
