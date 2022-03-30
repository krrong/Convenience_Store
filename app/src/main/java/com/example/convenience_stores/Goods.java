package com.example.convenience_stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Goods extends FragmentActivity {
    private Button searchConvenience;
    private Button searchGoods;
    private String place;
    private String[] nameList;
    private String[] priceList;
    private String[] urlList;

    private ViewPager2 viewPager2;                              // 뷰페이저
    private TabLayout tabLayout;                                // 레이아웃
    private FragmentAdapter adapter;                            // 뷰페이저 어댑터
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>(); // 1+1, 2+1 프래그먼트 담을 ArrayList
    private ArrayList<String> tabTitles = new ArrayList<String>();   // 1+1, 2+1 탭 타이틀 담을 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        initView();
        setListener();

    }

    // 초기 세팅
    private void initView(){
        Intent intent = getIntent();
        place = intent.getStringExtra("place"); // 선택한 편의점 받아오기
        nameList = intent.getStringArrayExtra("nameList");
        priceList = intent.getStringArrayExtra("priceList");
        urlList = intent.getStringArrayExtra("urlList");

        // binding
        viewPager2 = (ViewPager2) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        searchConvenience = (Button) findViewById(R.id.searchConvenience);
        searchGoods = (Button) findViewById(R.id.searchGoods);
        adapter = new FragmentAdapter(this);

        searchConvenience.setText("주변 " + place + "검색하기");

        // 번들 객체 생성
        Bundle bundle = new Bundle();
        bundle.putString("place", place);

        // fragment 생성
        fragments.add(new onePlusOneFragment());
        fragments.add(new twoPlusOneFragment());
        
        // fragment로 bundle 전달
        fragments.get(0).setArguments(bundle);
        fragments.get(1).setArguments(bundle);

        // 탭 타이틀 추가
        tabTitles.add("1+1");
        tabTitles.add("2+1");

        adapter.fragmentArrayList = fragments;

        // 뷰페이저에 어댑터 연결
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

        searchGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), test.class);
                intent.putExtra("nameList", nameList);
                intent.putExtra("priceList", priceList);
                intent.putExtra("urlList", urlList);
                startActivity(intent);
            }
        });
    }
}