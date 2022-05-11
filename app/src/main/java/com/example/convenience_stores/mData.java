package com.example.convenience_stores;

import android.os.Parcel;
import android.os.Parcelable;

public class mData implements Parcelable {
    private String[] nameList;
    private String[] priceList;
    private String[] urlList;

    public mData(){
        nameList = null;
        priceList = null;
        urlList = null;
    }

    public mData(String[] nameList, String[] priceList, String[] urlList){
        this.nameList = nameList;
        this.priceList = priceList;
        this.urlList = urlList;
    }

    public void setNameList(String[] nameList){
        this.nameList=nameList;
    }

    public void setPriceList(String[] priceList){
        this.priceList=priceList;
    }

    public void setUrlList(String[] urlList){
        this.urlList=urlList;
    }

    public String[] getNameList(){
        return this.nameList;
    }

    public String[] getPriceList(){
        return this.priceList;
    }

    public String[] getUrlList(){
        return this.urlList;
    }

    // Creator가 사용하는 생성자
    protected mData(Parcel in) {
        nameList = in.createStringArray();
        priceList = in.createStringArray();
        urlList = in.createStringArray();
    }

    // Creator는 Parcelable에서 필수적으로 가져야 하는 non-null static 필드다.
    public static final Creator<mData> CREATOR = new Creator<mData>() {
        @Override
        public mData createFromParcel(Parcel in) {
            return new mData(in);
        }

        @Override
        public mData[] newArray(int size) {
            return new mData[size];
        }
    };

    // FILE_DESCRIPTOR 외 경우 수정 필요하지 않음
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(nameList);
        parcel.writeStringArray(priceList);
        parcel.writeStringArray(urlList);
    }
}

