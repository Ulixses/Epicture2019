package com.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.epicture.request.ImgurAPI;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener {

    ImgurAPI imgur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        imgur = new ImgurAPI(this);
        imgur.generateFavorites();

        Button bUser = findViewById(R.id.buttonUser);
        Button bHome = findViewById(R.id.buttonHome);
        Button bUpload = findViewById(R.id.buttonUpload);
        Button bSearch = findViewById(R.id.buttonSearch);
        findViewById(R.id.buttonFav).setEnabled(false);

        bUser.setOnClickListener(this);
        bHome.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        bSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonUpload:
                intent = new Intent(this, UploadActivity.class);
                startActivity(intent);
                finish();
                break;
            /*case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;*/
            case R.id.buttonUser:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonHome:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
