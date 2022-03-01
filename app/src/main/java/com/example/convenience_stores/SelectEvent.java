package com.example.convenience_stores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SelectEvent extends AppCompatActivity {
    Button btn_11;
    Button btn_21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);

        btn_11 = findViewById(R.id.btn_11);
        btn_21 = findViewById(R.id.btn_21);

        Intent intent = getIntent();
        String place = intent.getStringExtra("place");

        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), cu.class);
                intent.putExtra("place",place);
                intent.putExtra("event","11");
                Log.e("test", "1+1버튼 클릭");
                startActivity(intent);
            }
        });

        btn_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), cu.class);
                intent.putExtra("place",place);
                intent.putExtra("event","21");
                startActivity(intent);
            }
        });
    }
}