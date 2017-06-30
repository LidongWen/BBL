package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.seek.qaq.CircleMainActivity;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.PreferencesUtils;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.SweetAlertDialog;

public class SeekActivity extends Activity implements OnClickListener {
	private RelativeLayout seek_matrimonial;
	private RelativeLayout seek_search;
	private RelativeLayout seek_nav;
	private RelativeLayout seek_app;
	private RelativeLayout seek_qaq;
	private RelativeLayout seek_nearby;
	private RelativeLayout seek_group;
	private RelativeLayout seek_nearlive;
	private RelativeLayout seek_weather;
	private RelativeLayout seek_ball;

	private Intent intent;
	private RelativeLayout seek_visit;
	private IntentFilter mIntentFilter;
	private RemianReceiver mRemianReceiver;

	BBLDao dao;
	SweetAlertDialog dialog;
	TextView tvgroup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek);
		ExitApplication.addActivity(SeekActivity.this);
		dao=new BBLDao(SeekActivity.this,null, null, 1);
		dialog=new SweetAlertDialog(SeekActivity.this);
		seek_matrimonial = (RelativeLayout) findViewById(R.id.seek_matrimonial_id);
		seek_search = (RelativeLayout) findViewById(R.id.seek_search_id);
		seek_visit = (RelativeLayout) findViewById(R.id.seek_visit_id);
		seek_nav = (RelativeLayout) findViewById(R.id.seek_nav_id);
		seek_app = (RelativeLayout) findViewById(R.id.seek_app_id);
		seek_qaq = (RelativeLayout) findViewById(R.id.seek_qaq_id);
		seek_nearby = (RelativeLayout) findViewById(R.id.seek_nearby_id);
		seek_group = (RelativeLayout) findViewById(R.id.seek_group_id);
		seek_nearlive = (RelativeLayout) findViewById(R.id.seek_nearlive_id);
		seek_weather = (RelativeLayout) findViewById(R.id.seek_weather_id);
		seek_ball = (RelativeLayout) findViewById(R.id.seek_ball_id);

		tvgroup=(TextView) findViewById(R.id.xq_group_remind_id);
		seek_matrimonial.setOnClickListener(this);
		seek_nav.setOnClickListener(this);
		seek_visit.setOnClickListener(this);
		seek_search.setOnClickListener(this);
		seek_app.setOnClickListener(this);
		seek_qaq.setOnClickListener(this);
		seek_nearby.setOnClickListener(this);
		seek_group.setOnClickListener(this);
		seek_nearlive.setOnClickListener(this);
		seek_weather.setOnClickListener(this);
		seek_ball.setOnClickListener(this);
		mIntentFilter = new IntentFilter();
		mRemianReceiver = new RemianReceiver();
		mIntentFilter.addAction(ReceiverConstant.TAB_THREE_REMAIND_ACTION);
		registerReceiver(mRemianReceiver, mIntentFilter);

	}

	private class RemianReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ReceiverConstant.TAB_THREE_REMAIND_ACTION)) {
				String type = intent.getStringExtra("type");
				if (type.equals(PublicConstant.GROUPTYPE)) {
					updateGroupstate();
					tvgroup.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	public void updateGroupstate() {
		PreferencesUtils.putInt(SeekActivity.this, "groupstate", 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.seek_matrimonial_id:
				intent = new Intent(SeekActivity.this, SeekMatrimonialActivity.class);
				startActivity(intent);
				break;
			case R.id.seek_search_id:
				intent = new Intent(SeekActivity.this, SeekSearchActivity.class);
				startActivity(intent);
				break;
			case R.id.seek_nav_id:
				intent = new Intent(SeekActivity.this, SeekNavMainActivity.class);
				startActivity(intent);
				break;
			case R.id.seek_visit_id:
				intent = new Intent(SeekActivity.this, SeekBusMainActivity.class);
				startActivity(intent);
				break;
			case R.id.seek_app_id:
				break;
			case R.id.seek_qaq_id:
				startActivity(new Intent(SeekActivity.this, CircleMainActivity.class));
				break;
			case R.id.seek_nearby_id:
				startActivity(new Intent(SeekActivity.this, SeekNearbyActivity.class));
				break;
			case R.id.seek_group_id:
				PreferencesUtils.putInt(SeekActivity.this, "groupstate", 1);
				intent = new Intent(SeekActivity.this, SeekGroupActivity.class);
				startActivity(intent);
				break;
			case R.id.seek_nearlive_id:
				startActivity(new Intent(SeekActivity.this, SeekNearLiveSearchActivity.class));
				break;
			case R.id.seek_weather_id:
				startActivity(new Intent(SeekActivity.this, SeekWeatherSearchActivity.class));
				break;
			case R.id.seek_ball_id:
				startActivity(new Intent(SeekActivity.this, SeekThrowBallActivity.class));
				break;
			default:

				break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
				case 0:
					if (dialog != null) {
						dialog.dismiss();
					}
					break;
				case PublicConstant.JsonCatch:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow(PublicConstant.ToastCatch, getApplicationContext());

					break;
				case 1:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("会员已过期,请重新购买！", getApplicationContext());
					Intent intent = new Intent(SeekActivity.this, OrderDailog.class);
					startActivity(intent);
					break;
				default:
					break;
			}

		}
	};

	@Override
	public void onResume() {
		super.onResume();
		int groupstate = PreferencesUtils.getInt(SeekActivity.this, "groupstate");
		if (groupstate == 0) {
			tvgroup.setVisibility(View.VISIBLE);
		} else {
			tvgroup.setVisibility(View.GONE);
		}
		// int remindanlian = PreferencesUtils.getInt(SeekActivity.this,
		// "anlianstate");
		// if (remindanlian == 0) {
		// remind_anlian.setVisibility(View.VISIBLE);
		// } else {
		// remind_anlian.setVisibility(View.GONE);
		// }
		// String isshow=PreferencesUtils.getString(SeekActivity.this,"adshow");
		// if(HelperUtil.flagISNoNull(isshow)&&isshow.equals("1"))
		// {
		// seek_app.setVisibility(View.VISIBLE);
		// }
		// else
		// {
		// seek_app.setVisibility(View.GONE);
		// }
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mRemianReceiver != null) {
			unregisterReceiver(mRemianReceiver);
		}
	}

}
