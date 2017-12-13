package com.dong.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2017/12/9.
 */

public class DrawActivity extends Activity implements View.OnClickListener{

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    //地理坐标基本数据结构
    LatLng mLatLng=new LatLng(0,0);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();

        //地图显示对应经纬度
        MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(mLatLng);
        mBaiduMap.setMapStatus(msu);

        mMapView.showZoomControls(false);

        findViewById(R.id.marker).setOnClickListener(this);
        findViewById(R.id.line).setOnClickListener(this);
        findViewById(R.id.polygon).setOnClickListener(this);
        findViewById(R.id.text).setOnClickListener(this);


        findViewById(R.id.info_window).setOnClickListener(this);
        findViewById(R.id.over_layout).setOnClickListener(this);
        findViewById(R.id.clean).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.marker:
                addPointsNearby(mLatLng);
                break;
            case R.id.line:
                drawLine(mLatLng);
                break;
            case R.id.polygon:
                drawPolygon(mLatLng);
                break;
            case R.id.info_window:
                drawInfoWindow();
                break;
            case R.id.text:
                drawText();
                break;
            case R.id.over_layout:
                drawLayout(mLatLng);
                break;
            case R.id.clean:
                mBaiduMap.clear();
                break;
        }
    }




    //在位置附近画点
    private void addPointsNearby(LatLng latLng){
        //创建OverlayOptions的集合

        List<OverlayOptions> options = new ArrayList<>();
        //设置坐标点

        LatLng point1 = new LatLng(latLng.longitude+0.05, latLng.longitude+0.05);
        LatLng point2 = new LatLng(latLng.longitude-0.05, latLng.longitude-0.05);


        BitmapDescriptor bdA = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location);

        //创建OverlayOptions属性

        MarkerOptions option1 =  new MarkerOptions()
                .position(point1)
                .icon(bdA)
                .draggable(true);

        OverlayOptions option2 =  new MarkerOptions()
                .position(point2)
                .icon(bdA);

        //动画
        //Marker帧动画
//        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//
//        giflist.add(bdA);
//        giflist.add(bdB);
//        giflist.add(bdC);
//
//        OverlayOptions ooD = new MarkerOptions().position(pt).icons(giflist)
//                .zIndex(0).period(10);
        //系统动画
        option1.animateType(MarkerOptions.MarkerAnimateType.grow);




        //将OverlayOptions添加到list
        options.add(option1);
        options.add(option2);


        //在地图上批量添加
        mBaiduMap.addOverlays(options);
    }

    private void drawLine(LatLng latLng){
        //构建折线点坐标
        LatLng p1 = new LatLng(latLng.longitude+0.05, latLng.longitude+0.05);
        LatLng p2 = new LatLng(latLng.longitude+0.03, latLng.longitude+0.07);
        LatLng p3 = new LatLng(latLng.longitude-0.05, latLng.longitude-0.05);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        points.add(p3);

        //绘制折线
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        Polyline polyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);

        //虚线
        polyline.setDottedLine(true);
    }

    private void drawPolygon(LatLng latLng){
        LatLng pt1 = new LatLng(latLng.longitude+0.05, latLng.longitude+0.05);
        LatLng pt2 = new LatLng(latLng.longitude+0.06, latLng.longitude+0.06);
        LatLng pt3 = new LatLng(latLng.longitude+0.11, latLng.longitude+0.09);
        LatLng pt4 = new LatLng(latLng.longitude+0.05, latLng.longitude+0.01);
        LatLng pt5 = new LatLng(latLng.longitude-0.05, latLng.longitude+0.03);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);

//构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);

//在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
    }


    private void drawText(){
        //构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(24)
                .fontColor(0xFFFF00FF)
                .text("百度地图SDK")
                .rotate(-30)
                .position(mLatLng);

        //在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);
    }


    private void drawInfoWindow(){
//创建InfoWindow展示的view
        Button button = new Button(getApplicationContext());
        button.setText("Button");


//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, mLatLng, -47);

//显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);

    }


    private void drawLayout(LatLng latLng){
        //定义Ground的显示地理范围
        LatLng pt1 = new LatLng(latLng.longitude-0.10, latLng.longitude-0.10);
        LatLng pt2 = new LatLng(latLng.longitude+0.2, latLng.longitude+2);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(pt1)
                .include(pt2)
                .build();

//定义Ground显示的图片
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.mipmap.bg_mine1);

//定义Ground覆盖物选项
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.8f);

//在地图中添加Ground覆盖物
        mBaiduMap.addOverlay(ooGround);
    }

}
