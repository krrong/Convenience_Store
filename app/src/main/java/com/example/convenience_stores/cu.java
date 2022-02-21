package com.example.convenience_stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class cu extends AppCompatActivity {
    private ArrayList<singleItem> searchList = new ArrayList<>();       // 검색한 단어와 일치하는 리스트 저장 용도
    private ArrayList<singleItem> originalList = new ArrayList<>();     // 원래 어댑터가 가지고 있던 리스트 저장 용도
    RecyclerView recyclerView;
    ItemAdapter adapter = new ItemAdapter(new ArrayList<singleItem>()); // 어댑터 생성(빈 리스트)
    Button rtn_btn;     // 돌아가기 버튼
    EditText search;    // 검색창

    private boolean isLoading = false;  // 로딩중 표시
    private int loadLength = 20;        // 받아올 데이터 개수

    String[] CU_name;   // 이름 배열
    String[] CU_price;  // 가격 배열
    String[] CU_url;    // 링크 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu);

        initReturnBtn();
        dataParsing();
        dataLoad();
        initAdapter();
        initScrollListener();
        initEditText();
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
    private void dataParsing(){
        try {
            InputStream inName = getResources().openRawResource(R.raw.cu_11_name);
            InputStream inPrice = getResources().openRawResource(R.raw.cu_11_price);
            InputStream inUrl  = getResources().openRawResource(R.raw.cu_11_link);

            byte[] bName = new byte[inName.available()]; // available = 읽을 수 있는 바이트 수 반환
            byte[] bPrice = new byte[inPrice.available()];
            byte[] bUrl  = new byte[inUrl.available()];

            // 인자만큼 읽어오기
            inName.read(bName);
            inPrice.read(bPrice);
            inUrl.read(bUrl);

            // byte -> string 변환
            String s_name = new String(bName);
            String s_price = new String(bPrice);
            String s_url = new String(bUrl);

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

    // 처음 실행하는 데이터 로드 함수(loadLength)개의 데이터 로드
    private void dataLoad(){
        // ArrayList에 아이템 추가(이름, 가격, url) 290개 추가
        for(int i=0; i<loadLength; i++){
            adapter.addItem(new singleItem(CU_name[i], CU_price[i], CU_url[i]));
        }
        // 현재 리스트 임시 저장(필터링 시 재사용 위함)
        originalList = adapter.getItems();
    }

    // recyclerView 찾아서 adapter와 연결
    private void initAdapter(){
        // activity_cu.xml에 있는 recyclerview 가져와서 adapter 붙여 넣기
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adapter 연결
        recyclerView.setAdapter(adapter);
    }

    // 검색창관련 메서드 정리
    private void initEditText(){
        search = findViewById(R.id.editText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = search.getText().toString();
                searchFilter(searchText);
            }

            public void searchFilter(String searchText){
                // == 대신 equals사용 해야함
                // "" 이면 검색어를 지운 것이므로 기존 아이템들이 보이도록 동작(originalList 사용)
                if(searchText.equals("")){
                    adapter.filterList(originalList);
                }
                // searchText 를 포함하는 아이템들만 보이도록 동작(searchList 사용)
                else{
                    searchList.clear();
                    for(int i = 0; i < originalList.size(); i++){
                        if(originalList.get(i).getName().toLowerCase().contains(searchText.toLowerCase())){
                            searchList.add(originalList.get(i));
                        }
                    }
                    adapter.filterList(searchList);
                }
            }
        });
    }

    // 스크롤 관련 메서드 정리
    private void initScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            // 사용자가 스크롤을 움직이는 동안 반복하여 호출됨
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 스크롤이 최상단일 경우
                if(!recyclerView.canScrollVertically(-1)){
                    Log.e("cu 액티비티", "최상단");
                }
                // 스크롤이 최하단일 경우
                else if(!recyclerView.canScrollVertically(1)){
                    Log.e("cu 액티비티", "최하단");
                    System.out.println(adapter.getItemCount());

                    LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                    System.out.println(layoutManager.findLastVisibleItemPosition());

//                    LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

                    if(!isLoading){
                        // 배열의 마지막이면 loadMore 함수를 이용하여 추가로드
                        if(layoutManager != null && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() -1){
                            loadMore();
                            isLoading = true;   // 2초후에 실행되는 loadMore에서 false로 바꿔줌
                        }
                    }
                }
            }
        });
    }

//     추가로 로드하는 함수
    private void loadMore(){
//        // progressBar 보기 위한 null 데이터 추가
        adapter.addItem(null);
        adapter.notifyItemInserted(adapter.getItemCount());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // null 데이터 삭제
                adapter.getItems().remove(adapter.getItemCount() - 1);
                int scrollPosition = adapter.getItemCount();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + loadLength;

                // nextLimit이 전체 상품 개수보다 크면 수동으로 크기 설정
                if(nextLimit > CU_name.length - 1){
                    nextLimit = CU_name.length - 1;
                }

                // 아이템 추가
                while(currentSize - 1 < nextLimit){
                    adapter.addItem(new singleItem(CU_name[currentSize], CU_price[currentSize], CU_url[currentSize]));
                    currentSize++;
                }
                adapter.notifyDataSetChanged();
                isLoading = false;

                // 현재 리스트 임시 저장(필터링 시 재사용 위함)
                originalList = adapter.getItems();
            }

        }, 1000);
    }
}