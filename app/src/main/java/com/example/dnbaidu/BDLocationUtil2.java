package com.example.dnbaidu;

import android.app.Activity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 雨至 on 2019/2/27.
 */

public class BDLocationUtil2 {

    public BDLocationUtil2(Activity currentActivity) {

        final LocationClient mLocationClient = new LocationClient(currentActivity);
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true); // 打开gps
        option.setScanSpan(30 * 60 * 1000);//设置设备每过30分钟获取一次定位数据，减少耗电量
//        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
        option.disableCache(true);
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                    //获取纬度信息
                    double latitude = bdLocation.getLatitude();
                    //获取经度信息
                    double longitude = bdLocation.getLongitude();

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("latitude", latitude + "");
                        jsonObject.put("longitude", longitude + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int errorCode = bdLocation.getLocType();
                    if (mBDLocationListener != null) {
                        mBDLocationListener.locationSuccess(jsonObject.toString());
                    }
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("latitude", "0");
                        jsonObject.put("longitude", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (mBDLocationListener != null) {
                        mBDLocationListener.locationFail(jsonObject.toString());
                    }
                }
                mLocationClient.stop();
            }
        });

        mLocationClient.start();//打开定位
        if (mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }

    private  BDLocationListener mBDLocationListener;

    public void setLocationListener(BDLocationListener locationListener) {
        this.mBDLocationListener = locationListener;
    }

    public interface BDLocationListener {
        void locationSuccess(String s);

        void locationFail(String f);
    }
}
