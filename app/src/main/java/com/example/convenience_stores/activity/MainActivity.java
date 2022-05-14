package com.example.convenience_stores.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.convenience_stores.R;
import com.example.convenience_stores.activity.GoodsActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private Button cu_btn;
    private Button seven_btn;
    private Button gs_btn;

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
                Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
                intent.putExtra("place","CU");
                startActivity(intent);
            }
        });

        seven_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
                intent.putExtra("place","7ELEVEn");
                startActivity(intent);
            }
        });

        gs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
                intent.putExtra("place","GS25");
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
}