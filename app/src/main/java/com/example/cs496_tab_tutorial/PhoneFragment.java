package com.example.cs496_tab_tutorial;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PhoneFragment extends Fragment {

    ListView mListView;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment1, null);
        mListView = (ListView)view.findViewById(R.id.phoneList);

        ArrayList<Person> m_orders = new ArrayList<Person>();

        AssetManager assetManager = getActivity().getAssets();

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

        PersonAdapter m_adapter = new PersonAdapter(getActivity(), R.layout.row, m_orders);
        mListView.setAdapter(m_adapter);
        return view;
    }
}
