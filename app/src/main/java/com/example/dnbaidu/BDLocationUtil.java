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

public class BDLocationUtil {

    public BDLocationUtil(Activity currentActivity) {
        //声明LocationClient类
        LocationClient mLocationClient = new LocationClient(currentActivity);

        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

//        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(30 * 60 * 1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(30 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();//打开定位
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                //以下只列举部分获取经纬度相关（常用）的结果信息
                //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

                if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                    double latitude = bdLocation.getLatitude();    //获取纬度信息
                    double longitude = bdLocation.getLongitude();    //获取经度信息
                    float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f
                    String coorType = bdLocation.getCoorType();
                    //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
                    int errorCode = bdLocation.getLocType();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("latitude", latitude + "");
                        jsonObject.put("longitude", longitude + "");
                        jsonObject.put("radius", radius + "");
                        jsonObject.put("coorType", coorType + "");
                        jsonObject.put("errorCode", errorCode + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
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

            }
        });


//        mLocationClient.start();//打开定位
//        if (mLocationClient.isStarted()) {
//            mLocationClient.requestLocation();
//        }
    }

    private BDLocationListener mBDLocationListener;

    public void setLocationListener(BDLocationListener locationListener) {
        this.mBDLocationListener = locationListener;
    }

    public interface BDLocationListener {
        void locationSuccess(String s);

        void locationFail(String f);
    }
}
