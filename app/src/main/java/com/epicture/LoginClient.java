package com.epicture;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;

public class LoginClient extends WebViewClient {
    private Activity activity;
    public LoginClient(Activity activity){
        this.activity = activity;
    }
    @Override
    public void onPageFinished (WebView view, String url){
        if(url.contains("epicture.com")){
            //todo: parse url to get access codes

            HashMap<String, String> parameters = new HashMap<>();
            String pieces = url.split("#")[1];
            for(String a : pieces.split("&")){
                String[] pair = a.split("=");
                parameters.put(pair[0], pair[1]);
            }
            String access_token = parameters.get("access_token");
            Long lifeTime = Long.parseLong(parameters.get("expires_in"));
            String token_type = parameters.get("token_type");
            String refresh_token = parameters.get("refresh_token");
            String account_username = parameters.get("account_username");
            String account_id = parameters.get("account_id");

            OAuth2Values values = new OAuth2Values(access_token, lifeTime, token_type, refresh_token, account_username, account_id);
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }

    }
}
