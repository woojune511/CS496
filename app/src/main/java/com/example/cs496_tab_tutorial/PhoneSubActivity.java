package com.example.cs496_tab_tutorial;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PhoneSubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sub);

        Intent intent = new Intent(this.getIntent());
        final String name = intent.getStringExtra("name");
        final String number = intent.getStringExtra("number");

        TextView nametextview = (TextView) findViewById(R.id.nameTextView);
        TextView numtextview = (TextView) findViewById(R.id.numTextView);

        nametextview.setText(name);
        numtextview.setText(number);

        Button callButton = (Button) findViewById(R.id.CallButton);
        Button textButton = (Button) findViewById(R.id.TextButton);

        callButton.setOnClickListener(new Button.OnClickListener(){
           @Override
           public void onClick(View view){
               startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+number)));
           }
        });

        textButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent("android.intent.action.SENDTO", Uri.parse("sms:"+number)));
            }
        });
    }
}