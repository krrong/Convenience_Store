package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private Button cu_btn;
    private Button seven_btn;
    private Button gs_btn;

    private String[] nameList;
    private String[] priceList;
    private String[] urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setBtnListener();
    }

    private void initView(){
        cu_btn = findViewById(R.id.cu_btn);
        seven_btn = findViewById(R.id.seven_btn);
        gs_btn = findViewById(R.id.gs_btn);
    }

    private void setBtnListener(){
        cu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Goods.class);
                dataParsing("CU");
                intent.putExtra("place","CU");
                intent.putExtra("nameList", nameList);
                intent.putExtra("priceList", priceList);
                intent.putExtra("urlList", urlList);
                startActivity(intent);
            }
        });

        seven_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Goods.class);
                dataParsing("7ELEVEn");
                intent.putExtra("place","7ELEVEn");
                intent.putExtra("nameList", nameList);
                intent.putExtra("priceList", priceList);
                intent.putExtra("urlList", urlList);
                startActivity(intent);
            }
        });

        gs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Goods.class);
                dataParsing("GS25");
                intent.putExtra("place","GS25");
                intent.putExtra("nameList", nameList);
                intent.putExtra("priceList", priceList);
                intent.putExtra("urlList", urlList);
                startActivity(intent);
            }
        });
    }

    // KeyHash 얻는 코드
    private void getAppKeyHash(){
        PackageInfo packageInfo = null;

        try{
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        if(packageInfo == null){
            Log.e("KeyHash", "KeyHash : null");
        }

        for(Signature signature : packageInfo.signatures){
            try{
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }
    }

    private void dataParsing(String place){
        InputStream inName;
        InputStream inPrice;
        InputStream inUrl;

        // 편의점과 행사에 맞는 파일 가져오기
        // 1+1
        if (place.equals("CU")){
            inName = getResources().openRawResource(R.raw.cu_11_name);
            inPrice = getResources().openRawResource(R.raw.cu_11_price);
            inUrl  = getResources().openRawResource(R.raw.cu_11_link);
        }
        else if(place.equals("7ELEVEn")){
            inName = getResources().openRawResource(R.raw.seven_11_name);
            inPrice = getResources().openRawResource(R.raw.seven_11_price);
            inUrl  = getResources().openRawResource(R.raw.seven_11_link);
        }
        else{
            inName = getResources().openRawResource(R.raw.gs_11_name);
            inPrice = getResources().openRawResource(R.raw.gs_11_price);
            inUrl  = getResources().openRawResource(R.raw.gs_11_link);
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}