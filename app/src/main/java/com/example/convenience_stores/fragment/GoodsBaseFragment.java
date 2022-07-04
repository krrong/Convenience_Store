package com.example.convenience_stores.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.convenience_stores.R;
import com.example.convenience_stores.data.SingleItem;
import com.example.convenience_stores.adapter.ItemAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GoodsBaseFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_good_base, container, false);

        // 리사이클러뷰 바인딩 및 어댑터와 연결
        recyclerView = (RecyclerView)viewGroup.findViewById(R.id.baseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return viewGroup;
    }

    /**
     * adapter 에 파싱된 데이터 셋팅 (화면 갱신)
     * @param goodList
     */
    public void setData(ArrayList<SingleItem> goodList) {
        if (recyclerView == null)
            return;

        // 리사이클러뷰의 어댑터를 받아와서 데이터 수정
        ItemAdapter adapter = new ItemAdapter(goodList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * recyclerView 에 붙은 adapter 리턴 
     */
    public ItemAdapter getAdapter() {
        // 리사이클러뷰가 없거나 연결된 어댑터가 없으면 종료
        if (recyclerView == null || recyclerView.getAdapter() == null)
            return null;

        // 리사이클러뷰의 어댑터 리턴
        return (ItemAdapter) recyclerView.getAdapter();
    }


    /**
     * JSON으로 저장된 파일을 불러와 데이터 파싱
     * @param place
     * @param event
     */
    public void dataParsing(String place, String event) {
        String fileName = getFileName(place, event);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;

        // fragment에 넘겨줄 데이터를 items에 담음
        ArrayList<SingleItem> items = new ArrayList<>();

        try {
            InputStream inputStream = getContext().openFileInput(fileName);
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String data = bufferedReader.readLine();

                // 파일 전체 읽기
                while(data != null){
                    Object obj = parser.parse(data);
                    jsonObject = (JsonObject) obj;

                    JsonElement name = jsonObject.get("name");
                    JsonElement price = jsonObject.get("price");
                    JsonElement url = jsonObject.get("url");

                    items.add(new SingleItem(name.getAsString(), price.getAsString(), url.getAsString()));

                    // 다음 데이터 읽어옴
                    data = bufferedReader.readLine();
                }

                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setData(items);
    }

    /**
     * 편의점과 이벤트를 입력 받아 알맞는 JSON파일명 리턴
     */
    public String getFileName(String place, String event) {
        switch(place) {
            case "CU":
                switch (event) {
                    case "1+1":
                        return "cu11.txt";
                    case "2+1":
                        return "cu21.txt";
                }
            case "7ELEVEn":
                switch (event) {
                    case "1+1":
                        return "seven11.txt";
                    case "2+1":
                        return "seven21.txt";
                }
            case "GS25":
                switch (event) {
                    case "1+1":
                        return "gs11.txt";
                    case "2+1":
                        return "gs21.txt";
                }
        }
        return null;
    }
}
