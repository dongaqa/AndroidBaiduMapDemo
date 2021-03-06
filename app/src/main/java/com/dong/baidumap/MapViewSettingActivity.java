package com.dong.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;

/**
 * Created by Dong on 2017/12/8.
 */

public class MapViewSettingActivity extends Activity implements View.OnClickListener {


    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;


    LogoPosition[] mLogoPositions = {
            LogoPosition.logoPostionleftBottom,
            LogoPosition.logoPostionleftTop,
            LogoPosition.logoPostionCenterBottom,
            LogoPosition.logoPostionCenterTop,
            LogoPosition.logoPostionRightBottom,
            LogoPosition.logoPostionRightTop};
    int position =0;

    boolean compassEnable = false;
    boolean scaleEnable = true;
    boolean zoomControlsEnable = true;

    boolean zoomGesturesEnable = true;
    boolean rotateEnable = true;
    boolean gesturesControlsEnable = true;

    boolean mapPioEnable = true;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview_setting);

        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();

        findViewById(R.id.logo_position).setOnClickListener(this);
        findViewById(R.id.compass).setOnClickListener(this);
        findViewById(R.id.scale).setOnClickListener(this);
        findViewById(R.id.zoomControls).setOnClickListener(this);

        findViewById(R.id.ZoomGestures).setOnClickListener(this);
        findViewById(R.id.Rotate).setOnClickListener(this);
        findViewById(R.id.Gestures).setOnClickListener(this);

        findViewById(R.id.MapPoi).setOnClickListener(this);
        findViewById(R.id.big).setOnClickListener(this);
        findViewById(R.id.small).setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zoomControls:
                zoomControlsEnable = !zoomControlsEnable;
                mMapView.showZoomControls(zoomControlsEnable);
                break;
            case R.id.logo_position:
                position++;
                if (position >= mLogoPositions.length)
                    position = 0;
                mMapView.setLogoPosition(mLogoPositions[position]);
                break;
            case R.id.compass:
                compassEnable = !compassEnable;
                UiSettings uiSettings = mBaiduMap.getUiSettings();
                uiSettings.setCompassEnabled(compassEnable);
                break;
            case R.id.scale:
                scaleEnable = !scaleEnable;
                mMapView. showScaleControl(scaleEnable);
                break;
            case R.id.ZoomGestures:
                zoomGesturesEnable = !zoomGesturesEnable;
                //控制是否启用或禁用缩放手势，默认开启。如果启用，用户可以双指点击或缩放地图视图。
                mBaiduMap.getUiSettings().setZoomGesturesEnabled(zoomGesturesEnable);
                break;
            case R.id.Rotate:
                rotateEnable = !rotateEnable;
                //控制是否启用或禁用地图旋转功能，默认开启。如果启用，则用户可使用双指 旋转来旋转地图。
                mBaiduMap.getUiSettings().setRotateGesturesEnabled(rotateEnable);
                break;
            case R.id.Gestures:
                gesturesControlsEnable = !gesturesControlsEnable;
                //控制是否一并禁止所有手势，默认关闭。如果启用，所有手势都将被禁用。
                mBaiduMap.getUiSettings().setAllGesturesEnabled(gesturesControlsEnable);
                break;
            case R.id.MapPoi:
                mapPioEnable = !mapPioEnable;
                //控制是否一并禁止所有手势，默认关闭。如果启用，所有手势都将被禁用。
                mBaiduMap.showMapPoi(mapPioEnable);
                break;
            case R.id.big:
                //跳到指定级别
                //mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(zoom));
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.small:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }
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

}
