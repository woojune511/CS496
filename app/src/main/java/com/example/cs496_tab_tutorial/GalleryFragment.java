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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalleryFragment extends Fragment {
    public List<String> deleteList = new ArrayList<String>();
    Context mContext;
    ImageView myImage;
    TextView textView;
    ImageButton delButton;
    ImageButton addButton;
    int directory_size;
    static final int REQUEST = 0;
    static final int REQUEST2 = 0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void FragmentRefresh(){
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
        FragmentRefresh();
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

        ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        deleteList = imageAdapter.deleteImages;
        gridView.setAdapter(imageAdapter);
        delButton = view.findViewById(R.id.imageButton2);
        addButton = view.findViewById(R.id.imageButton);
        final int REQUEST_TAKE_PHOTO = 1;
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST2);
        }


        addButton.setOnClickListener(new View.OnClickListener() {
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

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteList.size()>0) {
                    System.out.println("have delete items");

                    try{
                        for(int i=0 ; i < deleteList.size() ; i++) {
                            File tmpFile = new File(deleteList.get(i));
                            if(tmpFile.exists()) {
                                //해당 경로에 파일 존재하는지 확인
                                tmpFile.delete();
                                FragmentRefresh();
//                                Intent intent = new Intent(getActivity(), MainActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putInt("item_num", 1);
//                                intent.putExtras(bundle);
//                                getActivity().startActivity(intent);
                            }
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("no delete items");
                }
            }
        });

        return view;
    }






}
