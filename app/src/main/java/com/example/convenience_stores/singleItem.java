package com.example.convenience_stores;

public class singleItem {
    String name;    // 상품이름
    String price;   // 상품가격
    String url;      // 상품이미지 번호

    // 생성자
    public singleItem(String name, String price, String url){
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
}
