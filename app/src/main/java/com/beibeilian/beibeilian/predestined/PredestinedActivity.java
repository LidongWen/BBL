package com.beibeilian.beibeilian.predestined;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.listview.PullToRefreshLayout;
import com.beibeilian.beibeilian.listview.PullableListView;
import com.beibeilian.beibeilian.seek.adapter.SeekPersonListAdapter;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class PredestinedActivity extends Activity implements PullableListView.OnLoadListener {

	private static List<UserInfo> personlist = new ArrayList<UserInfo>();

	private int PageNumber = 0;

	private PullableListView Listview;

	private SeekPersonListAdapter seekPersonListAdapter;

	private InitLoadThread initLoadThread;

	private RelativeLayout load_progressbar;

	private IntentFilter intentFilter;

//	private RefreshListReceiver refreshListReceiver;

	private ProgressBar leftProgressbarid;

	private PullToRefreshLayout mpullToRefreshLayout;

	private PullableListView mPullableListView;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.predestined);
//		intentFilter = new IntentFilter();
//		refreshListReceiver = new RefreshListReceiver();
//		intentFilter.addAction(ReceiverConstant.Predestined_ACTION);
//		registerReceiver(refreshListReceiver, intentFilter);
		
		
		
		ExitApplication.addActivity(PredestinedActivity.this);
		Listview = (PullableListView) findViewById(R.id.listview);
		leftProgressbarid = (ProgressBar) findViewById(R.id.leftprogressbar_id);
		load_progressbar = (RelativeLayout) findViewById(R.id.list_load_id);
		seekPersonListAdapter = new SeekPersonListAdapter(
				PredestinedActivity.this, personlist);
		
		
		((PullToRefreshLayout) findViewById(R.id.predestined_layout))
				.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh(
							PullToRefreshLayout pullToRefreshLayout) {
						// TODO Auto-generated method stub
						mpullToRefreshLayout = pullToRefreshLayout;
						PageNumber = 0;
						if(load_progressbar!=null)
						{
							load_progressbar.setVisibility(View.GONE);
						}
						if (leftProgressbarid != null)
						{
							leftProgressbarid.setVisibility(View.VISIBLE);
						}
						if (initLoadThread != null) {
							initLoadThread.interrupt();
						}
						initLoadThread = new InitLoadThread();
						initLoadThread.start();

					}
				});

		initLoadThread = new InitLoadThread();
		initLoadThread.start();
		Listview.setOnLoadListener(this);
		Listview.setHasMoreData(false);
		
//		fn fnbanner=new fn(PredestinedActivity.this,BBLConstant.PANDASDK_ID, false);
//		fnbanner.setAttachedActivity(PredestinedActivity.this);
//		fnbanner.setAttachedViewId(R.id.rl_banner);
//		fnbanner.setAttachedMode(fn.MODE_BOTTOM);
//		fnbanner.setWidth(820);
//		fnbanner.setHeight(150);
//		fnbanner.start();
	}
//
//	private class RefreshListReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//
//			if (intent.getAction().equals(ReceiverConstant.Predestined_ACTION)) {
//				PageNumber = 0;
//				if (personlist != null && personlist.size() > 0) {
//					load_progressbar.setVisibility(View.GONE);
//				} else {
//					load_progressbar.setVisibility(View.VISIBLE);
//				}
////				if (leftProgressbarid != null)
////					leftProgressbarid.setVisibility(View.VISIBLE);
////				if (initLoadThread != null) {
////					initLoadThread.interrupt();
////				}
////				initLoadThread = new InitLoadThread();
////				initLoadThread.start();
//			}
//		}
//	}

	private class InitLoadThread extends Thread {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("pagenumber", String.valueOf(PageNumber));
				String result = HelperUtil.postRequest(
						HttpConstantUtil.FindHavealookPageList, map);
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
				if (PageNumber == 0 && personlist != null
						&& personlist.size() > 0) {
					personlist.clear();
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
					String sex = jsonArray.optJSONObject(i).optString("sex");
					String maritalstatus = jsonArray.optJSONObject(i).optString("maritalstatus");
					int heartduibaistate = jsonArray.optJSONObject(i).optInt("heartduibaistate");
					String auth = jsonArray.optJSONObject(i).optString(
							"auth");
					UserInfo model = new UserInfo();
					model.setBirthday(year);
					model.setHeartdubai(monologue);
					model.setLives(place);
					model.setNickname(nickname);
					model.setPhoto(photo);
					//model.setState(state);
					model.setUsername(username);
					model.setAuth(auth);
					model.setSex(sex);
					model.setHeartduibaistate(heartduibaistate);
					model.setMaritalstatus(maritalstatus);
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
			if (load_progressbar != null) {
				load_progressbar.setVisibility(View.GONE);
			}
			if (leftProgressbarid != null)
				leftProgressbarid.setVisibility(View.GONE);
			if (PageNumber == 0) {
				if (mpullToRefreshLayout != null) {
					mpullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);

				}
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
			if (PageNumber == 0) {
				if (mpullToRefreshLayout != null) {
					mpullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}
			if (load_progressbar != null) {
				load_progressbar.setVisibility(View.GONE);
			}
			if (leftProgressbarid != null)
				leftProgressbarid.setVisibility(View.GONE);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case 1:
				parseJson((String) msg.obj);
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
//		if (refreshListReceiver != null)
//			unregisterReceiver(refreshListReceiver);
	}

	@Override
	public void onLoad(PullableListView pullableListView) {
		// TODO Auto-generated method stub
//		System.out.println("���û���...");
		mPullableListView = pullableListView;
		PageNumber++;
		initLoadThread = new InitLoadThread();
		initLoadThread.start();
	}

}
