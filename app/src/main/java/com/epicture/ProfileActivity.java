package com.epicture;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicture.request.FavoritesCallback;
import com.epicture.request.ImgurAPI;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

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


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImgurAPI imgur;

    private static class Photo {
        String cover;
        String title;
        String ID;
        Boolean isAlbum;
    }

    private OkHttpClient httpClient,httpClient2;

    private void fetchData_all() {
        httpClient = new OkHttpClient.Builder().build();
        OAuth2Values values = LoginParameters.retrieveValues(this.getApplicationContext());
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/me/images")
                .header("Authorization", "Bearer " + values.getAccess_token())
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
                        ProfileActivity.Photo photo = new ProfileActivity.Photo();

                        photo.ID= item.getString("id");
                        photo.title = item.getString("title");

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

    private void fetchData_sub() {
        httpClient = new OkHttpClient.Builder().build();
        OAuth2Values values = LoginParameters.retrieveValues(this.getApplicationContext());
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/account/stam0325/submissions/0")
                .header("Authorization", "Bearer " + values.getAccess_token())
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
                        ProfileActivity.Photo photo = new ProfileActivity.Photo();

                        /*photo.ID= item.getString("cover");
                        photo.title = item.getString("title");*/

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
                        render2(photos);
                    }
                });
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        OAuth2Values values = LoginParameters.retrieveValues(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgur = new ImgurAPI(this);

        Button bUser = findViewById(R.id.buttonUser);
        Button bHome = findViewById(R.id.buttonHome);
        Button bUpload = findViewById(R.id.buttonUpload);
        Button bSearch = findViewById(R.id.buttonSearch);
        Button bFavorite = findViewById(R.id.buttonFav);

        Button ball = findViewById(R.id.all);
        Button bsub = findViewById(R.id.sub);


        bUser.setEnabled(false);
        bHome.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        bSearch.setOnClickListener(this);
        bFavorite.setOnClickListener(this);

        TextView header = findViewById(R.id.title);
        header.append(values.getAccount_username());


        fetchData_all();

        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData_all();
            }
        });

        bsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData_sub();
            }
        });


        /*Button settings = findViewById(R.id.bSettings);

        fetchData();

        Button settings = findViewById(R.id.bSettings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this , SettingActivity.class);
                startActivity(intent);
                finish();
            }

        });*/

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
            case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
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

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        Button fav;
        Button all;
        Button sub;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }

    private void render2(final List<Photo> photos) {
        final RecyclerView rv = findViewById(R.id.rv_of_photos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {
            @Override
            public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
                PhotoVH vh = new PhotoVH(getLayoutInflater().inflate(R.layout.item, null));
                vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
                vh.title = (TextView) vh.itemView.findViewById(R.id.title);
                vh.fav = (Button) vh.itemView.findViewById(R.id.buttonFavorite);
                vh.all = (Button) vh.itemView.findViewById(R.id.all);
                vh.sub = (Button) vh.itemView.findViewById(R.id.sub);

                return vh;
            }

            @Override
            public void onBindViewHolder(ProfileActivity.PhotoVH holder, final int position) {
                Picasso.get().load("https://i.imgur.com/" +
                        photos.get(position).cover + ".jpg").into(holder.photo);
                holder.title.setText(photos.get(position).title);

                holder.fav.setOnClickListener(new View.OnClickListener(){
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

                        httpClient2.newCall(request2).enqueue(new FavoritesCallback(ProfileActivity.this));
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

    private void render(final List<Photo> photos) {
        final RecyclerView rv = findViewById(R.id.rv_of_photos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {
            @Override
            public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
                PhotoVH vh = new PhotoVH(getLayoutInflater().inflate(R.layout.item, null));
                vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
                vh.title = (TextView) vh.itemView.findViewById(R.id.title);
                vh.fav = (Button) vh.itemView.findViewById(R.id.buttonFavorite);
                //vh.Add=(TextView)vh.itemView.findViewById(R.id.Add);
                //vh.Delete=(TextView)vh.itemView.findViewById(R.id.Delete);
                return vh;
            }

            @Override
            public void onBindViewHolder(ProfileActivity.PhotoVH holder, final int position) {
                Picasso.get().load("https://i.imgur.com/" +
                        photos.get(position).ID + ".jpg").into(holder.photo);
                holder.title.setText(photos.get(position).title);

                holder.fav.setOnClickListener(new View.OnClickListener(){
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

                        httpClient2.newCall(request2).enqueue(new FavoritesCallback(ProfileActivity.this));
                    }
                });
                //holder.ID.setText(photos.get(position).ID);
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
}