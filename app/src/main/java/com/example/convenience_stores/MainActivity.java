package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button cu_btn;
    Button seven_btn;
    Button gs_btn;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cu_btn = findViewById(R.id.cu_btn);
        seven_btn = findViewById(R.id.seven_btn);
        gs_btn = findViewById(R.id.gs_btn);

        cu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectEvent.class);
                intent.putExtra("place","CU");
                startActivity(intent);
            }
        });

        seven_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectEvent.class);
                intent.putExtra("place","7ELEVEn");
                startActivity(intent);
            }
        });

        gs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectEvent.class);
                intent.putExtra("place","GS25");
                startActivity(intent);
            }
        });
    }
}