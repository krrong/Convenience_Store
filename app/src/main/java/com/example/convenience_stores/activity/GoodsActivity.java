package com.example.convenience_stores.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.convenience_stores.adapter.FragmentAdapter;
import com.example.convenience_stores.adapter.ItemAdapter;
import com.example.convenience_stores.data.SingleItem;
import com.example.convenience_stores.fragment.GoodsBaseFragment;
import com.example.convenience_stores.fragment.OnePlusOneFragment;
import com.example.convenience_stores.R;
import com.example.convenience_stores.SearchMapActivity;
import com.example.convenience_stores.fragment.TwoPlusOneFragment;
import com.example.convenience_stores.mData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GoodsActivity extends FragmentActivity {
    private Button searchConvenienceBtn;                            // 편의점 검색 버튼
    private Button searchGoodsBtn;                                  // 상품 검색 버튼
    private String place;                                           // 선택한 편의점(get from Intent)
    int pos;                                                        // 뷰페이저가 보고 있는 페이지 position 저장

//    String[] nameList;
//    String[] priceList;
//    String[] urlList;
//    Bundle bundle = new Bundle();                               // viewPager2와 Fragment가 정보를 주고 받는 매개체

    private ViewPager2 viewPager2;                              // 뷰페이저
    private TabLayout tabLayout;                                // 레이아웃
    //private FragmentAdapter adapter;                            // 뷰페이저 어댑터
    //private ArrayList<Fragment> fragments = new ArrayList<Fragment>(); // 1+1, 2+1 프래그먼트 담을 ArrayList
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

        // binding
        viewPager2 = (ViewPager2) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        searchConvenienceBtn = (Button) findViewById(R.id.searchConvenienceBtn);
        searchGoodsBtn = (Button) findViewById(R.id.searchGoodsBtn);

        ArrayList<Fragment> fragments = new ArrayList();

        // fragment 생성
        fragments.add(new OnePlusOneFragment());    // 1+1 프래그먼트
        fragments.add(new TwoPlusOneFragment());    // 2+1 프래그먼트

        FragmentAdapter fragmentAdapter = new FragmentAdapter(this, fragments);

        // 주변 편의점 검색 버튼 텍스트
        searchConvenienceBtn.setText("주변 " + place + "검색하기");

        // 탭 타이틀 추가
        tabTitles.add("1+1");
        tabTitles.add("2+1");

        // fragmentAdapter에 fragment 넣어주기
        fragmentAdapter.fragmentArrayList = fragments;

        // 뷰페이저에 어댑터 연결
        viewPager2.setAdapter(fragmentAdapter);
        viewPager2.setOffscreenPageLimit(2);
        
        // 현재 보고 있는 Fragment를 얻기 위한 콜백 생성
        viewPager2.registerOnPageChangeCallback(viewPagerCallback);

        // 탭 타이틀 연결
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(GoodsActivity.this);
                textView.setText(tabTitles.get(position));
                tab.setCustomView(textView);
            }
        }).attach();
    }

    // 리스너 함수 bind
    private void setListener(){
        // 편의점 검색 버튼 클릭 함수
        searchConvenienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchMapActivity.class);
                intent.putExtra("place", place);
                startActivity(intent);
            }
        });
        // 상품 검색 버튼 클릭 함수
        searchGoodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos > 1){
                    Log.e("MSG", "searchGoodsBtn.setOnClickListener 에러");
                    return;
                }

                // 현재 보고 있는 프래그먼트의 어댑터 -> 어댑터의 아이템을 items 에 저장.
                FragmentAdapter fragmentAdapter = (FragmentAdapter)viewPager2.getAdapter();
                ItemAdapter itemAdapter = ((GoodsBaseFragment) fragmentAdapter.getItem(pos)).getAdapter();
                ArrayList<SingleItem> items =  itemAdapter.getItems();

                // 위치와 아이템을 intent 를 통해 SearchGoodsActivity 로 전달
                Intent intent = new Intent(getApplicationContext(), SearchGoodsActivity.class);
                intent.putExtra("place", place);
                // 객체 리스트 보낼 때
                intent.putParcelableArrayListExtra("items", items);
                
                // 객체만 보낼 때
                // intent.putExtra("items", item);
                startActivity(intent);
            }
        });
    }

    /**
     * 각 Fragment에 데이터를 넘겨주기 위한 콜백 (Fragment 전환 시 실행)
     */
    private ViewPager2.OnPageChangeCallback viewPagerCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            // 현재 보고 있는 페이지의 position 저장
            pos = position;

            // 1+1 Fragment : 1+1파일 파싱해서 보내주기
            // 2+1 Fragment : 2+1파일 파싱해서 보내주기
            dataTransfer(position, place, position == 0 ? "1+1" : "2+1");
        }
    };

    /**
     * place, event에 맞는 데이터를 파싱하여 position에 위치한 Fragment에 전달
     */
    private void dataTransfer(int position, String place, String event){
        if(viewPager2 == null || viewPager2.getAdapter() == null){
            return;
        }

        FragmentAdapter fragmentAdapter = (FragmentAdapter) viewPager2.getAdapter();
        ((GoodsBaseFragment) fragmentAdapter.getItem(position)).dataParsing(place, event);
    }
}