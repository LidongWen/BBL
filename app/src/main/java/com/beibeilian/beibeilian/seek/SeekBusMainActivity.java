package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.SweetAlertDialog;

public class SeekBusMainActivity extends Activity implements LocationSource, AMapLocationListener {

	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private AMap aMap;
	private MapView mapView;
	TextView tv_sloaction, tv_eloaction;
	double slat, slng, elat, elng;
	SweetAlertDialog mSweetAlertDialog;
	Button btnLocation;
	class LocationReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String lat = intent.getStringExtra("lat");
			String lng = intent.getStringExtra("lng");
			if (lat != null)
				elat = Double.parseDouble(lat);
			if (lng != null)
				elng = Double.parseDouble(lng);
			tv_eloaction.setText(intent.getStringExtra("address"));
		}
	}

	IntentFilter mIntentFilter;
	LocationReceiver mLocationReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_bus_main);
		mSweetAlertDialog=new SweetAlertDialog(SeekBusMainActivity.this);
		btnLocation=(Button)findViewById(R.id.btn_location_id);
		btnLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSweetAlertDialog.setTitleText("请稍候...");
				mSweetAlertDialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mlocationClient.startLocation();
					}
				}).start();

			}
		});
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写t
		mSweetAlertDialog.setTitleText("请稍候...");
		mSweetAlertDialog.show();
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		tv_sloaction = (TextView) findViewById(R.id.tv_province_id);
		tv_eloaction = (TextView) findViewById(R.id.tv_year_id);
		mIntentFilter = new IntentFilter();
		mLocationReceiver = new LocationReceiver();
		mIntentFilter.addAction(ReceiverConstant.LOCATION_MESSAGE_ACTION);
		registerReceiver(mLocationReceiver, mIntentFilter);

		Button btnNav = (Button) findViewById(R.id.btn_search_id);
		btnNav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(slat<=0)
				{
					HelperUtil.totastShow("请先定位当前位置",getApplicationContext());
					return;
				}
				if(elat<=0)
				{
					HelperUtil.totastShow("请选择终点位置",getApplicationContext());
					return;
				}
				Intent mIntent = new Intent(SeekBusMainActivity.this, BusRouteActivity.class);
				mIntent.putExtra("slat", String.valueOf(slat));
				mIntent.putExtra("slng", String.valueOf(slng));
				mIntent.putExtra("elat", String.valueOf(elat));
				mIntent.putExtra("elng", String.valueOf(elng));
				startActivity(mIntent);
			}
		});
		tv_eloaction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SeekBusMainActivity.this,SeekPoiKeywordSearchActivity.class));
			}
		});
		Button btnback = (Button) findViewById(R.id.btnBack);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

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
			tv_sloaction.setText(location.getAddress());
			slat = location.getLatitude();
			slng = location.getLongitude();
			if(mSweetAlertDialog!=null) mSweetAlertDialog.dismiss();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		deactivate();
		if(mLocationReceiver!=null) unregisterReceiver(mLocationReceiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			finish();
		}
		return false;
	}
}
