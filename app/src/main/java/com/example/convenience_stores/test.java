package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;

import java.util.ArrayList;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ArrayList<singleItem> items = new ArrayList<>();
        items.add(new singleItem("a","1000","http://bgf-cu.xcache.kinxcdn.com/product/8801051009033.jpg"));
        items.add(new singleItem("b","1000","http://bgf-cu.xcache.kinxcdn.com/product/8801051009033.jpg"));
        items.add(new singleItem("c","1000","http://bgf-cu.xcache.kinxcdn.com/product/8801051009033.jpg"));
        items.add(new singleItem("d","1000","http://bgf-cu.xcache.kinxcdn.com/product/8801051009033.jpg"));
        items.add(new singleItem("e","1000","http://bgf-cu.xcache.kinxcdn.com/product/8801051009033.jpg"));
        items.add(new singleItem("f","1000",""));
        items.add(new singleItem("g","1000",""));
        items.add(new singleItem("h","1000",""));
        items.add(new singleItem("i","1000",""));
        items.add(new singleItem("j","1000",""));
        items.add(new singleItem("k","1000",""));
        items.add(new singleItem("l","1000",""));
        items.add(new singleItem("m","1000",""));
        items.add(new singleItem("n","1000",""));
        items.add(new singleItem("o","1000",""));
        items.add(new singleItem("p","1000",""));
        items.add(new singleItem("q","1000",""));
        items.add(new singleItem("r","1000",""));
        items.add(new singleItem("s","1000",""));
        items.add(new singleItem("t","1000",""));
        items.add(new singleItem("u","1000",""));

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemAdapter adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);
    }
}