package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.beibeilian.beibeilian.MainActivity;
import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.seek.adapter.SeekPersonListAdapter;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeekNearbyActivity extends Activity implements LocationSource,
		AMapLocationListener {
	//	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private AMap aMap;
	private MapView mapView;
	private static List<UserInfo> personlist = new ArrayList<UserInfo>();

	private int PageNumber = 0;

	private ListView Listview;

	private SeekPersonListAdapter seekPersonListAdapter;

	private InitLoadThread initLoadThread;

	private Dialog dialog;

	private Button btnBack;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_nearby);
		Listview = (ListView) findViewById(R.id.listview);
		btnBack = (Button) findViewById(R.id.btnBack);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		dialog = new Dialog(SeekNearbyActivity.this, R.style.theme_dialog_alert);
		seekPersonListAdapter = new SeekPersonListAdapter(
				SeekNearbyActivity.this, personlist);
		HelperUtil
				.customDialogShow(dialog, SeekNearbyActivity.this, "正在加载中...");

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SeekNearbyActivity.this,
						MainActivity.class));
				finish();
			}
		});

//		Listview.setOnLoadListener(this);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
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

	private String lat;
	private String lon;

	private class InitLoadThread extends Thread {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pagenumber", String.valueOf(PageNumber));
				map.put("lat", lat);
				map.put("lon", lon);
				android.os.Message message = handler.obtainMessage();
				message.obj = HelperUtil.postRequest(
						HttpConstantUtil.FindNearbyList, map);
				message.what = 1;
				message.sendToTarget();
			} catch (Exception e) {
				// TODO: handle exception
				handler.sendEmptyMessage(PublicConstant.JsonCatch);
			}
		}
	}

	private void parseJson(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			if (jsonArray.length() > 0) {
				if (PageNumber == 0) {
					if (personlist.size() > 0) {
						personlist.clear();
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					String username = jsonArray.optJSONObject(i).optString(
							"username");
					String year = jsonArray.optJSONObject(i).optString(
							"birthday");
					String photo = jsonArray.optJSONObject(i)
							.optString("photo");
//					String state = jsonArray.optJSONObject(i)
//							.optString("state");
					String monologue = jsonArray.optJSONObject(i).optString(
							"heartdubai");
					String place = jsonArray.optJSONObject(i)
							.optString("lives");
					String nickname = jsonArray.optJSONObject(i).optString(
							"nickname");
//					String logintime = jsonArray.optJSONObject(i).optString(
//							"time");
					String distance = jsonArray.optJSONObject(i).optString(
							"distance");
					String sex = jsonArray.optJSONObject(i).optString("sex");

					int heartduibaistate = jsonArray.optJSONObject(i).optInt("heartduibaistate");
					UserInfo model = new UserInfo();
					model.setBirthday(year);
					model.setHeartdubai(monologue);
					model.setLives(place);
					model.setNickname(nickname);
					model.setPhoto(photo);
//					model.setState(state);
					model.setUsername(username);
//					model.setTime(logintime);
					model.setDistance(distance);
					model.setSex(sex);
					model.setHeartduibaistate(heartduibaistate);
					personlist.add(model);
				}
				if(Listview.getAdapter()==null)
				{
					Listview.setAdapter(seekPersonListAdapter);
				}
				else
				{
					if(seekPersonListAdapter!=null)
					{
						seekPersonListAdapter.notifyDataSetChanged();
					}
				}
			}
			if(dialog!=null)
			{
				dialog.dismiss();
			}

		} catch (Exception e) {
			if(dialog!=null)
			{
				dialog.dismiss();
			}
		}

	}

	private int RECORDPOST = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

				case 1:
					parseJson((String) msg.obj);
					break;
				case PublicConstant.JsonCatch:
					if(dialog!=null)
					{
						dialog.dismiss();
					}
					break;
				case 10:
					if (RECORDPOST != 0)
						return;
					else {
						if (initLoadThread != null) {
							initLoadThread.interrupt();
						}
						initLoadThread = new InitLoadThread();
						initLoadThread.start();
						RECORDPOST = 1;
					}
					break;
				case 11:
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (dialog != null) {
								dialog.dismiss();
							}
//						HelperUtil.totastShow("请检查获取位置权限是否被第三方软件限制或您的附近没有人哦",
//								SeekNearbyActivity.this);
						}
					}, 10000);


					break;
				default:
					break;
			}

		}
	};


	@Override
	public void onDestroy() {
		super.onDestroy();
		RECORDPOST = 0;
		if (dialog != null) {
			dialog.dismiss();
		}

		if (initLoadThread != null) {
			initLoadThread.interrupt();
		}
		deactivate();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		return false;
	}


	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			//设置定位参数
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
		lat = String.valueOf(location.getLatitude());
		lon = String.valueOf(location.getLongitude());
		Log.e("test", location.getCity());
		if (HelperUtil.flagISNoNull(lat) && lat.equals("0.0")
				&& HelperUtil.flagISNoNull(lon) && lon.equals("0.0")) {
//			handler.sendEmptyMessage(11);
			lat="43.114638";
			lon="128.913603";
		}
		handler.sendEmptyMessage(10);
	}


//	@Override
//	public void onLoad(PullableListView pullableListView) {
//		// TODO Auto-generated method stub
//		
//		mPullableListView=pullableListView;
//		
//		PageNumber++;
//
//		if (initLoadThread != null) {
//			initLoadThread.interrupt();
//		}
//		initLoadThread = new InitLoadThread();
//		initLoadThread.start();
//
//	}
}
