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

    // 생성자
    public singleItemAdapter(ArrayList<singleItem> array_singleItem, Context context){
        this.context = context;
        this.array_singleItem = array_singleItem;
    }

    // 데이터 개수 리턴
    @Override
    public int getCount() {
        return array_singleItem.size();
    }

    // position 위치의 아이템 리턴
    @Override
    public Object getItem(int position) {
        return array_singleItem.get(position);
    }

    // position 위치 아이템의 id 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // i번째 아이템을 어떻게 보여줄 것인지 결정
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // LayoutInflater를 통해 layout파일 메모리에 객체화
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_list, parent, false);

        // 각 view의 내용 설정
        TextView name = view.findViewById(R.id.text_name);
        name.setText(array_singleItem.get(position).getName());

        TextView price = view.findViewById(R.id.text_price);
        price.setText(array_singleItem.get(position).getPrice());

        ImageView image = view.findViewById(R.id.imageView);
        image.setImageResource(array_singleItem.get(position).getResId());
        
        // view 반환
        return view;
    }

}
