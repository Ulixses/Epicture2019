package com.epicture;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OAuth2Values values = LoginParameters.retrieveValues(this.getApplicationContext());
        if(values == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            values = LoginParameters.retrieveValues(this.getApplicationContext());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView header = findViewById(R.id.title);
        header.append(values.getAccount_username());
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}