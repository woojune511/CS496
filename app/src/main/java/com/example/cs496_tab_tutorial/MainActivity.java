package com.example.cs496_tab_tutorial;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    class PersonAdapter extends ArrayAdapter<Person> {

        private ArrayList<Person> items;

        public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            Person p = items.get(position);
            if (p != null) {
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                if (tt != null) {
                    tt.setText(p.getName());
                }
                if (bt != null) {
                    bt.setText("전화번호: " + p.getNumber());
                }
            }
            return v;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        TabLayout.Tab phoneTab = tabLayout.newTab();
        phoneTab.setText("연락처");
        tabLayout.addTab(phoneTab);

        TabLayout.Tab galleryTab = tabLayout.newTab();
        galleryTab.setText("갤러리");
        tabLayout.addTab(galleryTab);

        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("세번째탭");
        tabLayout.addTab(thirdTab);


        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        onTabSelectedListener.onTabSelected(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()));
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);


    }

    private void changeView(int pos) {
        ListView listview = (ListView) findViewById(R.id.phoneList);

        switch (pos) {
            case 0 :
                listview.setVisibility(View.VISIBLE);
                ArrayList<Person> m_orders = new ArrayList<Person>();

                AssetManager assetManager = getAssets();

                try{
                    InputStream is = assetManager.open("jsons/phoneNumber.json");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while(line!=null){
                        buffer.append(line+"\n");
                        line = reader.readLine();
                    }

                    String jsonData = buffer.toString();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String phones = jsonObject.getString("phones");
                    JSONArray jsonArray = new JSONArray(phones);
                    System.out.println(jsonArray.length());
                    for(int i=0 ; i < jsonArray.length() ; i++) {
                        JSONObject subJsonObject = jsonArray.getJSONObject(i);
                        String name = subJsonObject.getString("name");
                        String number = subJsonObject.getString("number");

                        Person p = new Person(name, number);
                        m_orders.add(p);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                PersonAdapter m_adapter = new PersonAdapter(getApplicationContext(), R.layout.row, m_orders);
                listview.setAdapter(m_adapter);
                break;
            case 1 :
                listview.setVisibility(View.INVISIBLE);
                break;
            case 2 :
                listview.setVisibility(View.INVISIBLE);

        }
    }
}