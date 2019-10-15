package com.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String oauthURL = "https://api.imgur.com/oauth2/authorize?client_id=" + ImgurAPI.client_id + "&response_type=token";

        WebView web = findViewById(R.id.WebView);


        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new LoginClient(this));
        web.loadUrl(oauthURL);
    }
}
