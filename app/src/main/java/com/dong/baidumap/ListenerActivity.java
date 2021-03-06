package com.dong.baidumap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Dong on 2017/12/13.
 */

public class ListenerActivity extends Activity {

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    SDKReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        //监听地图事件监听
        IntentFilter iFilter = new IntentFilter();
        //key 验证失败
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);

        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);

        mReceiver = new SDKReceiver();

        registerReceiver(mReceiver, iFilter);

        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();


        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
        mBaiduMap.snapshot(mSnapshotReadyCallback);
        mBaiduMap.setOnMapLoadedCallback(mOnMapLoadedCallback);
        mBaiduMap.setOnMapRenderCallbadk(mOnMapRenderCallback);
        mBaiduMap.setOnMapDoubleClickListener(mOnMapDoubleClickListener);
    }

    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR))
            {
                // key 验证失败，相应处理

            }
        }
    }


    //地图状态改变
    BaiduMap.OnMapStatusChangeListener mOnMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {

        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         * @param mapStatus 地图状态改变开始时的地图状态
         */
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
            toast("onMapStatusChangeStart");
        }
        /** 因某种操作导致地图状态开始改变。
         * @param mapStatus 地图状态改变开始时的地图状态
         * @param reason 表示地图状态改变的原因，取值有：
         * 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
         * 2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
         * 3：开发者调用,导致的地图状态改变
         */
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int reason) {
            toast("onMapStatusChangeStart");
        }
        /**
         * 地图状态变化中
         * @param mapStatus 当前地图状态
         */
        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }
        /**
         * 地图状态改变结束
         * @param mapStatus 地图状态改变结束后的地图状态
         */
        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            toast("onMapStatusChangeFinish");
        }
    };


    //地图单击事件
    BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         * @param point 点击的地理坐标
         */
        public void onMapClick(LatLng point){
            toast("onMapClick");
        }
        /**
         * 地图内 Poi 单击事件回调函数
         * @param poi 点击的 poi 信息
         */
        public boolean onMapPoiClick(MapPoi poi){
            return false;
        }
    };


    //地图加载完成回调
    BaiduMap.OnMapLoadedCallback mOnMapLoadedCallback = new BaiduMap.OnMapLoadedCallback() {
        /**
         * 地图加载完成回调函数
         */
        public void onMapLoaded(){
            toast("地图加载完成回调");
        }
    };


    //地图渲染完成
    BaiduMap.OnMapRenderCallback mOnMapRenderCallback = new BaiduMap.OnMapRenderCallback() {
        @Override
        public void onMapRenderFinished() {
            toast("地图渲染完成");
        }
    };

    //地图双击事件监听
    BaiduMap.OnMapDoubleClickListener mOnMapDoubleClickListener = new BaiduMap.OnMapDoubleClickListener() {
        @Override
        public void onMapDoubleClick(LatLng latLng) {
            toast("地图双击");
        }
    };
    //地图长按事件监听
    BaiduMap.OnMapLongClickListener mOnMapLongClickListener = new BaiduMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            toast("地图长按");
        }
    };
    //地图 Marker 覆盖物点击事件监听
    BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    };
    //地图定位图标点击事件监听
    BaiduMap.OnMyLocationClickListener mOnMyLocationClickListener = new BaiduMap.OnMyLocationClickListener() {
        @Override
        public boolean onMyLocationClick() {
            return false;
        }
    };
    //地图截屏回调
    BaiduMap.SnapshotReadyCallback mSnapshotReadyCallback = new BaiduMap.SnapshotReadyCallback() {
        @Override
        public void onSnapshotReady(Bitmap bitmap) {
            toast("地图截屏回调");
        }
    };

    //触摸地图回调
    BaiduMap.OnMapTouchListener mOnMapTouchListener = new BaiduMap.OnMapTouchListener() {
        @Override
        public void onTouch(MotionEvent motionEvent) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        unregisterReceiver(mReceiver);
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



    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
