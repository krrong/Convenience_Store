package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class cu extends AppCompatActivity {
    Button rtn_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu);
        String[] CU_name;
        String[] CU_price;

        // 돌아가기 버튼 + 기능
        rtn_btn = findViewById(R.id.rtn_btn);
        rtn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // CU 1+1 상품, 가격 파일로부터 읽어오기
        try {
            InputStream in_name = getResources().openRawResource(R.raw.cu_11_name);
            InputStream in_price = getResources().openRawResource(R.raw.cu_11_price);

            byte[] b_name = new byte[in_name.available()];      // available = 읽을 수 있는 바이트 수 반환
            byte[] b_price = new byte[in_price.available()];    // available = 읽을 수 있는 바이트 수 반환

            // 인자만큼 읽어오기
            in_name.read(b_name);
            in_price.read(b_price);

            // byte -> string 변환
            String s_name = new String(b_name);
            String s_price = new String(b_price);

//            Log.e("test",s_name);       // 잘 읽어오는지 확인을 위한 로그
//            Log.e("test",s_price);      // 잘 읽어오는지 확인을 위한 로그

            // "\n" 단위로 나누어 각각 배열에 저장
            CU_name = s_name.split("\n");
            CU_price = s_price.split("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView listView = findViewById(R.id.cu_listview);                     // listview
        ArrayList<singleItem> items = new ArrayList<>();                        // item 저장할 ArrayList
        items.add(new singleItem("롯데)빅팜60g","1600원",R.drawable.img0));          // item 추가
        items.add(new singleItem("해피바스)로즈바디워시","5000원",R.drawable.img1));   // item 추가
        items.add(new singleItem("46cm)초극세모칫솔","3500원",R.drawable.img2));     // item 추가

        singleItemAdapter adapter = new singleItemAdapter(items, getApplicationContext());  // 어댑터 생성
        listView.setAdapter(adapter);   // 어댑터 연결
    }
}