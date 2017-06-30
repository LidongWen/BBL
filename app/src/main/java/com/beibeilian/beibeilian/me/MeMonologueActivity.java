package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.beibeilian.beibeilian.MainActivity;
import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MeMonologueActivity extends Activity {
	private Button savebtn;
	private EditText duibai;

	private BBLDao dao;

	private Dialog dialog;

	private Button btnBack;


	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_monologue);
		dialog = new Dialog(MeMonologueActivity.this,
				R.style.theme_dialog_alert);
		dao=new BBLDao(MeMonologueActivity.this, null, null, 1);
		username=dao.queryUserByNewTime().getUsername();
		savebtn = (Button) findViewById(R.id.duibai_save_btnid);
		duibai = (EditText) findViewById(R.id.me_duibai_content_id);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MeMonologueActivity.this,
						MainActivity.class));
				finish();

			}
		});
		savebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubt
				String content=duibai.getText().toString().trim();
				if(content.length()==0)
				{
					HelperUtil.totastShow("内容不能为空",
							MeMonologueActivity.this);
					return;
				}
				save(content);
			}
		});
		init();
	}

	private void init() {
		HelperUtil.customDialogShow(dialog, MeMonologueActivity.this, "正在加载中...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username",username);
					JSONArray jsonArray = new JSONArray(HelperUtil.postRequest(
							HttpConstantUtil.FindDubai, map));
					MeMonologueActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(dialog!=null)
									{
										dialog.dismiss();
									}
								}
							});
					if (jsonArray.length() > 0) {
						final String cont=jsonArray.optJSONObject(0)
								.optString("heartdubai");
						MeMonologueActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								duibai.setText(cont);
							}
						});
					}
				} catch (Exception e) {
					// TODO: handle exception

					MeMonologueActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(dialog!=null)
							{
								dialog.dismiss();
							}
							HelperUtil.totastShow("请检查网络或稍候再试",
									MeMonologueActivity.this);
						}
					});
				}

			}
		}).start();

	}

	private void save(final String content) {
		HelperUtil.customDialogShow(dialog, MeMonologueActivity.this, "正在保存中...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username",username);
					map.put("duibai", content);
					JSONObject jsonObject = new JSONObject(
							HelperUtil.postRequest(HttpConstantUtil.UpDubai,
									map));
					MeMonologueActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if(dialog!=null)
									{
										dialog.dismiss();
									}
								}
							});
					if (jsonObject.length() > 0) {
						if (jsonObject.getInt("result") > 0) {

							MeMonologueActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									HelperUtil.totastShow("保存成功,将进行人工审核...(不能包含联系方式、广告、不合法字符、不健康字符等等)",MeMonologueActivity.this);
								}
							});
						}
					}
				} catch (Exception e) {
					// TODO: handle exception

					MeMonologueActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(dialog!=null)
							{
								dialog.dismiss();
							}
							HelperUtil.totastShow("请检查网络或稍候再试",
									MeMonologueActivity.this);
						}
					});
				}

			}
		}).start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		return false;
	}

}
