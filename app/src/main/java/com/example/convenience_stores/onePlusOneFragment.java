package com.example.convenience_stores;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.util.ArrayList;

public class onePlusOneFragment extends GoodBaseFragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter = new ItemAdapter(new ArrayList<singleItem>());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_one_plus_one, container, false);

        // 리사이클러뷰 바인딩, 어댑터와 연결
        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.onePlusOneRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return viewGroup;
    }

    @Override
    public void setData(ArrayList<singleItem> goodList) {
        // 리사이클러뷰의 어댑터를 받아와서 데이터 수정
        adapter = (ItemAdapter)recyclerView.getAdapter();
        adapter.setItems(goodList);
    }
}