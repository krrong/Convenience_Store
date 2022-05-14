package com.example.convenience_stores.data;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleItem implements Parcelable {
    String name;    // 상품이름
    String price;   // 상품가격
    String url;     // 상품이미지 저장링크

    // 생성자
    public SingleItem(String name, String price, String url){
        this.name = name;
        this.price = price;
        this.url = url;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getName(){
        return this.name;
    }
    public String getPrice(){
        return this.price;
    }
    public String getUrl(){ return this.url; }

    protected SingleItem(Parcel parcel){
        name = parcel.readString();
        price = parcel.readString();
        url = parcel.readString();
    }

    public static final Creator<SingleItem> CREATOR = new Creator<SingleItem>() {
        @Override
        public SingleItem createFromParcel(Parcel parcel) {
            return new SingleItem(parcel);
        }

        @Override
        public SingleItem[] newArray(int size) {
            return new SingleItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(url);
    }
}
