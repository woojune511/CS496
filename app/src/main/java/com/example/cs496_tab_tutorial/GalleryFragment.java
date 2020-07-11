package com.example.cs496_tab_tutorial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GalleryFragment extends Fragment {
    Context mContext;
    ImageView myImage;
    TextView textView;
    int directory_size;
    static final int REQUEST = 0;
    static final int REQUEST2 = 0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void refresh(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.detach(this).attach(this).commit();
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles();
        directory_size = files.length;
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println("directory : "+storageDir);
        refresh();
        return image;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment2, null);
        textView = view.findViewById(R.id.noImages);
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles();
        directory_size = files.length;
        System.out.println(directory_size);
        if(directory_size>0){
            textView.setVisibility(View.INVISIBLE);
        }
        GridView gridView = view.findViewById(R.id.gridView);

        gridView.setAdapter(new ImageAdapter(getActivity()));

        ImageButton imgButton = view.findViewById(R.id.imageButton);
        final int REQUEST_TAKE_PHOTO = 1;
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST2);
        }


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,}, REQUEST);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) !=null) {
                        //Create file where the photo should go
                        File photoFile = null;
                        try {

                            photoFile = createImageFile();

                        } catch (IOException ex){
                            ex.printStackTrace();
                        }

                        if(photoFile != null) {


                            Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            File f = new File(mCurrentPhotoPath);
                            Uri contentUri = Uri.fromFile(f);
                            mediaScanIntent.setData(contentUri);
                            getActivity().sendBroadcast(mediaScanIntent);

                        }
                    }

                }

            }
        });

        return view;
    }






}
