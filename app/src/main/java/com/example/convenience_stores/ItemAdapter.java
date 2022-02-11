package com.example.convenience_stores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<singleItem> items = new ArrayList<>();    // 어댑터에 들어갈 list
    Bitmap bitmap;  // 이미지 비트맵

    // ViewHolder
    // 여기서 subView를 setting해줘야 함
    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView price;

        ViewHolder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.text_name);
            price = itemView.findViewById(R.id.text_price);
        }

        // bind해주는 함수
        void onBind(singleItem item){
            name.setText(item.getName());
            price.setText(item.getPrice());

            // 안드로이드에서 네트워크와 관련된 작업을 할 때 별도의 작업 Thread를 생성하여 작업해야 한다.
            Thread uThread = new Thread(){
                @Override
                public void run() {
                    try{
                        // 객체에 저장된 url을 가지고 온다.
                        URL url = new URL(item.getUrl());
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                        conn.setDoInput(true);  // 서버통신에서 입력 가능한 상태로 만든다.
                        conn.connect();         // 연결된 곳에 접속

                        InputStream is = conn.getInputStream();     // inputStream 값 가져오기
                        bitmap = BitmapFactory.decodeStream(is);    // bitmap으로 변환
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }
            };
            // 작업 Thread 시작
            uThread.start();

            try{
                // join() : 별도의 작업 Thread가 종료될 때까지 메인 Thread대기 시킴
                // -> InterruptException 발생시킴

                uThread.join();
                imageView.setImageBitmap(bitmap);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    ItemAdapter(ArrayList<singleItem> list) {
        items = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 test.xml inflate
        // ViewHolder 반환
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        // item을 하나, 하나 보여주는 함수(bind)
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수
        return items.size();
    }


}


