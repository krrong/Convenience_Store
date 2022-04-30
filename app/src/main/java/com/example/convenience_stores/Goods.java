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

import java.io.InputStream;
import java.util.ArrayList;

public class Goods extends FragmentActivity {
    private Button searchConvenience;                           // 편의점 검색 버튼
    private Button searchGoods;                                 // 상품 검색 버튼
    private String place;                                       // 선택한 편의점
    String[] nameList;
    String[] priceList;
    String[] urlList;
    Bundle bundle = new Bundle();                               // viewPager2와 Fragment가 정보를 주고 받는 매개체
    boolean flag = true;                                       //

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

        // binding
        viewPager2 = (ViewPager2) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        searchConvenience = (Button) findViewById(R.id.searchConvenience);
        searchGoods = (Button) findViewById(R.id.searchGoods);
        adapter = new FragmentAdapter(this);

        searchConvenience.setText("주변 " + place + "검색하기");

        // 번들 객체 생성
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
        
        // 현재 보고 있는 Fragment를 얻기 위한 콜백 생성
        viewPager2.registerOnPageChangeCallback(viewPagerCallback);

        // 처음에 1+1 Fragment에 데이터를 넘겨주기 위해 실행
        dataParsing(place, "1+1");

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(Goods.this);
                textView.setText(tabTitles.get(position));
                tab.setCustomView(textView);
            }
        }).attach();

    }

    // 리스너 함수 bind
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
                intent.putExtra("place", place);
                intent.putExtra("nameList", nameList);
                intent.putExtra("priceList", priceList);
                intent.putExtra("urlList", urlList);
                startActivity(intent);
            }
        });
    }

    // 2+1 Fragment에 데이터를 넘겨주기 위해 콜백으로 작성(Fragment 전환 시 실행)
    private ViewPager2.OnPageChangeCallback viewPagerCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            // 1+1 Fragment : 1+1파일 파싱해서 보내주기
            if (position == 0){
                dataParsing(place, "1+1");
            }
            // 2+1 Fragment : 2+1파일 파싱해서 보내주기
            else if(position == 1){
                dataParsing(place, "2+1");
            }
        }
    };

    // place, event에 맞는 데이터를 파싱하여 Fragment에 전달
    private void dataParsing(String place, String event){
        InputStream inName;
        InputStream inPrice;
        InputStream inUrl;

        // 편의점과 행사에 맞는 파일 가져오기
            if (place.equals("CU")){
                if(event.equals("1+1")){
                    inName = getResources().openRawResource(R.raw.cu_11_name);
                    inPrice = getResources().openRawResource(R.raw.cu_11_price);
                    inUrl  = getResources().openRawResource(R.raw.cu_11_link);
                }
                else{
                    inName = getResources().openRawResource(R.raw.cu_21_name);
                    inPrice = getResources().openRawResource(R.raw.cu_21_price);
                    inUrl  = getResources().openRawResource(R.raw.cu_21_link);
                }
            }
            else if(place.equals("7ELEVEn")){
                if(event.equals("1+1")) {
                    inName = getResources().openRawResource(R.raw.seven_11_name);
                    inPrice = getResources().openRawResource(R.raw.seven_11_price);
                    inUrl  = getResources().openRawResource(R.raw.seven_11_link);
                }
                else{
                    inName = getResources().openRawResource(R.raw.seven_21_name);
                    inPrice = getResources().openRawResource(R.raw.seven_21_price);
                    inUrl  = getResources().openRawResource(R.raw.seven_21_link);
                }
            }
            else{
                if(event.equals("1+1")) {
                    inName = getResources().openRawResource(R.raw.gs_11_name);
                    inPrice = getResources().openRawResource(R.raw.gs_11_price);
                    inUrl  = getResources().openRawResource(R.raw.gs_11_link);
                }
                else{
                    inName = getResources().openRawResource(R.raw.gs_21_name);
                    inPrice = getResources().openRawResource(R.raw.gs_21_price);
                    inUrl  = getResources().openRawResource(R.raw.gs_21_link);
                }
            }

        // 파일에서 데이터 읽어오기
        try {
            byte[] bName = new byte[inName.available()]; // available = 읽을 수 있는 바이트 수 반환
            byte[] bPrice = new byte[inPrice.available()];
            byte[] bUrl = new byte[inUrl.available()];

            // 인자만큼 읽어오기
            inName.read(bName);
            inPrice.read(bPrice);
            inUrl.read(bUrl);

            // byte -> string 변환
            String s_name = new String(bName);
            String s_price = new String(bPrice);
            String s_url = new String(bUrl);

            // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
            nameList = s_name.split("\n");
            priceList = s_price.split("\n");
            urlList = s_url.split("\n");

            bundle.putStringArray("nameList", nameList);
            bundle.putStringArray("priceList", priceList);
            bundle.putStringArray("urlList", urlList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}