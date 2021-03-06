package com.example.cs496_tab_tutorial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PagerAdapter mPagerAdapter;
    static final int REQUEST2 = 0;

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
//                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                if (tt != null) {
                    tt.setText(p.getName());
                }
//                if (bt != null) {
//                    bt.setText(p.getNumber());
//                }
            }
            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        TabLayout.Tab phoneTab = mTabLayout.newTab();
        phoneTab.setText("연락처");
        mTabLayout.addTab(phoneTab);

        TabLayout.Tab galleryTab = mTabLayout.newTab();
        galleryTab.setText("갤러리");
        mTabLayout.addTab(galleryTab);

        TabLayout.Tab thirdTab = mTabLayout.newTab();
        thirdTab.setText("아맞다!우산");
        mTabLayout.addTab(thirdTab);

        mViewPager = (ViewPager) findViewById(R.id.pager_content);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            int tab_num = bundle.getInt("item_num");
            System.out.println("num : "+tab_num);
            mViewPager.setCurrentItem(tab_num);
        } else{
            System.out.println("bundle null!");
        }


        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                mViewPager.setCurrentItem(pos);
                if(pos==1) {
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST2);
                    }

                } else if(pos==2){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST2);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos==1) {
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST2);
                    }

                } else if(pos==2){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST2);
                    }
                }

            }
        };
        onTabSelectedListener.onTabSelected(mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()));
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

//    @Override
//    protected void onNewIntent(Intent intent){
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }

/*
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
 */
}