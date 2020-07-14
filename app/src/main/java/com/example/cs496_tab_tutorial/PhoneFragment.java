package com.example.cs496_tab_tutorial;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;


import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    RecyclerView mRecyclerView;
    ConstraintLayout cl;
    ImageButton addButton;
    ArrayList<Person> phoneBook;
    static final int REQUEST = 0;
    static final int REQUEST2 = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment1, null);

        cl = (ConstraintLayout) view.findViewById(R.id.ConstraintLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.phoneList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecyclerView.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                System.out.println("SWIPED");
//                System.out.println("ID: "+ phoneBook.get(viewHolder.getAdapterPosition()).getId());
                deleteContact(getContext(), Long.parseLong(phoneBook.get(viewHolder.getAdapterPosition()).getId()));
                refresh();
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);

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

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CONTACTS,}, REQUEST2);
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
//        System.out.println("\n\n" + newName + "\n" + newNum + "\n\n");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                newName = data.getStringExtra("newName");
                newNum = data.getStringExtra("newNum");
//                System.out.println("입력 받은 이름: "+newName+" 입력 받은 번호: "+newNum);

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CONTACTS,}, REQUEST2);
                }
                else
                    addContact(newName, newNum);
                    refresh();
                //phoneBook.add(new Person(newName, newNum));
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

                Person phoneBook = new Person(name, number, id);

//                System.out.println("Name: "+name+" Number: "+number +"Id : " + id);
                datas.add(phoneBook);
            }
        }

        cursor.close();
        return datas;
    }

    public void showContacts(){
        phoneBook = getContacts(getActivity());

//        System.out.println("number of contacts: "+phoneBook.size());

        final PersonAdapter m_adapter = new PersonAdapter(phoneBook);
        m_adapter.addContext(getActivity());
        mRecyclerView.setAdapter(m_adapter);

//        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), PhoneSubActivity.class);
//                intent.putExtra("name", phoneBook.get(position).getName());
//                intent.putExtra("number", phoneBook.get(position).getNumber());
//                startActivity(intent);
//            }
//        });
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

    public void addContact(final String newName, final String newNum){
        new Thread(){
            @Override
            public void run() {

                ArrayList<ContentProviderOperation> list = new ArrayList<>();
                try{
                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                    .build()
                    );

                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newName)   //이름

                                    .build()
                    );

                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNum)           //전화번호
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE  , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)

                                    .build()
                    );

//                    list.add(
//                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
//                                    .withValue(ContactsContract.CommonDataKinds.Email.DATA  , "hong_gildong@naver.com")  //이메일
//                                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE  , ContactsContract.CommonDataKinds.Email.TYPE_WORK)     //이메일타입(Type_Work : 직장)
//
//                                    .build()
//                    );

                    getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);  //주소록추가
                    list.clear();   //리스트 초기화
                }catch(RemoteException e){
                    e.printStackTrace();
                }catch(OperationApplicationException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    static public void deleteContact(Context context, long contactId){
//    static public void deleteContact(Context context, String name){
        context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,
//                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=" + name, null);
                ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null);
    }

    private void refresh(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
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