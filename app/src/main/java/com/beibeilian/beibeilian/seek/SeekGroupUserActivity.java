package com.beibeilian.beibeilian.seek;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.listview.PullableListView;
import com.beibeilian.beibeilian.seek.adapter.SeekPersonListAdapter;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class SeekGroupUserActivity extends Activity implements PullableListView.OnLoadListener {

	private static List<UserInfo> personlist = new ArrayList<UserInfo>();

	private int PageNumber = 0;

	private PullableListView Listview;

	private SeekPersonListAdapter seekPersonListAdapter;

	private InitLoadThread initLoadThread;

	private PullableListView mPullableListView;

	private String group,username;

	SweetAlertDialog mDialog;

	BBLDao dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_group_user);
		group = getIntent().getStringExtra("group");
		dao=new BBLDao(SeekGroupUserActivity.this,null,null,1);
		username=dao.queryUserByNewTime().getUsername();
		Button btnback = (Button) findViewById(R.id.btnBack);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Button btnexit = (Button) findViewById(R.id.btn_exit_id);
		btnexit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				exitGroup();
			}
		});

		ExitApplication.addActivity(SeekGroupUserActivity.this);
		Listview = (PullableListView) findViewById(R.id.listview);
		seekPersonListAdapter = new SeekPersonListAdapter(SeekGroupUserActivity.this, personlist);
		mDialog = new SweetAlertDialog(SeekGroupUserActivity.this);
		mDialog.setTitleText("请稍候...");
		mDialog.show();
		initLoadThread = new InitLoadThread();
		initLoadThread.start();
		Listview.setOnLoadListener(this);
		Listview.setHasMoreData(false);

	}

	private void exitGroup()
	{
		mDialog.setTitleText("请稍候...");
		mDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Map<String, String> map=new HashMap<String, String>();
				map.put("group", group);
				map.put("username",username);
				try {
					JSONObject mJsonObject=new JSONObject(HelperUtil.postRequest(HttpConstantUtil.ExitGroup, map));
					int result=mJsonObject.optInt("result");
					if(result>0)
					{
						handler.sendEmptyMessage(2);
					}
					else
					{
						handler.sendEmptyMessage(3);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(-1);
				}


			}
		}).start();

	}


	private class InitLoadThread extends Thread {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pagenumber", String.valueOf(PageNumber));
				map.put("group", group);
				String result = HelperUtil.postRequest(HttpConstantUtil.FindGroupUser, map);
				android.os.Message message = handler.obtainMessage();
				message.obj = result;
				message.what = 1;
				message.sendToTarget();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void parseJson(String result) {
		try {
			JSONArray jsonArray = new JSONArray(result);
			if (jsonArray.length() > 0) {
				if (PageNumber == 0 && personlist != null && personlist.size() > 0) {
					personlist.clear();
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					String username = jsonArray.optJSONObject(i).optString("username");
					String year = jsonArray.optJSONObject(i).optString("birthday");
					String photo = jsonArray.optJSONObject(i).optString("photo");
					// String state = jsonArray.optJSONObject(i)
					// .optString("state");
					String monologue = jsonArray.optJSONObject(i).optString("heartdubai");
					String place = jsonArray.optJSONObject(i).optString("lives");
					String nickname = jsonArray.optJSONObject(i).optString("nickname");
					String sex = jsonArray.optJSONObject(i).optString("sex");
					int heartduibaistate = jsonArray.optJSONObject(i).optInt("heartduibaistate");
					// String logintime = jsonArray.optJSONObject(i).optString(
					// "time");
					UserInfo model = new UserInfo();
					model.setBirthday(year);
					model.setHeartdubai(monologue);
					model.setLives(place);
					model.setNickname(nickname);
					model.setPhoto(photo);
					// model.setState(state);
					model.setUsername(username);
					// model.setTime(logintime);
					model.setSex(sex);
					model.setHeartduibaistate(heartduibaistate);
					personlist.add(model);
				}

			}
			if (Listview.getAdapter() == null) {
				Listview.setAdapter(seekPersonListAdapter);
				if (seekPersonListAdapter != null)
					seekPersonListAdapter.notifyDataSetChanged();
			} else {
				if (seekPersonListAdapter != null)
					seekPersonListAdapter.notifyDataSetChanged();
			}
			if (PageNumber == 0) {
			} else {
				if (mPullableListView != null) {
					mPullableListView.finishLoading();
				}
			}
			if (jsonArray.length() >= 0 && jsonArray.length() < 15) {
				Listview.setHasMoreData(false);
			} else {
				Listview.setHasMoreData(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

				case 1:
					if (mDialog != null)
						mDialog.dismiss();
					parseJson((String) msg.obj);
					break;
				case 2:
					if (mDialog != null)
						mDialog.dismiss();
					HelperUtil.totastShow("退出成功", getApplicationContext());
					startActivity(new Intent(SeekGroupUserActivity.this,SeekGroupActivity.class));
					finish();
					break;
				case 3:
					if (mDialog != null)
						mDialog.dismiss();
					HelperUtil.totastShow("退出失败", getApplicationContext());
					break;
				case -1:
					if (mDialog != null)
						mDialog.dismiss();
					HelperUtil.totastShow(PublicConstant.ToastCatch, getApplicationContext());
					break;
				default:
					break;
			}

		}
	};

	@Override
	public void onPause() {
		super.onPause();
		if (initLoadThread != null) {
			initLoadThread.interrupt();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// if (refreshListReceiver != null)
		// unregisterReceiver(refreshListReceiver);
	}

	@Override
	public void onLoad(PullableListView pullableListView) {
		// TODO Auto-generated method stub
		// System.out.println("调用滑动...");
		mPullableListView = pullableListView;
		PageNumber++;
		initLoadThread = new InitLoadThread();
		initLoadThread.start();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}
}
