package com.example.cs496_tab_tutorial;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class PhotoActivity extends AppCompatActivity {
    ImageView mImageView;
    ViewPager mViewPager;
    SliderAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        Intent intent = getIntent();
        Integer pos = intent.getExtras().getInt("pos");
        mAdapter = new SliderAdapter(this);


        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(pos);

    }
}