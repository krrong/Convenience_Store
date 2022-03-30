package com.example.convenience_stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Goods extends FragmentActivity {
    private Button searchConvenience;
    private String place;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private FragmentAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> tabTitles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        initView();
        setListener();
    }

    private void initView(){
        Intent intent = getIntent();
        place = intent.getStringExtra("place");

        viewPager2 = (ViewPager2) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        searchConvenience = (Button) findViewById(R.id.searchConvenience);
        adapter = new FragmentAdapter(this);

        // 번들 객체 생성
        Bundle bundle = new Bundle();
        bundle.putString("place", place);

        // fragment 생성
        fragments.add(new onePlusOneFragment());
        fragments.add(new twoPlusOneFragment());
        
        // fragment로 bundle 전달
        fragments.get(0).setArguments(bundle);
        fragments.get(1).setArguments(bundle);

        tabTitles.add("1+1");
        tabTitles.add("2+1");

        adapter.fragmentArrayList = fragments;

        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(Goods.this);
                textView.setText(tabTitles.get(position));
                tab.setCustomView(textView);
            }
        }).attach();
    }

    private void setListener(){
        searchConvenience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchMap.class);
                intent.putExtra("place", place);
                startActivity(intent);
            }
        });
    }
}