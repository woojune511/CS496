package com.example.cs496_tab_tutorial;

import android.content.Context;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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


        class Person {
            private String Name;
            private String Number;

            public Person(String _Name, String _Number){
                this.Name = _Name;
                this.Number = _Number;
            }
            public String getName() {
                return Name;
            }
            public String getNumber() {
                return Number;
            }
        }


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


        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0){
                    ArrayList<Person> m_orders = new ArrayList<Person>();

                    Person p1 = new Person("안드로이드", "011-123-4567"); // 리스트에 추가할 객체입니다.
                    Person p2 = new Person("구글", "02-123-4567"); // 리스트에 추가할 객체입니다.

                    m_orders.add(p1); // 리스트에 객체를 추가합니다.
                    m_orders.add(p2);

                    PersonAdapter m_adapter = new PersonAdapter(getApplicationContext(), R.layout.row, m_orders);

                    ListView listview = (ListView) findViewById(R.id.phoneList);
                    listview.setAdapter(m_adapter);
                }
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
}