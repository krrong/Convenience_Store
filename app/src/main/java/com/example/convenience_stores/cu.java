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
    Button rtn_btn;         // 돌아가기 버튼
    EditText search;        // 검색창
    TextView countTextView; // n/m 표기위한 텍스트뷰
    String msg;             // n/m 표기위한 메시지

    private boolean isLoading = false;  // 로딩중 표시
    private int loadLength = 20;        // 받아올 데이터 개수
    private int totalLength = 0;        // 선택한 페이지의 데이터 총 개수
    private int curLength = 0;          // 선택한 페이지의 데이터 현재 개수

    String[] CU_name;   // 이름 배열
    String[] CU_price;  // 가격 배열
    String[] CU_url;    // 링크 배열

    String[] SEVEN_name;   // 이름 배열
    String[] SEVEN_price;  // 가격 배열
    String[] SEVEN_url;    // 링크 배열

    String[] GS_name;   // 이름 배열
    String[] GS_price;  // 가격 배열
    String[] GS_url;    // 링크 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu);

        Intent intent = getIntent();
        String place = intent.getStringExtra("place");
        String event = intent.getStringExtra("event");

        if (place.equals("cu")){
            cuDataParsing(event);
        }

        else if (place.equals("seven")){
            sevenDataParsing(event);
        }
        else if(place.equals("gs")){
            gsDataParsing(event);
        }

        initTextView();
        initReturnBtn();
        dataLoad(place);
        initAdapter();
        initScrollListener(place);
        initEditText();
    }

    // 돌아가기 버튼 + 기능
    private void initReturnBtn(){
        rtn_btn = findViewById(R.id.rtn_btn);
        rtn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // n/m 텍스트 표시
    private void initTextView(){
        countTextView = findViewById(R.id.countTextView);
        msg = Integer.toString(loadLength) + "/" + Integer.toString(totalLength);
        countTextView.setText(msg);
    }

    // 상품, 가격, 링크를 [res] -> [raw] -> [.txt]파일로부터 읽어와 배열에 저장
    private void cuDataParsing(String event){
        if (event.equals("11")){
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

                // 데이터 총 개수
                totalLength = CU_name.length;
                Log.e("테스트", Integer.toString(totalLength));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                InputStream inName = getResources().openRawResource(R.raw.cu_21_name);
                InputStream inPrice = getResources().openRawResource(R.raw.cu_21_price);
                InputStream inUrl  = getResources().openRawResource(R.raw.cu_21_link);

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

                // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
                CU_name = s_name.split("\n");
                CU_price = s_price.split("\n");
                CU_url = s_url.split("\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 상품, 가격, 링크를 [res] -> [raw] -> [.txt]파일로부터 읽어와 배열에 저장
    private void sevenDataParsing(String event){
        if (event.equals("11")){
            try {
                InputStream inName = getResources().openRawResource(R.raw.seven_11_name);
                InputStream inPrice = getResources().openRawResource(R.raw.seven_11_price);
                InputStream inUrl  = getResources().openRawResource(R.raw.seven_11_link);

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

                // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
                SEVEN_name = s_name.split("\n");
                SEVEN_price = s_price.split("\n");
                SEVEN_url = s_url.split("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                InputStream inName = getResources().openRawResource(R.raw.seven_21_name);
                InputStream inPrice = getResources().openRawResource(R.raw.seven_21_price);
                InputStream inUrl  = getResources().openRawResource(R.raw.seven_21_link);

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

                // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
                SEVEN_name = s_name.split("\n");
                SEVEN_price = s_price.split("\n");
                SEVEN_url = s_url.split("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 상품, 가격, 링크를 [res] -> [raw] -> [.txt]파일로부터 읽어와 배열에 저장
    private void gsDataParsing(String event){
        if (event.equals("11")){
            try {
                InputStream inName = getResources().openRawResource(R.raw.gs_11_name);
                InputStream inPrice = getResources().openRawResource(R.raw.gs_11_price);
                InputStream inUrl  = getResources().openRawResource(R.raw.gs_11_link);

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

                // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
                GS_name = s_name.split("\n");
                GS_price = s_price.split("\n");
                GS_url = s_url.split("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                InputStream inName = getResources().openRawResource(R.raw.gs_21_name);
                InputStream inPrice = getResources().openRawResource(R.raw.gs_21_price);
                InputStream inUrl  = getResources().openRawResource(R.raw.gs_21_link);

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

                // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
                GS_name = s_name.split("\n");
                GS_price = s_price.split("\n");
                GS_url = s_url.split("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 처음 실행하는 데이터 로드 함수(loadLength)개의 데이터 로드
    private void dataLoad(String place){
        if (place.equals("cu")){
            // ArrayList에 아이템(이름, 가격, url) loadLength(20)개 추가
            for(int i=0; i<loadLength; i++){
                adapter.addItem(new singleItem(CU_name[i], CU_price[i], CU_url[i]));
            }
        }
        else if(place.equals("seven")){
            // ArrayList에 아이템(이름, 가격, url) loadLength(20)개 추가
            for(int i=0; i<loadLength; i++){
                adapter.addItem(new singleItem(SEVEN_name[i], SEVEN_price[i], SEVEN_url[i]));
            }
        }
        else if(place.equals("gs")){
            // ArrayList에 아이템(이름, 가격, url) loadLength(20)개 추가
            for(int i=0; i<loadLength; i++){
                adapter.addItem(new singleItem(GS_name[i], GS_price[i], GS_url[i]));
            }
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
        });
    }

//     추가로 로드하는 함수
    private void loadMore(String place){
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

                if(place.equals("cu")){
                    // nextLimit이 전체 상품 개수보다 크면 수동으로 크기 설정
                    if(nextLimit > CU_name.length - 1){
                        nextLimit = CU_name.length - 1;
                    }

                    // 아이템 추가
                    while(currentSize - 1 < nextLimit){
                        adapter.addItem(new singleItem(CU_name[currentSize], CU_price[currentSize], CU_url[currentSize]));
                        currentSize++;
                    }
                }
                else if(place.equals("seven")){
                    // nextLimit이 전체 상품 개수보다 크면 수동으로 크기 설정
                    if(nextLimit > SEVEN_name.length - 1){
                        nextLimit = SEVEN_name.length - 1;
                    }

                    // 아이템 추가
                    while(currentSize - 1 < nextLimit){
                        adapter.addItem(new singleItem(SEVEN_name[currentSize], SEVEN_price[currentSize], SEVEN_url[currentSize]));
                        currentSize++;
                    }
                }
                else if(place.equals("gs")){
                    // nextLimit이 전체 상품 개수보다 크면 수동으로 크기 설정
                    if(nextLimit > GS_name.length - 1){
                        nextLimit = GS_name.length - 1;
                    }

                    // 아이템 추가
                    while(currentSize - 1 < nextLimit){
                        adapter.addItem(new singleItem(GS_name[currentSize], GS_price[currentSize], GS_url[currentSize]));
                        currentSize++;
                    }
                }
                msg = Integer.toString(currentSize) + "/" + Integer.toString(totalLength);
                countTextView.setText(msg);
                
                adapter.notifyDataSetChanged();
                isLoading = false;

                // 현재 리스트 임시 저장(필터링 시 재사용 위함)
                originalList = adapter.getItems();
            }

        }, 1000);
    }
}