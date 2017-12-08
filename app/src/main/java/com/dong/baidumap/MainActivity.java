package com.dong.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    String[] mEnters = {"定位","地图","与地图交互","绘制"};


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
        }
    }

    private void startActivity(Class clz){
        startActivity(new Intent(this,clz));
    }

}
