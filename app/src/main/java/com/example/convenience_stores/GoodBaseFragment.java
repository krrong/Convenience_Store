package com.example.convenience_stores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GoodBaseFragment extends Fragment {
    private RecyclerView baseRecyclerView;
    private ItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_good_base, container, false);

        // 리사이클러뷰 바인딩 및 어댑터와 연결
        baseRecyclerView = (RecyclerView)viewGroup.findViewById(R.id.baseRecyclerView);
        baseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        baseRecyclerView.setAdapter(adapter);

        return viewGroup;
    }

    public void setData(ArrayList<singleItem> goodList) {
        // 리사이클러뷰의 어댑터를 받아와서 데이터 수정
        adapter = (ItemAdapter)baseRecyclerView.getAdapter();
        adapter.setItems(goodList);
    }
}
