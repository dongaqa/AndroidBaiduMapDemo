package com.dong.baidumap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2017/12/7.
 */

public class AddressSearchActivity extends Activity {

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    //定位
    public LocationClient mLocationClient = null;
    private AddressSearchActivity.MyLocationListener myListener = new AddressSearchActivity.MyLocationListener();
    LocationClientOption option;

    PopupWindow mPopupWindow;
    InfoWindow mInfoWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        initPopwindos();


        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();

        //设置定位图层
        mBaiduMap.setMyLocationConfiguration(locationConfig());
        //开启定位
        initLBS();
        mLocationClient.start();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("444","onMarkerClick");

                mInfoWindow = new InfoWindow(LayoutInflater.from(AddressSearchActivity.this)
                        .inflate(R.layout.popwindow_marker_info,null), marker.getPosition(), -47);

                mBaiduMap.showInfoWindow(mInfoWindow);



                mPopupWindow.showAtLocation(mMapView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);


                return true;
            }
        });




        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationClient.start();
            }
        });
    }



    private void initPopwindos(){
        mPopupWindow = new PopupWindow(
                LayoutInflater.from(this).inflate(R.layout.popwindow_marker_info,null),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                false);
        mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        popupWindow.setBackgroundDrawable(dw)
    }






    private MyLocationConfiguration locationConfig(){

//        mCurrentMode = LocationMode.FOLLOWING;//定位跟随态
//        mCurrentMode = LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
//        mCurrentMode = LocationMode.COMPASS;  //定位罗盘态
        MyLocationConfiguration.LocationMode locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        //是否开启方向
        boolean enableDirection = true;

        //自定义定位图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

        //精度圈填充颜色
        int accuracyCircleFillColor = Color.parseColor("#99ff0000");

        //自定义精度圈边框颜色
        int accuracyCircleStrokeColor = Color.parseColor("#11ff0000");

        MyLocationConfiguration locationConfiguration = new MyLocationConfiguration(
                locationMode,
                enableDirection,
                bitmapDescriptor,
                accuracyCircleFillColor,
                accuracyCircleStrokeColor
        );

        return locationConfiguration;
    }


    private void initLBS(){

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setCoorType("bd09ll");

        option.setScanSpan(1000);

        option.setOpenGps(true);

        mLocationClient.setLocOption(option);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            Log.e("444",latitude+" "+longitude);


            //设置当前定位位置
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            mLocationClient.stop();


            //地图移动当前定位位置
            //地理坐标基本数据结构
            LatLng latLng=new LatLng(latitude,longitude);
            //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
            MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
            //改变地图状态
            mBaiduMap.setMapStatus(msu);


            //在定位位置生成5的点
            List<OverlayOptions> options = new ArrayList<OverlayOptions>();
            options.add(addPoint(latitude+0.01,longitude+0.01,R.mipmap.point));
            options.add(addPoint(latitude+0.02,longitude+0.02,R.mipmap.point));
            options.add(addPoint(latitude+0.03,longitude+0.03,R.mipmap.point));
            options.add(addPoint(latitude-0.01,longitude-0.01,R.mipmap.point));
            options.add(addPoint(latitude-0.02,longitude-0.02,R.mipmap.point));

            mBaiduMap.addOverlays(options);
        }
    }



    private OverlayOptions addPoint(double latitude,double longitude,int resID){
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(resID);
        OverlayOptions option =  new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(bitmap);
        return option;
    }

}
