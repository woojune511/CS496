package com.example.cs496_tab_tutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class PopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        final EditText nameInput = (EditText) findViewById(R.id.NameInput);
        final EditText numInput=  (EditText) findViewById(R.id.NumInput);

        final Button confirmButton = (Button) findViewById(R.id.ConfirmButton);
        final Button cancelButton = (Button) findViewById(R.id.CancelButton);

        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("newName", nameInput.getText().toString());
                intent.putExtra("newNum", numInput.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                //입력된 정보를 json 파일에 덧쓰고,
                //연락처 fragment에 추가된 연락처가 보이게 하기
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        return;
    }
}