package com.dong.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by Dong on 2017/12/8.
 */

public class LocationActivity extends Activity {

    Button mBtStart,mBtStop,mBtStartTimes;
    TextView mTvLog;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    LocationClientOption option;

    boolean mIsSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mBtStart = (Button) findViewById(R.id.location_start);
        mBtStartTimes = (Button) findViewById(R.id.location_start_times);
        mBtStop = (Button) findViewById(R.id.location_stop);
        mTvLog = (TextView) findViewById(R.id.location_log);
        mTvLog.setMovementMethod(ScrollingMovementMethod.getInstance());

        mBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(0);
                mIsSingle = true;
            }
        });

        mBtStartTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(1000);
                mIsSingle = false;
            }
        });

        mBtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
                mTvLog.setText("");
            }
        });


    }




    private void initLBS(int interval){

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(interval);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        //option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        //option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        //option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false


        //option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        option.setIsNeedLocationDescribe(true);
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true

        option.setIsNeedLocationPoiList(true);
        //可选，是否需要周边POI信息，默认为不需要，即参数为false
        //如果开发者需要获得周边POI信息，此处必须为true

        mLocationClient.setLocOption(option);
    }


    public void start(int interval){
        initLBS(interval);
        mLocationClient.start();
    }


    public void stop(){
        mLocationClient.stop();
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            textViewAddText(mTvLog,"latitude: "+latitude+"     longitude: "+longitude);

            String addr = location.getAddrStr();    //获取详细地址信息
            textViewAddText(mTvLog,"AddrStr: "+addr);
            String country = location.getCountry();    //获取国家
            textViewAddText(mTvLog,"country: "+country);
            String province = location.getProvince();    //获取省份
            textViewAddText(mTvLog,"province: "+province);
            String city = location.getCity();    //获取城市
            textViewAddText(mTvLog,"city: "+city);
            String district = location.getDistrict();    //获取区县
            textViewAddText(mTvLog,"district: "+district);
            String street = location.getStreet();    //获取街道信息
            textViewAddText(mTvLog,"street: "+street);

            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
            textViewAddText(mTvLog,"locationDescribe: "+locationDescribe);

            List<Poi> poiList = location.getPoiList();
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明

            if (poiList != null){
                for (Poi poi : poiList){
                    textViewAddText(mTvLog,"Poi: "+poi.getName());
                }
            }

            textViewAddText(mTvLog,"----");

            if (mIsSingle)
                mLocationClient.stop();

        }
    }


    private void textViewAddText(TextView textView,String s){
        if (TextUtils.isEmpty(s))
            return;
        textView.setText(textView.getText()+"\n"+s);
    }
}
