package com.example.cs496_tab_tutorial;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    public Integer[] thumbImages = new Integer[20];

    private Context Cont;

    public ImageAdapter(Context c) {
        Cont = c;
        Integer imgNumber = R.drawable.img1;
        for(int i=0 ; i<20 ; i++){
            thumbImages[i] = imgNumber++;
        }
    }

    @Override
    public int getCount() {
        return thumbImages.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imgView = new ImageView(Cont);
        imgView.setLayoutParams(new GridView.LayoutParams(370, 250));
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgView.setPadding(10, 10, 10, 10);
        imgView.setImageResource(thumbImages[i]);
        return imgView;
    }
}
