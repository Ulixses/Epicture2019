package com.epicture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epicture.request.ImgurAPI;
import androidx.appcompat.app.AppCompatActivity;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImgurAPI imgur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!LoginParameters.isLogged(this.getApplicationContext())){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        OAuth2Values values = LoginParameters.retrieveValues(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgur = new ImgurAPI(this);

        Button bUser = findViewById(R.id.buttonUser);
        Button bHome = findViewById(R.id.buttonHome);
        Button bUpload = findViewById(R.id.buttonUpload);
        Button bSearch = findViewById(R.id.buttonSearch);
        Button bFavorites = findViewById(R.id.buttonFav);

        bUser.setEnabled(false);
        bHome.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        bSearch.setOnClickListener(this);
        bFavorites.setOnClickListener(this);

        TextView textView = findViewById(R.id.title);
        textView.append(values.getAccount_username());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonHome:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonUpload:
                intent = new Intent(this, UploadActivity.class);
                startActivity(intent);
                finish();
                break;
            /*case R.id.buttonFav:
                intent = new Intent(this, UploadActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;*/
        }
    }
}