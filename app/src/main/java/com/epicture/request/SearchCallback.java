package com.epicture.request;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epicture.LoginParameters;
import com.epicture.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchCallback implements okhttp3.Callback{
    Activity context;

    private static class Photo {
        String cover;
        String title;
        String ID;
        Boolean isAlbum;
    }

    private static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        Button fav;
        TextView ID;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }

    public SearchCallback(Activity context) {
        this.context = context;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        Log.e("SearchCallback", "onFailure");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.d("SearchCallback", "onResponse");
        final List<Photo> photos = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(response.body().string());
            JSONArray items = data.getJSONArray("data");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Photo photo = new Photo();
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
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                render(photos);
            }
        });
    }

    private void render(final List<Photo> photos) {
        final RecyclerView rv = context.findViewById(R.id.photos);
        rv.setLayoutManager(new LinearLayoutManager(context));

        RecyclerView.Adapter<PhotoVH> adapter = new RecyclerView.Adapter<PhotoVH>() {
            @Override
            public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
                PhotoVH vh = new PhotoVH(context.getLayoutInflater().inflate(R.layout.item, null));
                vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
                vh.title = (TextView) vh.itemView.findViewById(R.id.title);
                vh.fav = (Button) vh.itemView.findViewById(R.id.buttonFavorite);

                return vh;
            }

            @Override
            public void onBindViewHolder(PhotoVH holder, final int position) {
                Picasso.get().load("https://i.imgur.com/" +
                        photos.get(position).cover + ".jpg").into(holder.photo);
                holder.title.setText(photos.get(position).title);

                holder.fav.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        OkHttpClient client = new OkHttpClient.Builder().build();
                        RequestBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("image", "title")
                                .build();
                        String type = photos.get(position).isAlbum ? "album" : "image";
                        Request request = new Request.Builder()
                                .url("https://api.imgur.com/3/" + type + "/" + photos.get(position).ID + "/favorite")
                                .header("Authorization", "Bearer " + LoginParameters.retrieveValues(context.getApplicationContext()).getAccess_token())
                                .post(body)
                                .build();

                        client.newCall(request).enqueue(new FavoritesCallback(context));
                    }
                });
                holder.ID.setText(photos.get(position).ID);
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
