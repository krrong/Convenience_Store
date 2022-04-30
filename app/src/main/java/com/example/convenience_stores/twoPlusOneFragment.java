package com.example.convenience_stores;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.util.ArrayList;

public class twoPlusOneFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter = new ItemAdapter(new ArrayList<singleItem>());

    private ArrayList<singleItem> searchList = new ArrayList<>();       // 검색한 단어와 일치하는 리스트 저장 용도
    private ArrayList<singleItem> originalList = new ArrayList<>();     // 원래 어댑터가 가지고 있던 리스트 저장 용도

    private String[] nameList;  // 이름 배열
    private String[] priceList; // 가격 배열
    private String[] urlList;   // 링크 배열

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_two_plus_one, container, false);

        getData();
        dataLoad();     // 데이터 로드

        // 리사이클러뷰 바인딩 및 어댑터와 연결
        recyclerView = (RecyclerView)viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return viewGroup;
    }

    // Goods 액티비티에서 Bundle 객체를 이용하여 데이터를 받아오는 함수
    private void getData(){
        Bundle bundle = getArguments();
        nameList = bundle.getStringArray("nameList");
        priceList = bundle.getStringArray("priceList");
        urlList = bundle.getStringArray("urlList");
    }

    // 데이터 로드 (전체 데이터 한번에 로드)
    private void dataLoad(){
        int stride = nameList.length;
        for(int i=0; i<stride; i++){
            adapter.addItem(new singleItem(nameList[i], priceList[i], urlList[i]));
        }
        // 현재 리스트 임시 저장(필터링 시 재사용 위함)
        originalList = adapter.getItems();
    }
}