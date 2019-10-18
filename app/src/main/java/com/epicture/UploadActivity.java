package com.epicture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.epicture.request.ImgurAPI;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    Button send;
    Button select;
    Uri selectedUri;
    Boolean slectionMade = false;
    ImgurAPI imgur;

    private static final int SELECT_CODE = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imgur = new ImgurAPI(this);

        Button bUser = findViewById(R.id.buttonUser);
        Button bHome = findViewById(R.id.buttonHome);
        Button bFavorite = findViewById(R.id.buttonFav);
        Button bSearch = findViewById(R.id.buttonSearch);
        findViewById(R.id.buttonUpload).setEnabled(false);

        bUser.setOnClickListener(this);
        bHome.setOnClickListener(this);
        bFavorite.setOnClickListener(this);
        bSearch.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
        send = findViewById(R.id.buttonSend);
        select = findViewById((R.id.buttonSelect));

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

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
            case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonUser:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonFav:
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void selectImage(){
        Intent selector = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(selector, SELECT_CODE);
    }

    private void uploadImage(){
        if(slectionMade == true){
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream dummyBytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, dummyBytes);
            byte[] image = dummyBytes.toByteArray();
            imgur.uploadImage(image);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_CODE) {
            selectedUri = data.getData();
            imageView.setImageURI(selectedUri);
            slectionMade = true;
        }
    }
}
