package com.example.convenience_stores;

public class singleItem {
    String name;    // 상품이름
    String price;   // 상품가격
    int resId;      // 상품이미지 번호

    // 생성자
    public singleItem(String name, String price, int resId){
        this.name = name;
        this.price = price;
        this.resId = resId;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public void setResId(int resId){
        this.resId = resId;
    }
    public String getName(){
        return this.name;
    }
    public String getPrice(){
        return this.price;
    }
    public int getResId(){
        return this.resId;
    }
}
