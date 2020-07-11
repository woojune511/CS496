package com.example.cs496_tab_tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ThirdFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment3, null);

        Button OnOffButton = (Button) view.findViewById(R.id.OnOffButton);
        Button NotifButton = (Button) view.findViewById(R.id.NotifButton);

        OnOffButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

        NotifButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });
        
        return view;
    }
}
