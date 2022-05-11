package com.example.convenience_stores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private ArrayList<singleItem> items;    // 어댑터에 들어갈 list

    // ViewHolder
    // 여기서 subView를 setting해줘야 함
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView price;

        ItemViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.text_name);
            price = itemView.findViewById(R.id.text_price);
        }

        // bind 해주는 함수
        void onBind(singleItem item){
            name.setText(item.getName());
            price.setText(item.getPrice());

            URL imageUrl = null;
            try {
                imageUrl = new URL(item.getUrl());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Glide
                    .with(imageView.getContext())
                    .load(imageUrl.toString())
                    .placeholder(R.drawable.img_loading)
                    .into(imageView);
        }
    }

    // 로딩바
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    // 생성자
    ItemAdapter(ArrayList<singleItem> list) {
        this.items = list;
    }

    // 인자로 받은 리스트로 수정
    public void setItems(ArrayList<singleItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            // LayoutInflater를 이용하여 test.xml inflate
            // ViewHolder 반환
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new ItemViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            // item을 하나, 하나 보여주는 함수(bind)
            ((ItemViewHolder) holder).onBind(items.get(position));
        }
        else if(holder instanceof LoadingViewHolder){
           showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    // 게시물과 프로그레스바 아이템 뷰 구분
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    // RecyclerView 아이템의 총 개수
    public int getItemCount() {
        return items.size();
    }

    // 어댑터의 아이템 리스트에 추가 
    public void addItem(singleItem item) {
        items.add(item);
    }

    // 어댑터의 아이템 리스트 반환 
    public ArrayList<singleItem> getItems() {
        return items;
    }

    // 검색 기능을 위해 어댑터의 리스트 변경
    public void filterList(ArrayList<singleItem> list){
        items = list;
        notifyDataSetChanged();
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {

    }
}



