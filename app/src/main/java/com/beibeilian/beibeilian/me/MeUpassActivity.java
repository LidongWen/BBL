package com.beibeilian.beibeilian.me;

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

import com.beibeilian.beibeilian.LoginActivity;
import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.service.CoreIMService;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 修改密码
 *
 */
public class MeUpassActivity extends Activity implements OnClickListener {

	private Button btnBack, btnModify;// 返回按钮，修改密码按钮

	private EditText et_curPwd, et_newPwd, et_rePwd;// 当前密码，新密码，重复输入密码文本框

	String curName = "";// 当前姓名

	private Dialog dialog;// 定义dialog

	private BBLDao dao;// 定义dao

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_pwd_modify);
		ExitApplication.addActivity(MeUpassActivity.this);
		dialog = new Dialog(MeUpassActivity.this, R.style.theme_dialog_alert);
		dao = new BBLDao(MeUpassActivity.this, null, null, 1);
		init();
		btnModify.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		curName = dao.queryUserByNewTime().getUsername();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		et_curPwd = (EditText) findViewById(R.id.et_curPwd);
		et_newPwd = (EditText) findViewById(R.id.et_newPwd);
		et_rePwd = (EditText) findViewById(R.id.et_rePwd);
		btnModify = (Button) findViewById(R.id.btnOk);
		btnBack = (Button) findViewById(R.id.btnBack);
	}

	/**
	 * 处理返回事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnOk:
				changePwd();
				break;

			case R.id.btnBack:
				this.finish();
				break;
		}
	}

	/**
	 * 修改密码
	 */
	private void changePwd() {
		final String curPwdStr = et_curPwd.getText().toString().trim();// 当前密码
		final String newPwdStr = et_newPwd.getText().toString().trim();// 新密码
		final String rePwdStr = et_rePwd.getText().toString().trim();// 再次输入密码
		if (curPwdStr.length() == 0) {
			HelperUtil.totastShow("当前密码不能为空", MeUpassActivity.this);
			return;
		}
		if ((newPwdStr.length() == 0) || (newPwdStr.length() < 6)) {
			HelperUtil.totastShow("新密码不能为空且不能低于6位", MeUpassActivity.this);
			return;
		}

		if ((rePwdStr.length() == 0) || (rePwdStr.length() < 6)) {
			HelperUtil.totastShow("再次输入的密码不能为空且不能低于6位", MeUpassActivity.this);
			return;
		}
		if (HelperUtil.CheckChinese(newPwdStr) || HelperUtil.CheckChinese(rePwdStr)) {
			HelperUtil.totastShow("密码不能包含中文", MeUpassActivity.this);
			return;
		}

		if (newPwdStr.equals(rePwdStr)) {// 新密码与再次输入密码相同 可访问请求
			final HashMap<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("username", curName);
			rawParams.put("oldpass", HelperUtil.MD5(curPwdStr));
			rawParams.put("newpass", newPwdStr);
			HelperUtil.customDialogShow(dialog, MeUpassActivity.this, "正在请求服务器中...");
			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						String result = HelperUtil.postRequest(HttpConstantUtil.UpPass, rawParams);
						JSONObject jsonObject = new JSONObject(result);
						int number = jsonObject.getInt("result");

						// Log.e("aaa", number + "修改密码");
						if (number == 0) {
							handler.sendEmptyMessage(0);
						} else if (number == 1) {
							handler.sendEmptyMessage(1);// 1修改成功 0失败2原始密码错误
						} else {
							handler.sendEmptyMessage(2);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(3);
					}
				}
			}).start();
		} else {
			HelperUtil.totastShow("两次输入的新密码不相同", MeUpassActivity.this);
		}

	}

	/**
	 * 处理hander消息
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("修改失败,请检查网络或服务器故障", MeUpassActivity.this);
					break;
				case 1:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("修改成功,请重新登录", MeUpassActivity.this);
					dao.updateLoginState(curName);
					stopService(new Intent(MeUpassActivity.this, CoreIMService.class));

					Intent intent = new Intent(MeUpassActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
					ExitApplication.exit();
					break;
				case 2:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("原始密码错误", MeUpassActivity.this);
					break;
				case 3:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("修改失败,请求服务器异常", MeUpassActivity.this);
					break;
				default:
					break;
			}
		};
	};

	/**
	 * 处理返回事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			this.finish();
		}
		return false;
	}
}
