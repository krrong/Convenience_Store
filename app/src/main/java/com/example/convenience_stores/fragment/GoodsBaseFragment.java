package com.example.convenience_stores.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.convenience_stores.R;
import com.example.convenience_stores.data.SingleItem;
import com.example.convenience_stores.adapter.ItemAdapter;

import java.io.InputStream;
import java.util.ArrayList;

public class GoodsBaseFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_good_base, container, false);

        // 리사이클러뷰 바인딩 및 어댑터와 연결
        recyclerView = (RecyclerView)viewGroup.findViewById(R.id.baseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return viewGroup;
    }

    /**
     * adapter 에 파싱된 데이터 셋팅 (화면 갱신)
     * @param goodList
     */
    public void setData(ArrayList<SingleItem> goodList) {
        if (recyclerView == null)
            return;

        // 리사이클러뷰의 어댑터를 받아와서 데이터 수정
        ItemAdapter adapter = new ItemAdapter(goodList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * recyclerView 에 붙은 adapter 리턴 
     */
    public ItemAdapter getAdapter() {
        // 리사이클러뷰가 없거나 연결된 어댑터가 없으면 종료
        if (recyclerView == null || recyclerView.getAdapter() == null)
            return null;

        // 리사이클러뷰의 어댑터 리턴
        return (ItemAdapter) recyclerView.getAdapter();
    }

    /**
     * 편의점, 이벤트에 따른 파일 데이터 불러와 파싱
     */
    public void dataParsing(String place, String event) {
        InputStream inName = getResources().openRawResource(getNameRawResourceId(place, event));
        InputStream inPrice = getResources().openRawResource(getPriceRawResourceId(place, event));
        InputStream inUrl = getResources().openRawResource(getUrlRawResourceId(place, event));

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

            // fragment에 넘겨줄 데이터를 items에 담음
            ArrayList<SingleItem> items = new ArrayList<>();
            for(int i = 0; i < nameList.length; i++) {
                items.add(new SingleItem(nameList[i], priceList[i], urlList[i]));
            }

            setData(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  ============================================================================================
    // TODO : JSON 파일 하나로 통합된다면 아래 함수중 1개만 남고 사라지겠지?
    private int getNameRawResourceId(String place, String event) {
        switch(place) {
            case "CU":
                switch (event) {
                    case "1+1":
                        return R.raw.cu_11_name;
                    case "2+1":
                        return R.raw.cu_21_name;
                }
                break;
            case "7ELEVEn":
                switch (event) {
                    case "1+1":
                        return R.raw.seven_11_name;
                    case "2+1":
                        return R.raw.seven_21_name;
                }
                break;
            case "GS25":
                switch (event) {
                    case "1+1":
                        return R.raw.gs_11_name;
                    case "2+1":
                        return R.raw.gs_21_name;
                }
                break;
        }
        return 0;
    }

    private int getPriceRawResourceId(String place, String event) {
        switch(place) {
            case "CU":
                switch (event) {
                    case "1+1":
                        return R.raw.cu_11_price;
                    case "2+1":
                        return R.raw.cu_21_price;
                }
                break;
            case "7ELEVEn":
                switch (event) {
                    case "1+1":
                        return R.raw.seven_11_price;
                    case "2+1":
                        return R.raw.seven_21_price;
                }
                break;
            case "GS25":
                switch (event) {
                    case "1+1":
                        return R.raw.gs_11_price;
                    case "2+1":
                        return R.raw.gs_21_price;
                }
                break;
        }
        return 0;
    }

    private int getUrlRawResourceId(String place, String event) {
        switch(place) {
            case "CU":
                switch (event) {
                    case "1+1":
                        return R.raw.cu_11_link;
                    case "2+1":
                        return R.raw.cu_21_link;
                }
                break;
            case "7ELEVEn":
                switch (event) {
                    case "1+1":
                        return R.raw.seven_11_link;
                    case "2+1":
                        return R.raw.seven_21_link;
                }
                break;
            case "GS25":
                switch (event) {
                    case "1+1":
                        return R.raw.gs_11_link;
                    case "2+1":
                        return R.raw.gs_21_link;
                }
                break;
        }
        return 0;
    }
}
