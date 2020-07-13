package com.example.cs496_tab_tutorial;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.JsonWriter;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PhoneFragment extends Fragment {

    ListView mListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment1, null);

        mListView = (ListView)view.findViewById(R.id.phoneList);

        final ArrayList<Person> m_orders = new ArrayList<Person>();

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

        final PersonAdapter m_adapter = new PersonAdapter(getActivity(), R.layout.row, m_orders);
        mListView.setAdapter(m_adapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getActivity(), PhoneSubActivity.class);
                intent.putExtra("name", m_orders.get(position).getName());
                intent.putExtra("number", m_orders.get(position).getNumber());
                startActivity(intent);
            }
        });

        ImageButton addButton = (ImageButton) view.findViewById(R.id.AddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PopupActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String newName = "";
        String newNum = "";
        System.out.println("\n\n" + newName + "\n" + newNum + "\n\n");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                newName = data.getStringExtra("newName");
                newNum = data.getStringExtra("newNum");
            }

            if (newName == null || newNum == null || newName.equals("") || newNum.equals(""))
                return;

            File appDir = getContext().getFilesDir();
            File file = new File(appDir, "phoneNumber.json");

            if (!file.exists()) {
                try {
                    JSONObject person = new JSONObject();
                    person.put("name", newName);
                    person.put("number", newNum);
                    JSONArray phones = new JSONArray();
                    phones.put(person);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //2주차에 DB 할 때 만들자
        }
    }



}