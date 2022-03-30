package com.example.convenience_stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

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
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new FragmentAdapter(this);

        fragments.add(new onePlusOneFragment());
        fragments.add(new twoPlusOneFragment());

        tabTitles.add("1+1");
        tabTitles.add("2+1");

        adapter.fragmentArrayList = fragments;

        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(test.this);
                textView.setText(tabTitles.get(position));
                tab.setCustomView(textView);
            }
        }).attach();
    }
}