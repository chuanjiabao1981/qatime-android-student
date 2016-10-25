package cn.qatime.player.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import cn.qatime.player.base.BaseActivity;

/**
 * @author lungtify
 * @Time 2016/10/24 18:16
 * @Describe
 */
public class LocationChanceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        // 获得最好的定位效果
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        // 使用省电模式
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 获得当前的位置提供者
        String provider = locationManager.getBestProvider(criteria, true);
        // 获得当前的位置
        Location location = locationManager.getLastKnownLocation(provider);

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Logger.e("latitude"+latitude);
        Logger.e("longitude"+longitude);
    }
}
