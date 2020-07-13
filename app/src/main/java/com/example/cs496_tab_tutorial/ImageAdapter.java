package com.example.cs496_tab_tutorial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    public List<String> thumbImages = new ArrayList<String>();

    private Context Cont;
    File storageDir;

    public ImageAdapter(Context c) {
        Cont = c;
        //Integer imgNumber = R.drawable.img1;
        storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles();
        int directory_size = files.length;
        for(int i=0 ; i<directory_size ; i++){
            thumbImages.add(storageDir + "/"+files[i].getName());
        }
    }

    @Override
    public int getCount() {
        return thumbImages.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ImageView imgView = new ImageView(Cont);
        imgView.setLayoutParams(new GridView.LayoutParams(370, 370));
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgView.setPadding(1, 1, 1, 1);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap myBitmap = BitmapFactory.decodeFile(thumbImages.get(i), bmOptions);
        imgView.setImageBitmap(myBitmap);
        this.notifyDataSetChanged();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Cont, Integer.toString(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Cont, PhotoActivity.class);
                intent.putExtra("pos",i);
                Cont.startActivity(intent);
                storageDir = Cont.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File[] files = storageDir.listFiles();
                int directory_size = files.length; // 실제 경로에 있는 파일 수

                if(thumbImages.size() != directory_size) {
                    //만약 실제 경로의 파일 수와, 내 local 파일 수가 다르다면, 삭제를 했다고 가정
                    thumbImages.remove(i);
                    notifyDataSetChanged();
                }

            }
        });
        return imgView;
    }
}
