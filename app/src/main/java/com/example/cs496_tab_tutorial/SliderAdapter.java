package com.example.cs496_tab_tutorial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter {

    public List<String> images = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context Cont;
    ImageButton delButton;
    ImageView imageView;

    File storageDir;

    //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
    //myImage.setImageBitmap(myBitmap);

    public SliderAdapter(Context context) {
        Cont = context;
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles();
        int directory_size = files.length;
        for(int i=0 ; i<directory_size ; i++){
            images.add(storageDir + "/"+files[i].getName());
        }
    }

    @Override
    public int getCount() {
        return images.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        inflater = (LayoutInflater)Cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);

        delButton = (ImageButton)v.findViewById(R.id.imageButton);
        imageView = (ImageView)v.findViewById(R.id.imageView);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap myBitmap = BitmapFactory.decodeFile(images.get(position), bmOptions);
        imageView.setImageBitmap(myBitmap);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 1200);

        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(10, 10, 10, 10);
        container.addView(v);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    File tmpFile = new File(images.get(position));
                    if(tmpFile.exists()) {
                        //해당 경로에 파일 존재하는지 확인
                        tmpFile.delete();
                        Intent intent = new Intent(Cont, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("item_num", 1);
                        intent.putExtras(bundle);
                        Cont.startActivity(intent);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }


            }
        });


        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }



}
