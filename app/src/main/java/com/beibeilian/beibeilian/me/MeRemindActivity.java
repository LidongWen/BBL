package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.model.Remind;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.ExitApplication;

public class MeRemindActivity extends Activity{

	private ImageView imgVoice;
	private ImageView imgZhendong;
	private Button btnBack;

	private BBLDao dao;

	private Remind remind;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_set_remind);
		ExitApplication.addActivity(MeRemindActivity.this);
		dao=new BBLDao(MeRemindActivity.this,null,null,1);
		imgVoice=(ImageView)findViewById(R.id.voice);
		imgZhendong=(ImageView)findViewById(R.id.zhendong);
		btnBack=(Button)findViewById(R.id.btnBack);
		username=dao.queryUserByNewTime().getUsername();
		remind=dao.queryRemind(username);
		if(remind!=null)
		{
			if(remind.getVoice().equals("1"))
			{
				imgVoice.setBackgroundResource(R.drawable.kaiqi);

			}
			else
			{
				imgVoice.setBackgroundResource(R.drawable.guanbi);

			}
			if(remind.getZhendong().equals("1"))
			{
				imgZhendong.setBackgroundResource(R.drawable.kaiqi);

			}
			else
			{
				imgZhendong.setBackgroundResource(R.drawable.guanbi);

			}
		}


		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		imgVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(remind.getVoice().equals("1"))
				{
					imgVoice.setBackgroundResource(R.drawable.guanbi);
					remind.setVoice("0");
				}
				else
				{
					imgVoice.setBackgroundResource(R.drawable.kaiqi);
					remind.setVoice("1");
				}
				dao.updateRemind(remind, username);
				sendBroadcast(new Intent(ReceiverConstant.RemindReceiver_ACTION));
			}
		});
		imgZhendong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(remind.getZhendong().equals("1"))
				{
					imgZhendong.setBackgroundDrawable(getResources().getDrawable(R.drawable.guanbi));
					remind.setZhendong("0");
				}
				else
				{
					imgZhendong.setBackgroundDrawable(getResources().getDrawable(R.drawable.kaiqi));
					remind.setZhendong("1");
				}
				dao.updateRemind(remind, username);
				sendBroadcast(new Intent(ReceiverConstant.RemindReceiver_ACTION));
			}
		});






	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}


}
