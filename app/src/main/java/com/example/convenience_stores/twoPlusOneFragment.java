package com.example.convenience_stores;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class twoPlusOneFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter = new ItemAdapter(new ArrayList<singleItem>());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_two_plus_one, container, false);

        adapter.addItem(new singleItem("a", "1000", "http://bgf-cu.xcache.kinxcdn.com/product/6921211104292.jpg"));
        adapter.addItem(new singleItem("b", "2000", "http://bgf-cu.xcache.kinxcdn.com/product/6921211104292.jpg"));
        adapter.addItem(new singleItem("C", "3000", "http://bgf-cu.xcache.kinxcdn.com/product/6921211104292.jpg"));

        recyclerView = (RecyclerView)viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return viewGroup;
    }
}