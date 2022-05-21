package com.example.convenience_stores;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchMapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener {

    private String LOG_TAG = "SearchMapActivity";

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String BASE_URL = "https://dapi.kakao.com/";                    // 카카오 URL
    private String API_KEY = "KakaoAK a2d69db3c795c70fb4bcb73b7794abfb";    // 카카오 API 인증키
    private Button searchBtn;           // 편의점 검색 버튼
    private int radius = 500;           // 검색 반경 거리

    private double currentLatitude = 0;
    private double currentLongitude = 0;

    // 앱을 실행하기 위해 필요한 퍼미션 정의
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    
    // 편의점 이름
    private String place;

    // Snackbar를 사용하기 위해서는 view 필요
    private View mLayout;

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_map);

        initView();
        setBtnListener();

        if(!checkLocationServicesStatus()){
            // GPS 활성화
            showDialogForLocationServiceSetting();
        }
        else{
            // 런타임 퍼미션 처리
            checkRunTimePermission();

            // 현 위치 잡기
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }

    }

    // 바인딩
    private void initView(){
        // 지도 띄우기
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup)findViewById(R.id.map);
        mapViewContainer.addView(mapView);

        // override 메서드들 붙여줌
        mapView.setCurrentLocationEventListener(this);

        // Intent 로부터 편의점 이름 받아오기
        place = getIntent().getStringExtra("place");

        // 버튼 바인딩
        searchBtn = findViewById(R.id.searchBtn);

        // 현재 위치 검색 될 때까지 버튼을 사용하지 못하게 함
        searchBtn.setEnabled(false);
    }

    // 버튼 리스너 작성
    private void setBtnListener(){
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // 현재 지도 화면의 중심점을 조회한다.
//                // 반환 : 현재 지도 화면의 중심점 --> 지도를 옮기면 변한다
//                MapPoint mapPoint = mapView.getMapCenterPoint();
//
//                // MapPoint 객체가 나타내는 지점의 좌표값을 위경도 좌표시스템(WGS84)의 좌표값으로 조회한다.
//                // 반환 : 위경도 좌표시스템(WGS84)의 좌표값
//                MapPoint.GeoCoordinate geoCoordinate =  mapPoint.getMapPointGeoCoord();

//                // 현재 지도 화면 중심점의 위, 경도
//                double latitude = geoCoordinate.latitude;
//                double longitude = geoCoordinate.longitude;

                MapCircle[] mapCircles = mapView.getCircles();

                // 현재 지도에 원이 있다면 삭제
                if(mapCircles.length != 0){
                    for(MapCircle circle : mapCircles){
                        mapView.removeCircle(circle);
                    }
                }

                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(currentLatitude, currentLongitude);

                // 중심점으로부터 반지름이 radius인 추가
                MapCircle mapCircle = new MapCircle(
                        mapPoint,           // 원의 중심좌표
                        radius,             // m단위 원의 반지름
                        android.graphics.Color.argb(50, 0, 0, 0),      // 선의 색
                        android.graphics.Color.argb(100, 255, 255, 255)       // 원의 색
                );
                mapView.addCircle(mapCircle);

                Log.e(LOG_TAG, "현재 디바이스 위도 : " + Double.toString(currentLatitude));
                Log.e(LOG_TAG, "현재 디바이스 경도 : " + Double.toString(currentLongitude));

                // Retrofit 생성
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)                                  // API 기본 URL
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // 통신 인터페이스 객체로 생성
                KakaoAPI api = retrofit.create(KakaoAPI.class);

                // 검색 조건 입력
                Call<ResultSearchKeyword> call =
                        api.getSearchKeyword(
                                API_KEY,                            // 카카오 API 인증키
                                place,                              // 검색을 원하는 질의어
                                Double.toString(currentLongitude),  // longitude
                                Double.toString(currentLatitude),   // latitude
                                radius                              // 반경거리
                        );

                // API 서버에 요청
                call.enqueue(new Callback<ResultSearchKeyword>() {
                    // 통신 성공 시 -> 검색 결과는 response.body()에 담김
                    @Override
                    public void onResponse(Call<ResultSearchKeyword> call, Response<ResultSearchKeyword> response) {
                        Log.e("TEST", "Raw : " + response.raw());
                        Log.e("TEST", "Body : " + response.body().documents);

                        // 기존에 찍혀있는 마커 삭제
                        mapView.removePOIItems(mapView.getPOIItems());

                        // 받아온 위치에 마커 추가
                        for(Place document : response.body().documents){
                            // 받아온 위치 확인
                            Log.e("TEST", document.place_name);

                            // 마커 추가
                            MapPOIItem marker = new MapPOIItem();
                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(document.y), Double.parseDouble(document.x));
                            marker.setItemName(document.place_name);
                            marker.setTag(0);
                            marker.setMapPoint(mapPoint);
                            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);        // 기본 마커 모양
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 클릭 시 바뀌는 모양

                            mapView.addPOIItem(marker); // 마커 추가
                        }
                        Log.e("TEST", "마커 추가 성공");
                    }
                    // 통신 실패 시
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("TEST", "통신 실패");
                    }
                });
                
                Toast.makeText(getApplicationContext(), "검색이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 현재 위치가 리턴되면 전역으로 선언된 위, 경도 수정 및 버튼 사용 가능하도록 수정
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        currentLatitude = mapPointGeo.latitude;
        currentLongitude = mapPointGeo.longitude;
        Log.e(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));

        searchBtn.setText("주변 " + place + "검색하기");
        searchBtn.setEnabled(true);
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);

        // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            // 위치 값을 가져올 수 있음
            if(check_result){
                Log.d(LOG_TAG, "start");
            }
            // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료 -> 2 가지 경우
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();

                }
                // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있다.
                else {
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }

        }
    }

    void checkRunTimePermission(){
        // 런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크한다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(SearchMapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식한다.)
            // 3.  위치 값을 가져올 수 있음

        }
        else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요하다. 2가지 경우(3-1, 4-1)가 있다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(SearchMapActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있다.
                Toast.makeText(SearchMapActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionsResult에서 수신된다.
                ActivityCompat.requestPermissions(SearchMapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
            else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 한다.
                // 요청 결과는 onRequestPermissionsResult에서 수신된다.
                ActivityCompat.requestPermissions(SearchMapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    // 여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchMapActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(LOG_TAG, "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
//
//    // 키워드 검색 함수
//    private void searchKeyword(String keyword){
//        // Retrofit 구성
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        KakaoAPI api = retrofit.create(KakaoAPI.class);                             // 통신 인터페이스 객체로 생성
//        Call<ResultSearchKeyword> call = api.getSearchKeyword(API_KEY, keyword);    // 검색 조건 입력
//
//        // API 서버에 요청
//        call.enqueue(new Callback<ResultSearchKeyword>() {
//            // 통신 성공 시 -> 검색 결과는 response.body()에 담김
//            @Override
//            public void onResponse(Call<ResultSearchKeyword> call, Response<ResultSearchKeyword> response) {
//                Log.e("TEST", "Raw : " + response.raw());
//                Log.e("TEST", "Body : " + response.body().documents);
//
//                for(Place document : response.body().documents){
//                    Log.e("TEST", document.place_name);
//                    Log.e("TEST", document.x);
//                    Log.e("TEST", document.y);
//
//                    // 마커 추가
//                    MapPOIItem marker = new MapPOIItem();
//                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(document.y), Double.parseDouble(document.x));
//                    marker.setItemName(document.place_name);
//                    marker.setTag(0);
//                    marker.setMapPoint(mapPoint);
//                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin);        // 기본 마커 모양
//                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 클릭 시 바뀌는 모양
//
//                    mapView.addPOIItem(marker);
//                }
//                Log.e("TEST", "마커 추가 성공");
//
//            }
//
//            // 통신 실패 시
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                Log.e("TEST", "통신 실패");
//            }
//        });
//    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
        Log.e(LOG_TAG, "onCurrentLocationDeviceHeadingUpdate");
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.e(LOG_TAG, "onCurrentLocationUpdateFailed");

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.e(LOG_TAG, "onCurrentLocationUpdateCancelled");

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.e(LOG_TAG, "onMapViewInitialized");

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}
