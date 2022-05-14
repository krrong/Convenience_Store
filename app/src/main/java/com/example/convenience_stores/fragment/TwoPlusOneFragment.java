package com.example.convenience_stores.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.convenience_stores.R;
import com.example.convenience_stores.data.SingleItem;
import com.example.convenience_stores.adapter.ItemAdapter;

import java.util.ArrayList;

public class TwoPlusOneFragment extends GoodsBaseFragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter = new ItemAdapter(new ArrayList<SingleItem>());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_two_plus_one, container, false);

        // 리사이클러뷰 바인딩, 어댑터와 연결
        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.twoPlusOneRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return viewGroup;
    }

    @Override
    public void setData(ArrayList<SingleItem> goodList) {
        adapter.setItems(goodList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void dataParsing(String place, String event) {
        super.dataParsing(place, event);
    }

    @Override
    public ItemAdapter getAdapter() {
        return adapter;
    }
}