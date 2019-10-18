package com.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;

import com.epicture.request.ImgurAPI;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    ImgurAPI imgur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imgur = new ImgurAPI(this);
        Button bUser = findViewById(R.id.buttonUser);
        Button bHome = findViewById(R.id.buttonHome);
        Button bFavorite = findViewById(R.id.buttonFav);
        Button bUpload = findViewById(R.id.buttonUpload);
        findViewById(R.id.buttonSearch).setEnabled(false);

        bUser.setOnClickListener(this);
        bHome.setOnClickListener(this);
        bFavorite.setOnClickListener(this);
        bUpload.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Todo: add search results
                closeKeyBoard();
                if(!query.equals("")){
                    imgur.generateSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void closeKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
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
            case R.id.buttonFav:
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
