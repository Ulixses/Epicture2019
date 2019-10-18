package com.epicture;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epicture.request.FavoritesCallback;
import com.epicture.request.ImgurAPI;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener{

    private ImgurAPI imgur;
    private static class Photo {
        String cover;
        String title;
        String ID;
        Boolean isAlbum;
    }

    private OkHttpClient httpClient,httpClient2;

    private void fetchData() {
        httpClient = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/me/favorites")
                .header("Authorization", "Bearer " + LoginParameters.retrieveValues(this.getApplicationContext()).getAccess_token())
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                final List<Photo> photos = new ArrayList<>();
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONArray items = data.getJSONArray("data");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        FavoriteActivity.Photo photo = new FavoriteActivity.Photo();
                        if (item.getBoolean("is_album")) {
                            photo.isAlbum = true;
                            photo.cover = item.getString("cover");
                        } else {
                            photo.isAlbum = false;
                            photo.cover = item.getString("id");
                        }
                        photo.title = item.getString("title");
                        photo.ID= item.getString("id");

                        photos.add(photo); // Add photo to list
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        render(photos);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!LoginParameters.isLogged(this.getApplicationContext())){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        OAuth2Values values = LoginParameters.retrieveValues(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        imgur = new ImgurAPI(this);

        Button bUser = findViewById(R.id.buttonUser);
        Button bHome = findViewById(R.id.buttonHome);
        Button bUpload = findViewById(R.id.buttonUpload);
        Button bSearch = findViewById(R.id.buttonSearch);
        Button bFavorites = findViewById(R.id.buttonFav);

        bFavorites.setEnabled(false);
        bHome.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        bSearch.setOnClickListener(this);
        bUser.setOnClickListener(this);

        fetchData();
    }

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        Button Del;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }

    private void render(final List<Photo> photos) {
        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv_of_photos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {
            @Override
            public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
                PhotoVH vh = new PhotoVH(getLayoutInflater().inflate(R.layout.item_fav, null));
                vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
                vh.title = (TextView) vh.itemView.findViewById(R.id.title);
                vh.Del = (Button) vh.itemView.findViewById(R.id.Delete);
                return vh;
            }

            @Override
            public void onBindViewHolder(PhotoVH holder, final int position) {
                Picasso.get().load("https://i.imgur.com/" +
                        photos.get(position).cover + ".jpg").into(holder.photo);
                holder.title.setText(photos.get(position).title);

                holder.Del.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        httpClient2= new OkHttpClient.Builder().build();
                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("image", "title")
                                .build();
                        String type = photos.get(position).isAlbum ? "album" : "image";
                        Request request2 = new Request.Builder()
                                .url("https://api.imgur.com/3/" + type + "/" + photos.get(position).ID + "/favorite")
                                .header("Authorization", "Bearer " + LoginParameters.retrieveValues(getApplicationContext()).getAccess_token())
                                .post(body)
                                .build();

                        httpClient2.newCall(request2).enqueue(new FavoritesCallback(FavoriteActivity.this));
                        fetchData();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return photos.size();
            }
        };

        rv.setAdapter(adapter);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16; // Gap of 16px
            }
        });
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
            case R.id.buttonUser:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

