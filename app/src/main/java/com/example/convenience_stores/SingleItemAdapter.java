package com.example.convenience_stores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.convenience_stores.data.SingleItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SingleItemAdapter extends BaseAdapter {
    private Context context;
    Bitmap bitmap;
    private ArrayList<SingleItem> array_singleItem = new ArrayList<>();

    // 생성자
    public SingleItemAdapter(ArrayList<SingleItem> array_singleItem, Context context){
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
//        image.setImageResource(array_singleItem.get(position).getResId());

        // 안드로이드에서 네트워크와 관련된 작업을 할 때 별도의 작업 Thread를 생성하여 작업해야 한다.
        Thread uThread = new Thread(){
            @Override
            public void run() {
                try{
                    // 객체에 저장된 url을 가지고 온다.
                    URL url = new URL(array_singleItem.get(position).getUrl());
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
            image.setImageBitmap(bitmap);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        // view 반환
        return view;
    }
}
