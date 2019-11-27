package com.example.dnbaidu;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.tv_info);

        RxPermissionsTool.
                with(this).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
    }

    public void startLocation(View view) {


        Log.e(TAG, "获取经纬度---------start---------");
        BDLocationUtil2 bdLocationUtil = new BDLocationUtil2(this);
        bdLocationUtil.setLocationListener(new BDLocationUtil2.BDLocationListener() {
            @Override
            public void locationSuccess(String s) {
                Log.e(TAG, "获取经纬度成功->" + s);
                tvInfo.setText(s);
            }

            @Override
            public void locationFail(String f) {
                Log.e(TAG, "获取经纬度失败->" + f);
                tvInfo.setText(f);
            }
        });
    }
}
