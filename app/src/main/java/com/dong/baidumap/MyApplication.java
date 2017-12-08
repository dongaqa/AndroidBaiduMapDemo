package com.dong.baidumap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Dong on 2017/12/6.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       SDKInitializer.initialize(this);
    }
}
