package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.beibeilian.beibeilian.R;

import java.util.ArrayList;
import java.util.List;

public class GPSNaviActivity extends Activity implements AMapNaviListener, AMapNaviViewListener {

	protected AMapNaviView mAMapNaviView;
	protected AMapNavi mAMapNavi;
	protected TTSController mTtsManager;
	protected NaviLatLng mEndLatlng;
	protected NaviLatLng mStartLatlng;
	protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
	protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
	protected List<NaviLatLng> mWayPointList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_basic_navi);
		String slat, slng, elat, elng;
		slat=getIntent().getStringExtra("slat");
		slng=getIntent().getStringExtra("slng");
		elat=getIntent().getStringExtra("elat");
		elng=getIntent().getStringExtra("elng");
		mStartLatlng=new NaviLatLng(Double.parseDouble(slat),Double.parseDouble(slng));
		mEndLatlng=new NaviLatLng(Double.parseDouble(elat),Double.parseDouble(elng));

		mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
		mAMapNaviView.onCreate(savedInstanceState);
		mAMapNaviView.setAMapNaviViewListener(this);

		//实例化语音引擎
		mTtsManager = TTSController.getInstance(getApplicationContext());
		mTtsManager.init();

		//
		mAMapNavi = AMapNavi.getInstance(getApplicationContext());
		mAMapNavi.addAMapNaviListener(this);
		mAMapNavi.addAMapNaviListener(mTtsManager);

		//设置模拟导航的行车速度
		mAMapNavi.setEmulatorNaviSpeed(75);
		sList.add(mStartLatlng);
		eList.add(mEndLatlng);


	}

	@Override
	public void onInitNaviSuccess() {
		/**
		 * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
		 *
		 * @congestion 躲避拥堵
		 * @avoidhightspeed 不走高速
		 * @cost 避免收费
		 * @hightspeed 高速优先
		 * @multipleroute 多路径
		 *
		 *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
		 *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
		 */
		int strategy = 0;
		try {
			//再次强调，最后一个参数为true时代表多路径，否则代表单路径
			strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

	}


	@Override
	public void onCalculateRouteSuccess() {
		mAMapNavi.startNavi(NaviType.GPS);
	}

	@Override
	public void onLockMap(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onNaviBackClick() {
		// TODO Auto-generated method stub
		finish();
		return false;
	}

	@Override
	public void onNaviCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviSetting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviViewLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideCross() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideLaneInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyParallelRoad(int i) {
		if (i == 0) {
			Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
			Log.d("wlx", "当前在主辅路过渡");
			return;
		}
		if (i == 1) {
			Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();

			Log.d("wlx", "当前在主路");
			return;
		}
		if (i == 2) {
			Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();

			Log.d("wlx", "当前在辅路");
		}
	}
	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArriveDestination(NaviStaticInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArriveDestination(AMapNaviStaticInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateMultipleRoutesSuccess(int[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showCross(AMapNaviCross arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAimlessModeStatistics(AimLessModeStat arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		mAMapNaviView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mAMapNaviView.onPause();

//	        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
		mTtsManager.stopSpeaking();
		//
//	        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//	        mAMapNavi.stopNavi();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAMapNaviView.onDestroy();
		//since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
		mAMapNavi.stopNavi();
		mAMapNavi.destroy();
		mTtsManager.destroy();
	}

}
