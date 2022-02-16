package com.example.convenience_stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class cu extends AppCompatActivity {
    // item 저장할 ArrayList
    ArrayList<singleItem> items = new ArrayList<>();
    // adapter
    ItemAdapter adapter;
    Button rtn_btn;
    private RecyclerView recyclerView;

    private boolean isLoading = false;  // 로딩중 표시
    private int load_length = 20;       // 받아올 데이터 개수

    String[] CU_name;   // 이름 배열
    String[] CU_price;  // 가격 배열
    String[] CU_url;    // 링크 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu);

        initReturnBtn();
        parsingData();
        dataLoad();
        initAdapter();
        initScrollListener();
    }

    // 돌아가기 버튼+기능
    private void initReturnBtn(){
        rtn_btn = findViewById(R.id.rtn_btn);
        rtn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // 상품, 가격, 링크를 [res] -> [raw] -> [.txt]파일로부터 읽어와 배열에 저장
    private void parsingData(){
        try {
            InputStream in_name = getResources().openRawResource(R.raw.cu_11_name);
            InputStream in_price = getResources().openRawResource(R.raw.cu_11_price);
            InputStream in_url  = getResources().openRawResource(R.raw.cu_11_link);

            byte[] b_name = new byte[in_name.available()]; // available = 읽을 수 있는 바이트 수 반환
            byte[] b_price = new byte[in_price.available()];
            byte[] b_url  = new byte[in_url.available()];

            // 인자만큼 읽어오기
            in_name.read(b_name);
            in_price.read(b_price);
            in_url.read(b_url);

            // byte -> string 변환
            String s_name = new String(b_name);
            String s_price = new String(b_price);
            String s_url = new String(b_url);

////            잘 읽어오는지 확인을 위한 로그
//            Log.e("test",s_name);
//            Log.e("test",s_price);
//            Log.e("test",s_url);

            // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
            CU_name = s_name.split("\n");
            CU_price = s_price.split("\n");
            CU_url = s_url.split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 처음 실행하는 데이터 로드 함수(load_length)개의 데이터 로드
    private void dataLoad(){
        // ArrayList에 아이템 추가(이름, 가격, url) 290개 추가
        for(int i=0; i<load_length; i++){
            items.add(new singleItem(CU_name[i], CU_price[i], CU_url[i]));
        }
    }

    // recyclerView 찾아서 adapter와 연결
    private void initAdapter(){
        // activity_cu.xml에 있는 recyclerview 가져와서 adapter 붙여 넣기
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adapter 생성
        adapter = new ItemAdapter(items);

        // adapter 연결
        recyclerView.setAdapter(adapter);
    }

    // 스크롤 관련 메서드 정리
    private void initScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

                if(!isLoading){
                    // 배열의 마지막이면 loadMore을 이용하여 추가로드
                    if(layoutManager != null && layoutManager.findLastVisibleItemPosition() == items.size() -1){
                        loadMore();
                        isLoading = true;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 스크롤이 최상단인지 확인
                if(!recyclerView.canScrollVertically(-1)){
                    Log.e("cu 액티비티", "최상단");
                }
                // 스크롤이 최하단인지 확인
                else if(!recyclerView.canScrollVertically(1)){
                    Log.e("cu 액티비티", "최하단");
                }
            }
        });
    }

    // 추가로 로드하는 함수
    private void loadMore(){
        // progressBar 보기 위한 null 데이터 추가
        items.add(null);
        adapter.notifyItemInserted(items.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // null 데이터 삭제
                items.remove(items.size() - 1);
                int scrollPosition = items.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + load_length;

                // nextLimit이 전체 상품 개수보다 크면 수동으로 크기 설정
                if(nextLimit > CU_name.length - 1){
                    nextLimit = CU_name.length - 1;
                }

                // 아이템 추가
                while(currentSize - 1 < nextLimit){
                    items.add(new singleItem(CU_name[currentSize], CU_price[currentSize], CU_url[currentSize]));
                    currentSize++;
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }

        }, 2000);
    }
}