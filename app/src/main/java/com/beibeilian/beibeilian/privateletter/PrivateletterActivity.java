package com.beibeilian.beibeilian.privateletter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.listview.PullToRefreshLayout;
import com.beibeilian.beibeilian.listview.PullableListView;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.privateletter.adapter.PrivateletterAdapter;
import com.beibeilian.beibeilian.privateletter.model.MessageList;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateletterActivity extends Activity implements PullableListView.OnLoadListener {

	private List<MessageList> messageList = new ArrayList<MessageList>(); // 获取json

	private List<MessageList> messageUIList = new ArrayList<MessageList>(); // 更新ui

	private JSONArray jsonArray;

	private PullableListView listview;

	private PrivateletterAdapter privateletterAdapter;

	private BBLDao dao;

	private String username;

	private int PAGENUMBER = 0;

	private RefreshThread refreshThread;

	private IntentFilter intentFilter;

	private MessageRefreashReceiver messageRefreashReceiver;

	private ProgressBar connProgressbar;

	private Button btnLiaoliao;

	private Button btnRefresh;

	private String outtemp = "";
	private String intemp = "";

	private PullToRefreshLayout mPullToRefreshLayout;

	private PullableListView mPullableListView;

	private PullToRefreshLayout privateletter_PullToRefreshLayout;

	private Dialog mdialog;

	private String CHAT_ON="1";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privateletter);
		dao = new BBLDao(PrivateletterActivity.this, null, null, 1);
		username = dao.queryUserByNewTime().getUsername();
		PayRule mPayRule=dao.findPayRule();
		if(mPayRule!=null)
		{
			CHAT_ON=mPayRule.getChat();
		}
		else
		{
			CHAT_ON="1";
		}
		ExitApplication.addActivity(PrivateletterActivity.this);
		listview = (PullableListView) findViewById(R.id.message_listview);
		connProgressbar = (ProgressBar) findViewById(R.id.connectid);
		btnLiaoliao = (Button) findViewById(R.id.liaoliao_id);
		btnRefresh = (Button) findViewById(R.id.btnRefreshid);
		mdialog = new Dialog(PrivateletterActivity.this, R.style.theme_dialog_alert);
		privateletter_PullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.privateletter_layout);
		privateletter_PullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				mPullToRefreshLayout = pullToRefreshLayout;
				outtemp = "";
				intemp = "";
				if (connProgressbar != null)
					connProgressbar.setVisibility(View.VISIBLE);
				PAGENUMBER = 0;
				if (refreshThread != null)
					refreshThread.interrupt();
				refreshThread = new RefreshThread();
				refreshThread.start();
			}
		});

		intentFilter = new IntentFilter();
		messageRefreashReceiver = new MessageRefreashReceiver();
		intentFilter.addAction(ReceiverConstant.MESSAGEREFEASHLIST_ACTION);
		intentFilter.addAction(ReceiverConstant.RECONN_RECEIVER_START_ACTION);
		intentFilter.addAction(ReceiverConstant.RECONN_RECEIVER_SUCCESS_ACTION);
		PrivateletterActivity.this.registerReceiver(messageRefreashReceiver, intentFilter);

		privateletterAdapter = new PrivateletterAdapter(PrivateletterActivity.this, messageUIList, username);
		// listview.setAdapter(privateletterAdapter);

		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				outtemp = "";
				intemp = "";
				if (connProgressbar != null)
					connProgressbar.setVisibility(View.VISIBLE);
				PAGENUMBER = 0;
				if (refreshThread != null)
					refreshThread.interrupt();
				refreshThread = new RefreshThread();
				refreshThread.start();
			}
		});
		btnLiaoliao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendBroadcast(new Intent(ReceiverConstant.PredestinedLiaoLiao_ACTION));
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				try {
					MessageList model = messageUIList.get(position);
					if ((HelperUtil.flagISNoNull(CHAT_ON) && CHAT_ON.equals("1"))) {
						if (model.getOutuser().equals(username)) {
							checkMemberState(username, model.getFromuser());
						} else {

							checkMemberState(username, model.getOutuser());
						}
					} else {
						String touser = "";
						if (model.getOutuser().equals(username)) {
							touser = model.getFromuser();
						} else {
							touser = model.getOutuser();
						}
						UserInfo userInfo = dao.queryPhoto(touser);
						String name = touser;
						if (userInfo != null && HelperUtil.flagISNoNull(userInfo.getNickname())) {
							name = userInfo.getNickname();
						}
						dao.updateImSeeState(touser);
						Intent intent = new Intent(PrivateletterActivity.this, PrivateletterChatActivity.class);
						intent.putExtra("toUser", touser);
						intent.putExtra("toName", name);
						startActivity(intent);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		});

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				// TODO Auto-generated method stub
				if (messageUIList.size() == 0 || position > messageUIList.size()) {
					return false;
				}
				final MessageList model = messageUIList.get(position);
				AlertDialog.Builder builder = new Builder(PrivateletterActivity.this);
				builder.setItems(getResources().getStringArray(R.array.item_messagemenu),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO 自动生成的方法存根

								if (arg1 == 0) {

									AlertDialog.Builder builder = new AlertDialog.Builder(PrivateletterActivity.this);
									builder.setTitle("温馨提示");
									builder.setMessage("您确定要删除此条记录吗?").setCancelable(false)
											.setPositiveButton("确定", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int id) {
													if (model.getFromuser().equals(username)) {

														dao.delIm(model.getOutuser());
													} else {
														dao.delIm(model.getFromuser());
													}
													outtemp = "";
													intemp = "";
													handler.sendEmptyMessage(PublicConstant.RefreashUI);
												}
											}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									}).show();
								}
								arg0.dismiss();
							}
						});
				builder.show();
				return false;
			}
		});

		initData();
		listview.setOnLoadListener(this);
		listview.setHasMoreData(false);

	}

	private void checkMemberState(final String username, final String touser) {

		HelperUtil.customDialogShow(mdialog, PrivateletterActivity.this, "请稍候...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stu
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.CheckONMember, map));
					handler.sendEmptyMessage(0);

					if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_NUMBER_OUT) {
						UserInfo userInfo = dao.queryPhoto(touser);
						String name = touser;
						if (userInfo != null && HelperUtil.flagISNoNull(userInfo.getNickname())) {
							name = userInfo.getNickname();
						}
						Intent intent = new Intent(PrivateletterActivity.this, OrderDailog.class);
						intent.putExtra("toUser", touser);
						intent.putExtra("toName", name);
						startActivity(intent);
					} else if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_OUT) {
						handler.sendEmptyMessage(3);
						UserInfo userInfo = dao.queryPhoto(touser);
						String name = touser;
						if (userInfo != null && HelperUtil.flagISNoNull(userInfo.getNickname())) {
							name = userInfo.getNickname();
						}
						Intent intent = new Intent(PrivateletterActivity.this, OrderDailog.class);
						intent.putExtra("toUser", touser);
						intent.putExtra("toName", name);
						startActivity(intent);
					} else {
						UserInfo userInfo = dao.queryPhoto(touser);
						String name = touser;
						if (userInfo != null && HelperUtil.flagISNoNull(userInfo.getNickname())) {
							name = userInfo.getNickname();
						}
						dao.updateImSeeState(touser);
						Intent intent = new Intent(PrivateletterActivity.this, PrivateletterChatActivity.class);
						intent.putExtra("toUser", touser);
						intent.putExtra("toName", name);
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	private class MessageRefreashReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ReceiverConstant.MESSAGEREFEASHLIST_ACTION)) {
				initData();
			}
			if (intent.getAction().equals(ReceiverConstant.RECONN_RECEIVER_START_ACTION)) {
				connProgressbar.setVisibility(View.VISIBLE);
			}
			if (intent.getAction().equals(ReceiverConstant.RECONN_RECEIVER_SUCCESS_ACTION)) {
				connProgressbar.setVisibility(View.GONE);
			}

		}
	}

	public void onResume() {
		super.onResume();
		outtemp = "";
		intemp = "";
		PAGENUMBER = 0;
		initData();
	}

	/**
	 * 消息列表
	 *
	 * @author Administrator
	 *
	 */
	private void initData() {
		try {
			List<MessageList> listTemp = dao.queryMessageList(String.valueOf(PAGENUMBER), username);
			if (listTemp != null && listTemp.size() > 0) {
				// if (PAGENUMBER == 0) {
				outtemp = "";
				intemp = "";
				if (messageList != null && messageList.size() > 0) {
					messageList.clear();
				}
				// }
				for (int i = 0; i < listTemp.size(); i++) {
					String outuser = listTemp.get(i).getOutuser();
					String inuser = listTemp.get(i).getFromuser();
					String content = listTemp.get(i).getFromcontent();
					String time = listTemp.get(i).getFromtime();
					MessageList model = new MessageList();
					model.setOutuser(outuser);
					model.setFromcontent(content);
					model.setFromtime(time);
					model.setFromuser(inuser);
					messageList.add(model);
				}

				if (PAGENUMBER == 0) {
					if (messageUIList != null && messageUIList.size() > 0) {
						messageUIList.clear();
					}
				}
				for (int i = 0; i < messageList.size(); i++) {
					String outuser = messageList.get(i).getOutuser();
					String inuser = messageList.get(i).getFromuser();
					String content = messageList.get(i).getFromcontent();
					String time = messageList.get(i).getFromtime();
					if (outuser.equals(outtemp) && inuser.equals(intemp)) {
					} else {
						MessageList model = new MessageList();
						model.setOutuser(outuser);
						model.setFromcontent(content);
						model.setFromtime(time);
						model.setFromuser(inuser);
						messageUIList.add(model);
					}
					outtemp = outuser;
					intemp = inuser;
				}
				listview.setVisibility(View.VISIBLE);
				btnLiaoliao.setVisibility(View.GONE);
				privateletter_PullToRefreshLayout.setVisibility(View.VISIBLE);

			} else {
				if (PAGENUMBER == 0) {
					listview.setVisibility(View.GONE);

					btnLiaoliao.setVisibility(View.VISIBLE);
					privateletter_PullToRefreshLayout.setVisibility(View.GONE);
				}
			}
			if (listview.getAdapter() == null) {
				listview.setAdapter(privateletterAdapter);
			}
			if (privateletterAdapter != null)
				privateletterAdapter.notifyDataSetChanged();
			if (PAGENUMBER == 0) {
				if (mPullToRefreshLayout != null) {
					mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			} else {
				if (mPullableListView != null) {
					mPullableListView.finishLoading();
				}
			}
			if (listTemp.size() >= 0 && listTemp.size() < 15) {
				listview.setHasMoreData(false);
				listview.setLoadMoreNO();
			} else {
				listview.setHasMoreData(true);
			}
			if (connProgressbar != null)
				connProgressbar.setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (connProgressbar != null)
				connProgressbar.setVisibility(View.GONE);
		}
	}

	private class RefreshThread extends Thread {
		@Override
		public void run() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", username);
				map.put("pagenumber", "0");
				jsonArray = new JSONArray(HelperUtil.postRequest(HttpConstantUtil.FindImRecord, map));
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						String outuser = jsonArray.optJSONObject(i).optString("outuser");
						String inuser = jsonArray.optJSONObject(i).optString("inuser");
						String content = jsonArray.optJSONObject(i).optString("content");
						String time = jsonArray.optJSONObject(i).optString("time");
						// String state = jsonArray.optJSONObject(i).optString(
						// "state");
						String imid = jsonArray.optJSONObject(i).optString("imid");
						dao.updateRefreashIm(outuser, inuser, content, time, "1", imid, "1");
					}

				}

				handler.sendEmptyMessage(PublicConstant.RefreashUI);

			} catch (Exception e) {

				handler.sendEmptyMessage(PublicConstant.JsonCatch);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

				case PublicConstant.RefreashUI:
					initData();
					break;
				case PublicConstant.JsonCatch:
					if (mPullToRefreshLayout != null) {
						mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
					}
					if (connProgressbar != null)
						connProgressbar.setVisibility(View.GONE);
					break;
				case 0:
					if (mdialog != null)
						mdialog.dismiss();
					break;
				case 1:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("请检查网络是否可用或稍候重试", getApplicationContext());
					break;
				case 3:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("会员已过期,请重新购买！", getApplicationContext());
					startActivity(new Intent(PrivateletterActivity.this, OrderDailog.class));

					break;
				case 4:

					break;
				default:
					break;
			}
		}

	};

	@Override
	public void onPause() {
		super.onPause();
		if (refreshThread != null)
			refreshThread.interrupt();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (messageRefreashReceiver != null) {
			PrivateletterActivity.this.unregisterReceiver(messageRefreashReceiver);
		}
	}

	@Override
	public void onLoad(PullableListView pullableListView) {
		// TODO Auto-generated method stub
		mPullableListView = pullableListView;
		PAGENUMBER++;
		initData();
	}

}