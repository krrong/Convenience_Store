package com.example.convenience_stores;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleItemView extends LinearLayout {
    TextView name;
    TextView price;
    ImageView imageView;

    public SingleItemView(Context context){
        super(context);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_list, this, true);

        name = findViewById(R.id.text_name);
        price = findViewById(R.id.text_price);
        imageView = findViewById(R.id.imageView);
    }

    public void setName(String n){
        name.setText(n);
    }

    public void setPrice(String p){
        price.setText(p);
    }
    public void setImageView(int resId){
        imageView.setImageResource(resId);
    }
}
