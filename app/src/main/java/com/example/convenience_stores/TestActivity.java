package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.convenience_stores.adapter.ItemAdapter;
import com.example.convenience_stores.data.SingleItem;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private ArrayList<SingleItem> originalList = new ArrayList<>();     // 원래 어댑터가 가지고 있던 리스트 저장 용도
    private ArrayList<SingleItem> searchList = new ArrayList<>();       // 빈 리스트, 검색 데이터 저장 용도

    private RecyclerView recyclerView;
    private ItemAdapter adapter = new ItemAdapter(new ArrayList<SingleItem>());
    private EditText editTextTest;

    String[] nameList;
    String[] priceList;
    String[] urlList;
    String place;           // 선택한 편의점 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        getData();
        initView();
        initEditText();
    }

    // intent로부터 데이터를 얻어옴
    private void getData(){
        Intent intent = getIntent();
        place = intent.getStringExtra("place");

        nameList = intent.getStringArrayExtra("nameList");
        priceList = intent.getStringArrayExtra("priceList");
        urlList = intent.getStringArrayExtra("urlList");
    }

    // 초기 세팅
    void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewTest);
        editTextTest = (EditText) findViewById(R.id.editTextTest);

        // adapter 에 아이템 추가
        for (int i = 0; i < nameList.length; i++) {
            adapter.addItem(new SingleItem(nameList[i], priceList[i], urlList[i]));
        }
        // originalList 는 모든 상품이 들어가 있는 상태로 세팅
        originalList = adapter.getItems();

        // adapter 를 데이터가 없는 searchList 로 세팅(초기에 아무것도 보여주지 않기 위함)
        adapter.setItems(searchList);

        // recyclerView <-> adapter 연결
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // 검색창관련 메서드 정리
    private void initEditText() {
        editTextTest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editTextTest.getText().toString();
                searchFilter(searchText);
            }

            public void searchFilter(String searchText) {
                // == 대신 equals사용 해야함
                // "" 이면 검색어를 지운 것이므로 아무것도 보이지 않도록 동작(searchList 를 clear() 해서 사용)
                if (searchText.equals("")) {
                    searchList.clear();
                    adapter.setItems(searchList);
                }
                // searchText 를 포함하는 아이템들만 보이도록 동작(searchList 사용)
                else {
                    searchList.clear();
                    for (int i = 0; i < originalList.size(); i++) {
                        if (originalList.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                            searchList.add(originalList.get(i));
                        }
                    }
                    adapter.setItems(searchList);
                }
            }
        });
    }
}