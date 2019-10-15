package com.example.logindummy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //todo: create class with client id
        String oauthURL = "https://api.imgur.com/oauth2/authorize?client_id=baaabc6b57cadba&response_type=token";

        WebView web = findViewById(R.id.WebView);


        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new LoginClient(this));
        web.loadUrl(oauthURL);
    }
}
