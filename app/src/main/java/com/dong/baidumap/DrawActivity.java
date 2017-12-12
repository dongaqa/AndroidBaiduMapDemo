package com.dong.baidumap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2017/12/9.
 */

public class DrawActivity extends Activity {

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    //定位
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    LocationClientOption option;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);


        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();

        //设置定位图层
        mBaiduMap.setMyLocationConfiguration(locationConfig());
        //开启定位
        initLBS();
        mLocationClient.start();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);








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


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            Log.e("444",latitude+" "+longitude);
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            mLocationClient.stop();



            //地理坐标基本数据结构
            LatLng latLng=new LatLng(latitude,longitude);
            //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
            MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latLng);
            //改变地图状态
            mBaiduMap.setMapStatus(msu);

            //点标记
            addPointsNearby(location);
        }
    }


    //在位置附近添加几个点
    private void addPointsNearby(BDLocation location){
        //创建OverlayOptions的集合

        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
//设置坐标点

        LatLng point1 = new LatLng(location.getLatitude()+0.05, location.getLongitude()+0.05);
        LatLng point2 = new LatLng(location.getLatitude()-0.05, location.getLongitude()-0.05);


        BitmapDescriptor bdA = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location);

//创建OverlayOptions属性

        OverlayOptions option1 =  new MarkerOptions()
                .position(point1)
                .icon(bdA)
                .draggable(true);
        OverlayOptions option2 =  new MarkerOptions()
                .position(point2)
                .icon(bdA);
//将OverlayOptions添加到list
        options.add(option1);
        options.add(option2);
        //在地图上批量添加
        mBaiduMap.addOverlays(options);
    }


}
