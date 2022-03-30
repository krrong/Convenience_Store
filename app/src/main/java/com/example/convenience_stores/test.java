package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class test extends FragmentActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    FragmentAdapter adapter;
    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    ArrayList<String> tabTitles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();

    }

    void initView(){
        viewPager2 = (ViewPager2) findViewById(R.id.viewPager);
        adapter = new FragmentAdapter(this);
        fragments.add(new onePlusOneFragment());
        fragments.add(new twoPlusOneFragment());

        adapter.fragmentArrayList = fragments;
        viewPager2.setAdapter(adapter);
    }
}