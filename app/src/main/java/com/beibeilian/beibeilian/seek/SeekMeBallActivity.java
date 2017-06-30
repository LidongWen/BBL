package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.listview.PullableListView;
import com.beibeilian.beibeilian.seek.adapter.SeekBallAdapter;
import com.beibeilian.beibeilian.seek.model.Ball;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.SweetAlertDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeekMeBallActivity  extends Activity implements PullableListView.OnLoadListener {
	private InitLoadThread initLoadThread;
	BBLDao dao;
	String username;
	PullableListView listview;
	SweetAlertDialog mDialog;
	SeekBallAdapter mSeekBallAdapter;
	List<Ball> listball=new ArrayList<Ball>();
	int PageNumber=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.seek_me_ball);
		dao = new BBLDao(SeekMeBallActivity.this, null, null, 1);
		username=dao.queryUserByNewTime().getUsername();
		listview=(PullableListView) findViewById(R.id.listview);
		Button btnback = (Button) findViewById(R.id.btnBack);
		mDialog = new SweetAlertDialog(SeekMeBallActivity.this);
		mSeekBallAdapter=new SeekBallAdapter(SeekMeBallActivity.this, listball);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mDialog.setTitleText("请稍候...");
		mDialog.show();
		initLoadThread=new InitLoadThread();
		initLoadThread.start();
		listview.setOnLoadListener(this);
		listview.setHasMoreData(false);

	}

	private class InitLoadThread extends Thread {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pagenumber", String.valueOf(PageNumber));
				map.put("username", username);
				String result = HelperUtil.postRequest(HttpConstantUtil.SelectBallBypage, map);
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
				if (PageNumber == 0 && listball != null && listball.size() > 0) {
					listball.clear();
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					String username = jsonArray.optJSONObject(i).optString("username");
					String nickname = jsonArray.optJSONObject(i).optString("name");
					String content = jsonArray.optJSONObject(i).optString("content");
					String time = jsonArray.optJSONObject(i).optString("modtime");
					Ball model=new Ball();
					model.setContent(content);
					model.setModtime(time);
					model.setName(nickname);
					model.setUsername(username);
					listball.add(model);
				}

			}
			if (listview.getAdapter() == null) {
				listview.setAdapter(mSeekBallAdapter);
				if (mSeekBallAdapter != null)
					mSeekBallAdapter.notifyDataSetChanged();
			} else {
				if (mSeekBallAdapter != null)
					mSeekBallAdapter.notifyDataSetChanged();
			}
			if (PageNumber == 0) {
			} else {
				if (listview != null) {
					listview.finishLoading();
				}
			}
			if (jsonArray.length() >= 0 && jsonArray.length() < 15) {
				listview.setHasMoreData(false);
			} else {
				listview.setHasMoreData(true);
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
		listview = pullableListView;
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
