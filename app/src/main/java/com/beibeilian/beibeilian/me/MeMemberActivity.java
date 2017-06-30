package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MeMemberActivity extends Activity {

	private BBLDao dao;
	private Button btnBack, btnMember;
	private TextView tv_ts;
	private Dialog mDialog;
	private IntentFilter mIntentFilter;
	private RefreshReceiver mRefreshReceiver;
	private String username;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_member);
		ExitApplication.addActivity(MeMemberActivity.this);
		mDialog = new Dialog(MeMemberActivity.this, R.style.theme_dialog_alert);
		dao = new BBLDao(MeMemberActivity.this, null, null, 1);
		username = dao.queryUserByNewTime().getUsername();
		btnBack = (Button) findViewById(R.id.btnBack);
		btnMember = (Button) findViewById(R.id.btn_member_kaitong_id);

		tv_ts = (TextView) findViewById(R.id.tv_ts_id);

		mIntentFilter = new IntentFilter();
		mRefreshReceiver = new RefreshReceiver();
		mIntentFilter.addAction(ReceiverConstant.ME_MEMBER_REFESH_ACTION);
		registerReceiver(mRefreshReceiver, mIntentFilter);

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnMember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MeMemberActivity.this,
						OrderDailog.class);
				intent.putExtra("member_value", "1");
				startActivity(intent);
			}
		});
		checkMember();
	}

	private class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					ReceiverConstant.ME_MEMBER_REFESH_ACTION)) {
				checkMember();
			}
		}

	}

	private int remaintime = 0;

	private void checkMember() {
		HelperUtil.customDialogShow(mDialog, MeMemberActivity.this, "请稍候...");

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					JSONObject jsonObject = new JSONObject(HelperUtil
							.postRequest(HttpConstantUtil.CheckIsOrNOMember, map));
					if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_YES) {
						remaintime = jsonObject.optInt("remaintime");
						handler.sendEmptyMessage(1);
					} else if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_OUT) {
						handler.sendEmptyMessage(2);
					} else {
						handler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
				case 0:
					if (mDialog != null)
						mDialog.dismiss();
					tv_ts.setText("您还没开通VIP会员");
					break;
				case 1:
					if (mDialog != null)
						mDialog.dismiss();
					tv_ts.setText("您的VIP会员剩余"+remaintime+"天");
					btnMember.setText("VIP会员续费");
					break;
				case 2:
					if (mDialog != null)
						mDialog.dismiss();
					tv_ts.setText("您的VIP会员已过期");
					break;
				case -1:
					if (mDialog != null)
						mDialog.dismiss();
					HelperUtil.totastShow("网请检查网络是否可用或稍候重试",
							getApplicationContext());
					break;

				default:
					break;
			}

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRefreshReceiver != null) {
			unregisterReceiver(mRefreshReceiver);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}

}
