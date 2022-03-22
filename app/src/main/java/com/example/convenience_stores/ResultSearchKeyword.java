package com.example.convenience_stores;


import java.util.List;

public class ResultSearchKeyword {
    List<Place> documents;      // 검색 결과
}

class Place{
    String place_name;          // 장소명
    String address_name;        // 전체 번지 주소
    String road_address_name;   // 전체 도로명 주소
    String x;                   // X 좌표값 or longitude
    String y;                   // Y 좌표값 or latitude
}
