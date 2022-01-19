package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

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
        SingleAdapter adapter = new SingleAdapter();
        adapter.addItem(new singleItem("롯데)빅팜60g","1600원",R.drawable.img0));
        adapter.addItem(new singleItem("해피바스)로즈바디워시","5000원",R.drawable.img1));
        adapter.addItem(new singleItem("46cm)초극세모칫솔","3500원",R.drawable.img2));

        listView.setAdapter(adapter);
    }

    class SingleAdapter extends BaseAdapter{
        ArrayList<singleItem> items = new ArrayList<singleItem>();

        @Override
        public int getCount(){
            return items.size();
        }

        public void addItem(singleItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position){
            return items.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            singleItemView singleItemView1 = null;

            if(convertView == null){
                singleItemView1 = new singleItemView(getApplicationContext());
            }
            else{
                singleItemView1 = (singleItemView)convertView;
            }
            singleItem item = items.get(position);
            singleItemView1.setName(item.getName());
            singleItemView1.setPrice(item.getPrice());
            singleItemView1.setImageView(item.getResId());

            return singleItemView1;
        }
    }
}