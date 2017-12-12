package com.dong.baidumap;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView mListView;

    String[] mEnters = {"定位","地图","与地图交互","111","搜索","检查权限"};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mEnters));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jumpTo(i);
            }
        });


    }


    private void jumpTo(int position){
        switch (position){
            case 0:
                startActivity(LocationActivity.class);
                break;
            case 1:
                startActivity(MapActivity.class);
                break;
            case 2:
                startActivity(MapViewSettingActivity.class);
                break;
            case 3:
                startActivity(DrawActivity.class);
                break;
            case 4:
                startActivity(AddressSearchActivity.class);
                break;
            case 5:
                checkPermission();
                break;
        }
    }

    private void startActivity(Class clz){
        startActivity(new Intent(this,clz));
    }



    private void checkPermission(){
        //1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            //2
//            int result = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//            //PackageManager.PERMISSION_GRANTED  PackageManager.PERMISSION_DENIED
//            if (result != PackageManager.PERMISSION_GRANTED){
//                toast("没权限");
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA},666);
//            }else {
//                toast("有权限");
//            }

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                toast("上次点了不在询问");
            }else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA},666);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        toast(" "+requestCode+"  "+ Arrays.toString(permissions)+"  "+Arrays.toString(grantResults));
        Log.e("666"," "+requestCode+"  "+permissions+"  "+grantResults);
    }

    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
