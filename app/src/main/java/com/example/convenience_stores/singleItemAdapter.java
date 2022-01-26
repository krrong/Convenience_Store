package com.example.convenience_stores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class singleItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<singleItem> array_singleItem = new ArrayList<>();

    public singleItemAdapter(ArrayList<singleItem> array_singleItem, Context context){
        this.context = context;
        this.array_singleItem = array_singleItem;
    }

    @Override
    public int getCount() {
        return array_singleItem.size();
    }

    @Override
    public Object getItem(int position) {
        return array_singleItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_list, parent, false);

        TextView name = view.findViewById(R.id.text_name);
        name.setText(array_singleItem.get(position).getName());

        TextView price = view.findViewById(R.id.text_price);
        price.setText(array_singleItem.get(position).getPrice());

        ImageView image = view.findViewById(R.id.imageView);
        image.setImageResource(array_singleItem.get(position).getResId());

        return view;
    }

}
