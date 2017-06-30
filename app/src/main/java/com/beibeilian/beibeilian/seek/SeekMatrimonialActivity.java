package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.listview.PullableListView;
import com.beibeilian.beibeilian.me.model.UserInfoEntiy;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.seek.adapter.SeekPersonListAdapter;
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

public class SeekMatrimonialActivity extends Activity implements PullableListView.OnLoadListener {

	private static List<UserInfo> personlist = new ArrayList<UserInfo>();

	private int PageNumber = 0;

	private PullableListView Listview;

	private SeekPersonListAdapter seekPersonListAdapter;

	private InitLoadThread initLoadThread;

	private Dialog dialog;

	private Button btnBack;

	private Button btnApply;

	private BBLDao dao;

	private PullableListView mPullableListView;


	private String matrimon="1",baoming;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_matrimonial);
		dao = new BBLDao(SeekMatrimonialActivity.this, null, null, 1);
		PayRule mPayRule=dao.findPayRule();
		if(mPayRule!=null)
		{
			matrimon=mPayRule.getMarriage();
		}
		else
		{
			matrimon="1";
		}
		Listview = (PullableListView) findViewById(R.id.listview);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnApply = (Button) findViewById(R.id.seek_apply);
		baoming=btnApply.getText().toString();
		dialog = new Dialog(SeekMatrimonialActivity.this,
				R.style.theme_dialog_alert);
		seekPersonListAdapter = new SeekPersonListAdapter(
				SeekMatrimonialActivity.this, personlist);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(HelperUtil.flagISNoNull(matrimon)&&matrimon.equals("1"))
				{
					checkMemberState();
				}
				else
				{
					apply();
				}

			}
		});
		HelperUtil.customDialogShow(dialog, SeekMatrimonialActivity.this,
				"请稍候...");
		if (initLoadThread != null) {
			initLoadThread.interrupt();
		}
		initLoadThread = new InitLoadThread(0);
		initLoadThread.start();
		Listview.setOnLoadListener(this);
		Listview.setHasMoreData(false);

	}

	private void checkMemberState() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stu
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username",dao.queryUserByNewTime().getUsername());
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.CheckONMember, map));
					handler.sendEmptyMessage(0);
					if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_NUMBER_OUT) {
						Intent intent = new Intent(SeekMatrimonialActivity.this, OrderDailog.class);
						startActivity(intent);
					} else if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_OUT) {
						handler.sendEmptyMessage(3);
					}
					else
					{
						apply();
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}

	private void apply() {
		String title = "您确定要报名征婚吗?";
		if (baoming.equals("更新")) {
			title = "您确定要更新征婚日期吗? 更新后将会排名到前面";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("温馨提示");
		builder.setMessage(title).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (personlist.size() > 0) {
							personlist.clear();
						}
						handler.sendEmptyMessage(PublicConstant.RefreashUI);

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();

	}

	private class InitLoadThread extends Thread {

		int type;

		public InitLoadThread(int type) {
			this.type = type;
		}

		@Override
		public void run() {
			try {
				UserInfoEntiy user = dao.queryUserByNewTime();
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", user.getUsername());
				map.put("pagenumber", String.valueOf(PageNumber));
				JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(
						HttpConstantUtil.FindZhenghunState, map));
				if (jsonObject.length() > 0) {
					String state = jsonObject.optString("matrimonial");
					if (state.equals("1")) {
						handler.sendEmptyMessage(7);
					}
				}
				if (type == 1) {
					jsonObject = new JSONObject(HelperUtil.postRequest(
							HttpConstantUtil.UpZhenghun, map));
					if (jsonObject.optInt("result") > 0) {
						handler.sendEmptyMessage(6);
					}
				}
				android.os.Message message=handler.obtainMessage();
				message.obj=HelperUtil.postRequest(HttpConstantUtil.FindZhenghunList, map);
				message.what=1;
				message.sendToTarget();
			} catch (Exception e) {
				// TODO: handle exception
				handler.sendEmptyMessage(PublicConstant.JsonCatch);
			}
		}
	}


	private void parseJson(String result)
	{
		try
		{
			JSONArray jsonArray = new JSONArray(result);
			if (jsonArray.length() > 0) {
				if(PageNumber==0)
				{
					if (personlist.size() > 0) {
						personlist.clear();
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					String username = jsonArray.optJSONObject(i).optString(
							"username");
					String year = jsonArray.optJSONObject(i).optString(
							"birthday");
					String photo = jsonArray.optJSONObject(i).optString(
							"photo");
//					String state = jsonArray.optJSONObject(i).optString(
//							"state");
					String monologue = jsonArray.optJSONObject(i)
							.optString("heartdubai");
					String place = jsonArray.optJSONObject(i).optString(
							"lives");
					String nickname = jsonArray.optJSONObject(i).optString(
							"nickname");
//					String matrimonialtime = jsonArray.optJSONObject(i)
//							.optString("matrimonialtime");
					String sex = jsonArray.optJSONObject(i)
							.optString("sex");
					int heartduibaistate = jsonArray.optJSONObject(i).optInt("heartduibaistate");
					UserInfo model = new UserInfo();
					model.setBirthday(year);
					model.setHeartdubai(monologue);
					model.setLives(place);
					model.setNickname(nickname);
					model.setPhoto(photo);
//					model.setState(state);
					model.setUsername(username);
//					model.setTime(matrimonialtime);
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
					if(seekPersonListAdapter!=null) seekPersonListAdapter.notifyDataSetChanged();
				}

			}
			if(PageNumber>0)
			{
				if(mPullableListView!=null)
				{
					mPullableListView.finishLoading();
				}
			}
			if(jsonArray.length()>=0&&jsonArray.length()<15)
			{
				Listview.setHasMoreData(false);
			}
			else
			{
				Listview.setHasMoreData(true);
			}
			if(dialog!=null) dialog.dismiss();

		}
		catch(Exception e)
		{

		}
	}



	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

				case 1:
					parseJson((String) msg.obj);
					break;
				case PublicConstant.JsonCatch:
					if(dialog!=null) dialog.dismiss();

					break;
				case PublicConstant.RefreashUI:
					String title = "正在报名...";
					if (btnApply.getText().toString().equals("更新")) {
						title = "正在更新...";
					}
					HelperUtil.customDialogShow(dialog,
							SeekMatrimonialActivity.this, title);
					PageNumber = 0;
					if (initLoadThread != null) {
						initLoadThread.interrupt();
					}
					initLoadThread = new InitLoadThread(1);
					initLoadThread.start();
					break;
				case 3:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("会员已过期,请重新购买！", getApplicationContext());
					Intent intent = new Intent(SeekMatrimonialActivity.this, OrderDailog.class);
					startActivity(intent);
					break;
				case 6:
					HelperUtil.totastShow("成功", SeekMatrimonialActivity.this);
					btnApply.setText("更新");
					break;
				case 7:
					btnApply.setText("更新");
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

			finish();
		}
		return false;
	}


	@Override
	public void onLoad(PullableListView pullableListView) {
		// TODO Auto-generated method stub

		mPullableListView=pullableListView;
		PageNumber++;
		initLoadThread = new InitLoadThread(0);
		initLoadThread.start();
	}

}