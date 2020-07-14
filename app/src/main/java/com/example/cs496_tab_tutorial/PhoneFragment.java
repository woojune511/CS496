package com.example.cs496_tab_tutorial;

import android.Manifest;
<<<<<<< HEAD
<<<<<<< HEAD


import android.content.ContentProviderOperation;


import android.app.ProgressDialog;

=======
import android.app.ProgressDialog;
>>>>>>> parent of a9c9479... 1st tab add new contact
=======
import android.app.ProgressDialog;
>>>>>>> parent of a9c9479... 1st tab add new contact
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
<<<<<<< HEAD
<<<<<<< HEAD


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.RemoteException;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;

=======
>>>>>>> parent of a9c9479... 1st tab add new contact
=======
>>>>>>> parent of a9c9479... 1st tab add new contact
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.JsonWriter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.provider.ContactsContract;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PhoneFragment extends Fragment {

    ListView mListView;
    ConstraintLayout cl;
    ImageButton addButton;
    ArrayList<Person> phoneBook;
    static final int REQUEST = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment1, null);

        cl = (ConstraintLayout) view.findViewById(R.id.ConstraintLayout);
        mListView = (ListView) view.findViewById(R.id.phoneList);
        addButton = (ImageButton) view.findViewById(R.id.AddButton);
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS,}, REQUEST);
                }
                else
                    showContacts();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //request permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS,}, REQUEST);
        }
        else{
            showContacts();
        }

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
                System.out.println("입력 받은 이름: "+newName+" 입력 받은 번호: "+newNum);
                phoneBook.add(new Person(newName, newNum));
            }

//            if (newName == null || newNum == null || newName.equals("") || newNum.equals(""))
//                return;
//
//            File appDir = getContext().getFilesDir();
//            File file = new File(appDir, "phoneNumber.json");
//
//            if (!file.exists()) {
//                try {
//                    JSONObject person = new JSONObject();
//                    person.put("name", newName);
//                    person.put("number", newNum);
//                    JSONArray phones = new JSONArray();
//                    phones.put(person);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
        }
    }

    public ArrayList<Person> getContacts(Context context){
        ArrayList<Person> datas = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();

        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

        if(cursor != null){
            while(cursor.moveToNext()){
                int idIndex = cursor.getColumnIndex(projection[0]); // 이름을 넣어주면 그 칼럼을 가져와준다.
                int nameIndex = cursor.getColumnIndex(projection[1]);
                int numberIndex = cursor.getColumnIndex(projection[2]);
                // 4.2 해당 index 를 사용해서 실제 값을 가져온다.
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);

                Person phoneBook = new Person();
                phoneBook.setId(id);
                phoneBook.setName(name);
                phoneBook.setNumber(number);

//                System.out.println("Name: "+name+" Number: "+number);
                datas.add(phoneBook);
            }
        }

        cursor.close();
        return datas;
    }

    public void showContacts(){
        phoneBook = getContacts(getActivity());

        final PersonAdapter m_adapter = new PersonAdapter(getActivity(), R.layout.row, phoneBook);
        mListView.setAdapter(m_adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PhoneSubActivity.class);
                intent.putExtra("name", phoneBook.get(position).getName());
                intent.putExtra("number", phoneBook.get(position).getNumber());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showContacts();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}

//        final ArrayList<Person> m_orders = new ArrayList<Person>();
//
//        AssetManager assetManager = getActivity().getAssets();
//
//        try{
//            InputStream is = assetManager.open("jsons/phoneNumber.json");
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader reader = new BufferedReader(isr);
//
//            StringBuffer buffer = new StringBuffer();
//            String line = reader.readLine();
//            while(line!=null){
//                buffer.append(line+"\n");
//                line = reader.readLine();
//            }
//
//            String jsonData = buffer.toString();
//            JSONObject jsonObject = new JSONObject(jsonData);
//            String phones = jsonObject.getString("phones");
//            JSONArray jsonArray = new JSONArray(phones);
//            System.out.println(jsonArray.length());
//            for(int i=0 ; i < jsonArray.length() ; i++) {
//                JSONObject subJsonObject = jsonArray.getJSONObject(i);
//                String name = subJsonObject.getString("name");
//                String number = subJsonObject.getString("number");
//
//                Person p = new Person(name, number);
//                m_orders.add(p);
//            }
//
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//
//        final PersonAdapter m_adapter = new PersonAdapter(getActivity(), R.layout.row, m_orders);
//        mListView.setAdapter(m_adapter);
//
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                Intent intent = new Intent(getActivity(), PhoneSubActivity.class);
//                intent.putExtra("name", m_orders.get(position).getName());
//                intent.putExtra("number", m_orders.get(position).getNumber());
//                startActivity(intent);
//            }
//        });
//
//        ImageButton addButton = (ImageButton) view.findViewById(R.id.AddButton);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), PopupActivity.class);
//                startActivityForResult(intent, 1);
//            }
//        });