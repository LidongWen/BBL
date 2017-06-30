package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.beibeilian.beibeilian.MainActivity;
import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.listview.PullableListView;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.seek.adapter.SeekPersonListTimeAdapter;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeAnlianActivity extends Activity implements PullableListView.OnLoadListener {

	private static List<UserInfo> personlist = new ArrayList<UserInfo>();

	private int PageNumber = 0;

	private PullableListView Listview;

	private PullableListView mPullableListView;

	private SeekPersonListTimeAdapter seekPersonListAdapter;

	private InitLoadThread initLoadThread;

	private Dialog dialog;

	private Button btnBack, btnAnlian;

	private BBLDao dao;

	String username,anlian="1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_anlian);
		dao = new BBLDao(MeAnlianActivity.this, null, null, 1);
		PayRule mPayRule=dao.findPayRule();
		if(mPayRule!=null)
			anlian=mPayRule.getAnlian();
		username=dao.queryUserByNewTime().getUsername();
		Listview = (PullableListView) findViewById(R.id.listview);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnAnlian = (Button) findViewById(R.id.btnMeWhoid);

		dialog = new Dialog(MeAnlianActivity.this, R.style.theme_dialog_alert);
		seekPersonListAdapter = new SeekPersonListTimeAdapter(MeAnlianActivity.this, personlist);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MeAnlianActivity.this, MainActivity.class));
				finish();
			}
		});
		btnAnlian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MeAnlianActivity.this, MeMineAnlianActivity.class));
			}
		});
		HelperUtil.customDialogShow(dialog, MeAnlianActivity.this, "请稍候...");
		if(HelperUtil.flagISNoNull(anlian)&&anlian.equals("1"))
		{
			checkMemberState();
		}
		else
		{
			handler.sendEmptyMessage(2);

		}

		Listview.setOnLoadListener(this);
		Listview.setHasMoreData(false);
//		fn fnbanner=new fn(MeAnlianActivity.this,BBLConstant.PANDASDK_ID, false);
//		fnbanner.setAttachedActivity(MeAnlianActivity.this);
//		fnbanner.setAttachedViewId(R.id.rl_anlian_banner);
//		fnbanner.setAttachedMode(fn.MODE_BOTTOM);
//		fnbanner.setWidth(820);
//		fnbanner.setHeight(150);
//		fnbanner.start();
	}
	private void checkMemberState() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stu
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username",username);
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.CheckONMember, map));
					if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_NUMBER_OUT) {
						Intent intent = new Intent(MeAnlianActivity.this, OrderDailog.class);
						startActivity(intent);
					} else if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_OUT) {
						handler.sendEmptyMessage(3);
						Intent intent = new Intent(MeAnlianActivity.this, OrderDailog.class);
						startActivity(intent);
					}
					else
					{
						handler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(PublicConstant.JsonCatch);
				}
			}
		}).start();
	}
	private class InitLoadThread extends Thread {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", dao.queryUserByNewTime().getUsername());
				map.put("pagenumber", String.valueOf(PageNumber));
				android.os.Message message = handler.obtainMessage();
				message.obj = HelperUtil.postRequest(HttpConstantUtil.FindWhoMeAnlianList, map);
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
					String username = jsonArray.optJSONObject(i).optString("username");
					String year = jsonArray.optJSONObject(i).optString("birthday");
					String photo = jsonArray.optJSONObject(i).optString("photo");
					// String state = jsonArray.optJSONObject(i).optString(
					// "state");
					String monologue = jsonArray.optJSONObject(i).optString("heartdubai");
					String place = jsonArray.optJSONObject(i).optString("lives");
					String nickname = jsonArray.optJSONObject(i).optString("nickname");
					String time = jsonArray.optJSONObject(i).optString("time");
					String sex = jsonArray.optJSONObject(i).optString("sex");
					UserInfo model = new UserInfo();
					model.setBirthday(year);
					model.setHeartdubai(monologue);
					model.setLives(place);
					model.setNickname(nickname);
					model.setPhoto(photo);
					// model.setState(state);
					model.setUsername(username);
					model.setTime(time);
					model.setSex(sex);
					personlist.add(model);
				}
				if (Listview.getAdapter() == null) {
					Listview.setAdapter(seekPersonListAdapter);
				}
				if (seekPersonListAdapter != null) {
					seekPersonListAdapter.notifyDataSetChanged();
				}

			}
			if (jsonArray.length() >= 0 && jsonArray.length() < 15) {
				Listview.setHasMoreData(false);
			} else {
				Listview.setHasMoreData(true);
			}
			if (PageNumber > 0) {
				if (mPullableListView != null) {
					mPullableListView.finishLoading();
				}
			}
			if (dialog != null) {
				dialog.dismiss();
			}
		} catch (Exception e) {

		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

				case 1:
					parseJson((String) msg.obj);
					break;
				case PublicConstant.JsonCatch:
					if (dialog != null) {
						dialog.dismiss();
					}
					break;
				case 2:
					if (initLoadThread != null) {
						initLoadThread.interrupt();
					}
					initLoadThread = new InitLoadThread();
					initLoadThread.start();
					break;
				case 3:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("会员已过期,请重新购买！", getApplicationContext());
					break;
				default:
					break;
			}

		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (dialog != null) {
			dialog.dismiss();
		}

		if (initLoadThread != null) {
			initLoadThread.interrupt();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		return false;
	}

	@Override
	public void onLoad(PullableListView pullableListView) {
		// TODO Auto-generated method stub

		mPullableListView = pullableListView;

		PageNumber++;

		if (initLoadThread != null) {
			initLoadThread.interrupt();
		}
		initLoadThread = new InitLoadThread();
		initLoadThread.start();
	}

}
