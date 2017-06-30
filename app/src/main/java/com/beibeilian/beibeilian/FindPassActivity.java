package com.beibeilian.beibeilian;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FindPassActivity extends Activity {

	private Button btnBack;
	private EditText et_email;
	private Button btnSend;

	private FindThread findThread;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_pass);
		ExitApplication.addActivity(FindPassActivity.this);
		dialog=new Dialog(FindPassActivity.this,R.style.theme_dialog_alert);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnSend = (Button) findViewById(R.id.sendid);
		et_email = (EditText) findViewById(R.id.emailid);
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int len=et_email.getText().toString().trim().length();
				if(len==0)
				{
					HelperUtil.totastShow("帐号不能为空",FindPassActivity.this);
					return;
				}
				HelperUtil.customDialogShow(dialog, FindPassActivity.this,"正在检测帐号... ");
				if(findThread!=null)
				{
					findThread.interrupt();
				}
				findThread=new FindThread();
				findThread.start();

			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//			   startActivity(new Intent(FindPassActivity.this,LoginActivity.class))	;
				finish();

			}
		});
	}

	private class FindThread extends Thread {

		@Override
		public void run() {
			try {

				Map<String, String> map = new HashMap<String, String>();
				map.put("username", et_email.getText().toString().trim());
				JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(
						HttpConstantUtil.FindQuesstion, map));
				handler.sendEmptyMessage(1);
				if (jsonObject.length() > 0) {
					String question=jsonObject.optString("quesstion");
					if(HelperUtil.flagISNoNull(question))
					{
						handler.sendEmptyMessage(1);
						Intent intent=new Intent(FindPassActivity.this,FindPassInputAnswerActivity.class);
						intent.putExtra("username",  et_email.getText().toString().trim());
						intent.putExtra("quesstion", question);
						startActivity(intent);
					}
					else
					{
						handler.sendEmptyMessage(2);
					}
				}
				else
				{
					handler.sendEmptyMessage(2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(0);
			}

		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					if(dialog!=null) dialog.dismiss();
					break;
				case 2:
					if(dialog!=null) dialog.dismiss();
					HelperUtil.totastShow("此帐号未设置密保或不存在此帐号",FindPassActivity.this);
					break;
				case 0:
					if(dialog!=null) dialog.dismiss();
					HelperUtil.totastShow("请检查网络是否可用或稍候再试",FindPassActivity.this);
					break;
				default:
					break;
			}
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(FindPassActivity.this,LoginActivity.class))	;
//			   finish();
		}
		return false;
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if(dialog!=null) dialog.dismiss();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(findThread!=null)
		{
			findThread.interrupt();
		}
	}

}
