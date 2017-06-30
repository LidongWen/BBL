package com.beibeilian.beibeilian;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;

public class RegsiterOneActivity extends Activity implements OnClickListener {
	private Button nextstepbtn;
	private EditText username;
	private EditText password;

	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regsiter_one);
		ExitApplication.addActivity(RegsiterOneActivity.this);
		nextstepbtn = (Button) findViewById(R.id.regsiter_xyb_id);
		username = (EditText) findViewById(R.id.emailid);
		password = (EditText) findViewById(R.id.passid);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		nextstepbtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
			case R.id.regsiter_xyb_id:
				String user = username.getText().toString().trim();
				String pass = password.getText().toString().trim();
				if (user.length() > 0 && pass.length() > 0) {
					if(user.equals("admin")||user.equals("ADMIN")||user.equals("beibeilian"))
					{
						HelperUtil.totastShow("此帐号禁止注册", this);
						return;
					}
					if(user.length()>15)
					{
						HelperUtil.totastShow("账号不能高于15位", this);
						return;
					}
					if (HelperUtil.inputMactches(user)) {
						if (pass.length()>=6&&pass.length() <= 16) {
							if (!HelperUtil.CheckChinese(pass)) {
								intent = new Intent(RegsiterOneActivity.this,
										RegsiterTwoActivity.class);
								intent.putExtra("username", user);
								intent.putExtra("password", pass);
								startActivity(intent);
//							finish();
							} else {
								HelperUtil.totastShow("密码不能包含中文", this);
							}
						} else {
							HelperUtil.totastShow("密码不能低于6位高于16位", this);
						}
					} else {
						HelperUtil.totastShow("帐号由数字、字母组成", this);
					}
				} else {
					HelperUtil.totastShow("帐号和密码不能为空", this);
				}
				break;
			case R.id.btnBack:
//			startActivity(new Intent(RegsiterOneActivity.this,
//					LoginActivity.class));
				finish();
				break;
			default:
				break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			startActivity(new Intent(RegsiterOneActivity.this,
//					LoginActivity.class));
			finish();
		}
		return false;
	}
}
