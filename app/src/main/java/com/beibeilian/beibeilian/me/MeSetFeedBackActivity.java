package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MeSetFeedBackActivity extends Activity {

	private Button btnsave;

	private BBLDao dao;

	private Dialog dialog;

	private EditText etContent;

	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_set_feedback);
		etContent = (EditText) findViewById(R.id.et_suggestion);
		dialog = new Dialog(MeSetFeedBackActivity.this,
				R.style.theme_dialog_alert);
		dao = new BBLDao(MeSetFeedBackActivity.this, null, null, 1);
		btnsave = (Button) findViewById(R.id.save_btnid);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		btnsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String content = etContent.getText().toString().trim();

				if (content.length() == 0 || content.length() > 100) {
					HelperUtil.totastShow("不能为空且不能超过100个字符",
							MeSetFeedBackActivity.this);
					return;
				}
				save();
			}
		});

	}

	private void save() {
		HelperUtil.customDialogShow(dialog, MeSetFeedBackActivity.this, "请稍候...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {

					Map<String, String> map = new HashMap<String, String>();
					map.put("username", dao.queryUserByNewTime().getUsername());
					map.put("code",
							HelperUtil.getVersionCode(MeSetFeedBackActivity.this));
					map.put("content", etContent.getText().toString().trim());
					JSONObject jsonObject = new JSONObject(HelperUtil
							.postRequest(HttpConstantUtil.ADDFeedback, map));
					MeSetFeedBackActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (dialog != null) {
								dialog.dismiss();
							}
						}
					});
					if (jsonObject.length() > 0) {
						if (jsonObject.getInt("result") > 0) {

							MeSetFeedBackActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											HelperUtil.totastShow("提交成功",
													MeSetFeedBackActivity.this);
											finish();
										}
									});
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					MeSetFeedBackActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (dialog != null) {
								dialog.dismiss();
							}
							HelperUtil.totastShow("请检查网络或稍候再试",
									MeSetFeedBackActivity.this);
						}
					});
				}

			}
		}).start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}
}
