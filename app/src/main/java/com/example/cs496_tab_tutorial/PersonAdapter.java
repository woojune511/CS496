package com.example.cs496_tab_tutorial;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{
    private ArrayList<Person> items;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;

        public ViewHolder(View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.toptext);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, PhoneSubActivity.class);
                    intent.putExtra("name", items.get(position).getName());
                    intent.putExtra("number", items.get(position).getNumber());
                    context.startActivity(intent);
                }
            });
        }

    }

    PersonAdapter(ArrayList<Person> list){
        items = list;
    }

    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row, parent, false);
        PersonAdapter.ViewHolder vh = new PersonAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(PersonAdapter.ViewHolder holder, int position){
        Person p = items.get(position);
        holder.textView1.setText(p.getName());
//        System.out.println("read name: " + p.getName());
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void addContext(Context context1){
        context = context1;
    }
}
//class PersonAdapter extends ArrayAdapter<Person> {
//
//    private ArrayList<Person> items;
//
//    public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> items) {
//        super(context, textViewResourceId, items);
//        this.items = items;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
//        if (v == null) {
//            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = vi.inflate(R.layout.row, null);
//        }
//        Person p = items.get(position);
//        if (p != null) {
//            TextView tt = (TextView) v.findViewById(R.id.toptext);
////            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
//            if (tt != null) {
//                tt.setText(p.getName());
//            }
////            if (bt != null) {
////                bt.setText(p.getNumber());
////            }
//        }
//        return v;
//    }
//
//}
