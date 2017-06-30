package com.beibeilian.beibeilian.seek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.seek.model.Group;
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

public class SeekGroupActivity extends Activity{
	ListView mlistview;
	SeekGroupAdapter mSeekgroupadapter;
	private List<Group> mlistgroup=new ArrayList<Group>();
	BBLDao dao;
	String username,groupname,group,chat="1";
	private SweetAlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_group);
		dialog = new SweetAlertDialog(SeekGroupActivity.this);
		dao=new BBLDao(SeekGroupActivity.this, null, null, 1);
		username=dao.queryUserByNewTime().getUsername();
		PayRule mPayRule=dao.findPayRule();
		if(mPayRule!=null)
		{
			chat=mPayRule.getChat();
		}
		else
		{
			chat="1";
		}
		mlistview=(ListView)findViewById(R.id.listview);
		mSeekgroupadapter=new SeekGroupAdapter(mlistgroup, SeekGroupActivity.this);

		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				group= mlistgroup.get(position).getGroup();
				groupname=mlistgroup.get(position).getName();
				if(HelperUtil.flagISNoNull(chat)&&chat.equals("1"))
				{
					checkMemberState();
				}
				else
				{
					handler.sendEmptyMessage(3);
				}


			}
		});
		Button btnback = (Button) findViewById(R.id.btnBack);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		dialog.setTitleText("请稍候...");
		dialog.show();
		init();
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
						Intent intent = new Intent(SeekGroupActivity.this, OrderDailog.class);
						startActivity(intent);
					} else if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_OUT) {
						handler.sendEmptyMessage(2);
					}
					else
					{
						handler.sendEmptyMessage(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}
	private void insert(final String group)
	{

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", username);
				map.put("group", group);
				try {
					String result = HelperUtil.postRequest(
							HttpConstantUtil.InsertGroup, map);
					handler.sendEmptyMessage(0);
					if(result!=null)
					{
						dao.insertMygroup(username,group);
						Intent mIntent=new Intent(SeekGroupActivity.this,SeekGroupChatActivity.class);
						mIntent.putExtra("group",group);
						mIntent.putExtra("groupname",groupname);
						startActivity(mIntent);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();


	}


	private void init()
	{

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String result = HelperUtil.getRequest(
							HttpConstantUtil.FindGroup);
					android.os.Message message = handler.obtainMessage();
					message.obj = result;
					message.what = 1;
					message.sendToTarget();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();

	}


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
				case 0:
					if(dialog!=null)
						dialog.dismiss();
				case 1:
					if(dialog!=null)
						dialog.dismiss();
					parseJson((String) msg.obj);
					break;
				case -1:
					if(dialog!=null)
						dialog.dismiss();
					HelperUtil.totastShow(PublicConstant.ToastCatch, getApplicationContext());
					break;
				case 2:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("会员已过期,请重新购买！", getApplicationContext());
					Intent intent = new Intent(SeekGroupActivity.this, OrderDailog.class);
					startActivity(intent);
					break;
				case 3:
					dialog.setTitleText("请稍候...");
					dialog.show();
					insert(group);
					dao.updateImSeeStateGroup(group);
					break;
				default:
					break;
			}

		}
	};


	private void parseJson(String json)
	{
		try {
			JSONArray jsonArray = new JSONArray(json);
			if(jsonArray!=null&&jsonArray.length()>0)
			{

				for(int i=0;i<jsonArray.length();i++)
				{
					String name=jsonArray.optJSONObject(i).optString("groupName");
					String id=jsonArray.optJSONObject(i).optString("groupId");
					String num=jsonArray.optJSONObject(i).optString("groupNumber");
					Group model=new Group();
					model.setGroup(id);
					model.setName(name);
					model.setNumber(num);
					int count=dao.queryCountByUserNewStateGroup(id);
					if(count>0)
					{
						model.setState(1);
					}
					else
					{
						model.setState(0);
					}
					mlistgroup.add(model);
				}
				if(mlistview.getAdapter()==null)
				{
					mlistview.setAdapter(mSeekgroupadapter);
				}
				if(mSeekgroupadapter!=null)
				{
					mSeekgroupadapter.notifyDataSetChanged();
				}
			}

		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
