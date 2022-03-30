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

        dataParsing();  // 데이터 파싱
        dataLoad();     // 데이터 로드

        // 리사이클러뷰 바인딩 및 어댑터와 연결
        recyclerView = (RecyclerView)viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return viewGroup;
    }

    private void dataParsing(){
        // Goods.java 에서 전달한 번들 저장
        Bundle bundle = getArguments();

        // 번들 안의 텍스트 불러오기
        String place = bundle.getString("place");

        InputStream inName;
        InputStream inPrice;
        InputStream inUrl;

        // 편의점과 행사에 맞는 파일 가져오기
        // 2+1
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