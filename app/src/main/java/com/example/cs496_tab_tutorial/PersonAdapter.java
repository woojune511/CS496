package com.example.cs496_tab_tutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
