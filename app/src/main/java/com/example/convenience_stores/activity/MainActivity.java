package com.example.convenience_stores.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.example.convenience_stores.data.SingleItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
        makeJson();
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

    // JSON 파일 형식으로 변경
    private void makeJson() {
        int nameFiles[] = {R.raw.cu_11_name, R.raw.seven_11_name, R.raw.gs_11_name, R.raw.cu_21_name, R.raw.seven_21_name, R.raw.gs_21_name};
        int priceFiles[] = {R.raw.cu_11_price, R.raw.seven_11_price, R.raw.gs_11_price, R.raw.cu_21_price, R.raw.seven_21_price, R.raw.gs_21_price};
        int urlFiles[] = {R.raw.cu_11_link, R.raw.seven_11_link, R.raw.gs_11_link, R.raw.cu_21_link, R.raw.seven_21_link, R.raw.gs_21_link};
        String names[] = {"cu11.txt", "seven11.txt", "gs11.txt", "cu12.txt", "seven12.txt", "gs12.txt"};

        for(int i=0;i<nameFiles.length; i++){
            String saveFileName = names[i];
            InputStream inName = getResources().openRawResource(nameFiles[i]);
            InputStream inPrice = getResources().openRawResource(priceFiles[i]);
            InputStream inUrl = getResources().openRawResource(urlFiles[i]);

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
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
                String sName = new String(bName);
                String sPrice = new String(bPrice);
                String sUrl = new String(bUrl);

                // "\n" 단위로 나누어 상품명, 가격, 이미지 url 배열에 저장
                String[] nameList = sName.split("\n");
                String[] priceList = sPrice.split("\n");
                String[] urlList = sUrl.split("\n");

                // JSONObject로 변경 후 내부 저장소 파일에 write
                for(int j=0; j< nameList.length; j++){
                    // 하나의 오브젝트로 사용하려다보니 같은 값이 반복되어 들어가서 새로 재생성
                    jsonObject = new JSONObject();

                    jsonObject.put("name", nameList[j]);
                    jsonObject.put("price", priceList[j]);
                    jsonObject.put("url", urlList[j]);

                    jsonArray.put(jsonObject);
                }
                wrtieFile(saveFileName, jsonArray);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 내부 저장소에 파일 쓰기
    void wrtieFile(String fileName, JSONArray msg){
        // Context.MODE_PRIVATE : 덮어쓰기
        // Context.MODE_APPEND : 이어쓰기
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE));

            for(int i=0;i<msg.length();i++){
                outputStreamWriter.write(msg.getJSONObject(i).toString());
                outputStreamWriter.write("\n");
            }
            outputStreamWriter.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 내부 저장소에 저장된 파일 읽기
    void readFile(String fileName) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;

        try {
            InputStream inputStream = openFileInput(fileName);
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String data = bufferedReader.readLine();

                // 파일 전체 읽기
                while(data != null){
                    Object obj = parser.parse(data);
                    jsonObject = (JsonObject) obj;
                }

                Log.e("HIHI", jsonObject.toString());

                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}