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

        ListView listView = findViewById(R.id.cu_listview);
        ArrayList<singleItem> items = new ArrayList<>();
        items.add(new singleItem("빅팜","1600원",R.drawable.img0));

        singleItemAdapter adapter = new singleItemAdapter(items, getApplicationContext());
        listView.setAdapter(adapter);
    }
}