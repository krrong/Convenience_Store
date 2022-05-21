package com.example.convenience_stores;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoAPI {
    @GET("v2/local/search/keyword.json")    // Keyword.json 의 정보를 받아옴

    // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김
    Call<ResultSearchKeyword> getSearchKeyword(
            @Header("Authorization") String key,    // 카카오 API 인증키
            @Query("query") String query,           // 검색을 원하는 질의어
            @Query("x") String x,                   // longitude
            @Query("y") String y,                   // latitude
            @Query("radius") int radius);           // 반경거리
}
