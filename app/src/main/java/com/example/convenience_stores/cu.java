package com.example.convenience_stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

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
    private RecyclerView recyclerView;
    private ItemAdapter adapter = new ItemAdapter(new ArrayList<singleItem>()); // 어댑터 생성(빈 리스트)
    private Button rtn_btn;         // 돌아가기 버튼
    private Button lowerBtn;        // 아래로 이동 버튼
    private Button searchBtn;       // 주변 편의점 찾는 버튼
    private EditText search;        // 검색창
    private TextView countTextView; // n/m 표기위한 텍스트뷰
    private TextView pageInfo;      // 편의점, 행사 표기 텍스트뷰
    private String msg;             // n/m 표기위한 메시지
    private String place;           // 편의점 이름
    private String event;           // 선택한 행사

    private boolean isLoading = false;  // 로딩중 표시
    private int loadLength = 20;        // 받아올 데이터 개수

    private String[] nameList;  // 이름 배열
    private String[] priceList; // 가격 배열
    private String[] urlList;   // 링크 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu);

        dataParsing();              // 편의점, 행사에 맞춰 알맞는 파일 읽어서 데이터 파싱
        initView();                 // 초기 설정
        setBtnListener();           // 버튼 리스터 설정
        changeCountText(loadLength);// n/m개 텍스트 뷰 보이도록 초기설정
        dataLoad();                 // 배열에 저장한 데이터 loadLength(20개) 만큼 추가하기
        initAdapter();              // 어댑터 연결하기
        initScrollListener(place);
        initEditText();             // 검색창 검색했을 때
    }

    // 초기 설정
    private void initView(){
        rtn_btn = findViewById(R.id.rtn_btn);
        countTextView = findViewById(R.id.countTextView);
        pageInfo = findViewById(R.id.pageInfo);
        recyclerView = findViewById(R.id.recycler);
        lowerBtn = findViewById(R.id.lowerBtn);
        searchBtn = findViewById(R.id.searchMapBtn);
        search = findViewById(R.id.editText);


        // 선택한 편의점에 맞도록 text구성
        searchBtn.setText("주변 " + place + "찾기");

        if (event.equals("11")){
            pageInfo.setText(place + " 1 + 1 행사");
        }
        else {
            pageInfo.setText(place + " 2 + 1 행사");
        }
    }

    private void setBtnListener(){
        rtn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchMap.class);
                intent.putExtra("place",place);
                startActivity(intent);
            }
        });

        lowerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                Log.e("nameList 개수 : ", Integer.toString(nameList.length));
                Log.e("Adapter 데이터 개수 : ", Integer.toString(recyclerView.getAdapter().getItemCount()-1));
                if(nameList.length == recyclerView.getAdapter().getItemCount()){
                    Toast.makeText(view.getContext(), "모두 내렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // n/m 텍스트 표시
    private void changeCountText(int size){
        msg = Integer.toString(size) + "/" + Integer.toString(nameList.length);
        countTextView.setText(msg);
    }

    // 상품, 가격, 링크를 [res] -> [raw] -> [.txt]파일로부터 읽어와 배열에 저장
    private void dataParsing(){
        Intent intent = getIntent();
        place = intent.getStringExtra("place");
        event = intent.getStringExtra("event");

        InputStream inName;
        InputStream inPrice;
        InputStream inUrl;

        // 편의점과 행사에 맞는 파일 가져오기
        // 1+1
        if(event.equals("11")) {
            if (place.equals("CU")){
                inName = getResources().openRawResource(R.raw.cu_11_name);
                inPrice = getResources().openRawResource(R.raw.cu_11_price);
                inUrl  = getResources().openRawResource(R.raw.cu_11_link);
            }
            else if(place.equals("7ELEVEn")){
                inName = getResources().openRawResource(R.raw.seven_11_name);
                inPrice = getResources().openRawResource(R.raw.seven_11_price);
                inUrl  = getResources().openRawResource(R.raw.seven_11_link);
            }
            else{
                inName = getResources().openRawResource(R.raw.gs_11_name);
                inPrice = getResources().openRawResource(R.raw.gs_11_price);
                inUrl  = getResources().openRawResource(R.raw.gs_11_link);
            }
        }
        // 2+1
        else{
            if (place.equals("CU")){
                inName = getResources().openRawResource(R.raw.cu_21_name);
                inPrice = getResources().openRawResource(R.raw.cu_21_price);
                inUrl  = getResources().openRawResource(R.raw.cu_21_link);
            }
            else if(place.equals("7ELEVEn")){
                inName = getResources().openRawResource(R.raw.seven_21_name);
                inPrice = getResources().openRawResource(R.raw.seven_21_price);
                inUrl  = getResources().openRawResource(R.raw.seven_21_link);
            }
            else{
                inName = getResources().openRawResource(R.raw.gs_21_name);
                inPrice = getResources().openRawResource(R.raw.gs_21_price);
                inUrl  = getResources().openRawResource(R.raw.gs_21_link);
            }
        }

        // 파일에서 데이터 읽어오기
        try {
            byte[] bName = new byte[inName.available()]; // available = 읽을 수 있는 바이트 수 반환
            byte[] bPrice = new byte[inPrice.available()];
            byte[] bUrl = new byte[inUrl.available()];

            // 인자만큼 읽어오기
            inName.read(bName);
            inPrice.read(bPrice);
            inUrl.read(bUrl);

            // byte -> string 변환
            String s_name = new String(bName);
            String s_price = new String(bPrice);
            String s_url = new String(bUrl);

            // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
            nameList = s_name.split("\n");
            priceList = s_price.split("\n");
            urlList = s_url.split("\n");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 처음 실행하는 데이터 로드 함수(loadLength)개의 데이터 로드
    private void dataLoad(){
        for(int i=0; i<loadLength; i++){
            adapter.addItem(new singleItem(nameList[i], priceList[i], urlList[i]));
        }
        // 현재 리스트 임시 저장(필터링 시 재사용 위함)
        originalList = adapter.getItems();
    }

    // recyclerView - adapter 연결
    private void initAdapter(){
        // activity_cu.xml에 있는 recyclerview 가져와서 adapter 붙여 넣기
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adapter 연결
        recyclerView.setAdapter(adapter);
    }

    // 검색창관련 메서드 정리
    private void initEditText(){
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
    private void initScrollListener(String place){
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
                    Log.e("메세지", "최상단");
                }
                // 스크롤이 최하단일 경우
                else if(!recyclerView.canScrollVertically(1)){
                    Log.e("메세지", "최하단");

                    // 검색어가 입력이 되지 않은 상태에서만 추가 로드
                    if(search.getText().toString().equals("")){
                        System.out.println(adapter.getItemCount());

                        LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                        System.out.println(layoutManager.findLastVisibleItemPosition());

                        if(!isLoading){
                            // 배열의 마지막이면 loadMore 함수를 이용하여 추가로드
                            if(layoutManager != null && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() -1){
                                loadMore(place);
                                isLoading = true;   // 2초후에 실행되는 loadMore에서 false로 바꿔줌
                            }
                        }
                    }
                }
            }
        });
    }

    // 추가로 로드하는 함수
    private void loadMore(String place){
        // progressBar 보기 위한 null 데이터 추가
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
                if(nextLimit > nameList.length - 1){
                    nextLimit = nameList.length - 1;
                }

                // 아이템 추가
                while(currentSize - 1 < nextLimit){
                    adapter.addItem(new singleItem(nameList[currentSize], priceList[currentSize], urlList[currentSize]));
                    currentSize++;
                }

                // n/m개 텍스트 뷰 변경
                changeCountText(currentSize);
                
                adapter.notifyDataSetChanged();
                isLoading = false;

                // 현재 리스트 임시 저장(필터링 시 재사용 위함)
                originalList = adapter.getItems();
            }

        }, 1000);
    }
}