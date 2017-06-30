package com.beibeilian.beibeilian.seek;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.predestined.PickBallActivty;
import com.beibeilian.beibeilian.predestined.Throwball_animationActivity;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;

public class SeekThrowBallActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_throw_ball);
		ExitApplication.addActivity(this);
		ImageView throwball = (ImageView) findViewById(R.id.throwball);
		ImageView pickball = (ImageView) findViewById(R.id.pickball);
		final RelativeLayout rl_content=(RelativeLayout) findViewById(R.id.rl_content);
		final RelativeLayout rl_send=(RelativeLayout) findViewById(R.id.rl_send);
		final EditText etcontent=(EditText) findViewById(R.id.content);
		Button btn_send=(Button) findViewById(R.id.send);

		Button btnmeball=(Button) findViewById(R.id.btn_meball_id);
		btnmeball.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SeekThrowBallActivity.this,SeekMeBallActivity.class));
			}
		});

		Button btnback=(Button) findViewById(R.id.btnBack);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cont=etcontent.getText().toString().trim();
				if(cont.length()<=0)
				{
					HelperUtil.totastShow("内容不能为空", getApplicationContext());
					return;
				}
				Intent mIntent=new Intent(SeekThrowBallActivity.this, Throwball_animationActivity.class);
				mIntent.putExtra("content",cont);
				startActivity(mIntent);
				rl_content.setVisibility(View.GONE);
				rl_send.setVisibility(View.GONE);
				etcontent.setText("");
			}
		});
		throwball.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl_content.setVisibility(View.VISIBLE);
				rl_send.setVisibility(View.VISIBLE);
			}
		});
		pickball.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SeekThrowBallActivity.this, PickBallActivty.class));
			}
		});

	}

}
