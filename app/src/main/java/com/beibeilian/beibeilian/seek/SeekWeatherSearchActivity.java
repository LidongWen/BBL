package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.beibeilian.beibeilian.R;

import java.util.List;

public class SeekWeatherSearchActivity extends Activity
		implements WeatherSearch.OnWeatherSearchListener, LocationSource, AMapLocationListener {
	private TextView forecasttv;
	private TextView reporttime1;
	private TextView reporttime2;
	private TextView weather;
	private TextView Temperature;
	private TextView wind;
	private TextView humidity;
	private WeatherSearchQuery mquery;
	private WeatherSearch mweathersearch;
	private LocalWeatherLive weatherlive;
	private LocalWeatherForecast weatherforecast;
	private List<LocalDayWeatherForecast> forecastlist = null;
	private String cityname = "北京市";// 天气搜索的城市，可以写名称或adcode；
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private AMap aMap;
	private MapView mapView;
	TextView city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_activity);

		init();

		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		Button btnback = (Button) findViewById(R.id.btnBack);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void init() {
		city = (TextView) findViewById(R.id.city);

		forecasttv = (TextView) findViewById(R.id.forecast);
		reporttime1 = (TextView) findViewById(R.id.reporttime1);
		reporttime2 = (TextView) findViewById(R.id.reporttime2);
		weather = (TextView) findViewById(R.id.weather);
		Temperature = (TextView) findViewById(R.id.temp);
		wind = (TextView) findViewById(R.id.wind);
		humidity = (TextView) findViewById(R.id.humidity);
	}

	private void searchforcastsweather() {
		mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);// 检索参数为城市和天气类型，实时天气为1、天气预报为2
		mweathersearch = new WeatherSearch(this);
		mweathersearch.setOnWeatherSearchListener(this);
		mweathersearch.setQuery(mquery);
		mweathersearch.searchWeatherAsyn(); // 异步搜索
	}

	private void searchliveweather() {
		mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);// 检索参数为城市和天气类型，实时天气为1、天气预报为2
		mweathersearch = new WeatherSearch(this);
		mweathersearch.setOnWeatherSearchListener(this);
		mweathersearch.setQuery(mquery);
		mweathersearch.searchWeatherAsyn(); // 异步搜索
	}

	/**
	 * 实时天气查询回调
	 */
	@Override
	public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
		if (rCode == AMapException.CODE_AMAP_SUCCESS) {
			if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
				weatherlive = weatherLiveResult.getLiveResult();
				reporttime1.setText(weatherlive.getReportTime() + "发布");
				weather.setText(weatherlive.getWeather());
				Temperature.setText(weatherlive.getTemperature() + "°");
				wind.setText(weatherlive.getWindDirection() + "风     " + weatherlive.getWindPower() + "级");
				humidity.setText("湿度         " + weatherlive.getHumidity() + "%");
			} else {
				ToastUtil.show(SeekWeatherSearchActivity.this, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(SeekWeatherSearchActivity.this, rCode);
		}
	}

	/**
	 * 天气预报查询结果回调
	 */
	@Override
	public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode) {
		if (rCode == AMapException.CODE_AMAP_SUCCESS) {
			if (weatherForecastResult != null && weatherForecastResult.getForecastResult() != null
					&& weatherForecastResult.getForecastResult().getWeatherForecast() != null
					&& weatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
				weatherforecast = weatherForecastResult.getForecastResult();
				forecastlist = weatherforecast.getWeatherForecast();
				fillforecast();

			} else {
				ToastUtil.show(SeekWeatherSearchActivity.this, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(SeekWeatherSearchActivity.this, rCode);
		}
	}

	private void fillforecast() {
		reporttime2.setText(weatherforecast.getReportTime() + "发布");
		String forecast = "";
		for (int i = 0; i < forecastlist.size(); i++) {
			LocalDayWeatherForecast localdayweatherforecast = forecastlist.get(i);
			String week = null;
			switch (Integer.valueOf(localdayweatherforecast.getWeek())) {
				case 1:
					week = "周一";
					break;
				case 2:
					week = "周二";
					break;
				case 3:
					week = "周三";
					break;
				case 4:
					week = "周四";
					break;
				case 5:
					week = "周五";
					break;
				case 6:
					week = "周六";
					break;
				case 7:
					week = "周日";
					break;
				default:
					break;
			}
			String temp = String.format("%-3s/%3s", localdayweatherforecast.getDayTemp() + "°",
					localdayweatherforecast.getNightTemp() + "°");
			String date = localdayweatherforecast.getDate();
			forecast += date + "  " + week + "                 " + localdayweatherforecast.getDayWeather() + "        " + temp
					+ "\n\n";
		}
		forecasttv.setText(forecast);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		// if(location!=null)
		if (location != null && location.getErrorCode() == 0) {
			cityname = location.getCity();
			city.setText(cityname);
			searchliveweather();
			searchforcastsweather();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		deactivate();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}
}
