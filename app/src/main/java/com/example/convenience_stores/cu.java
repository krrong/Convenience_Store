package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class cu extends AppCompatActivity {
    Button rtn_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu);

        rtn_btn = findViewById(R.id.rtn_btn);
        rtn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        
        ListView listView = findViewById(R.id.cu_listview);                     // listview
        ArrayList<singleItem> items = new ArrayList<>();                        // item 저장할 ArrayList
        items.add(new singleItem("롯데)빅팜60g","1600원",R.drawable.img0));          // item 추가
        items.add(new singleItem("해피바스)로즈바디워시","5000원",R.drawable.img1));   // item 추가
        items.add(new singleItem("46cm)초극세모칫솔","3500원",R.drawable.img2));     // item 추가

        singleItemAdapter adapter = new singleItemAdapter(items, getApplicationContext());  // 어댑터 생성
        listView.setAdapter(adapter);   // 어댑터 연결
    }
}