package com.beibeilian.beibeilian.predestined;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Throwball_animationActivity extends Activity {

	// private AnimationDrawable ad;

	private RelativeLayout chuck_bottle_layout;
	BBLDao dao;
	String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chuck_pop1);
		dao = new BBLDao(Throwball_animationActivity.this, null, null, 1);
//		chuck_empty2 = (ImageView) findViewById(R.id.chuck_empty2);
		// chuck_empty1=(ImageView)findViewById(R.id.chuck_empty1);
		// chuck_spray = (ImageView) findViewById(R.id.chuck_spray);
		chuck_bottle_layout = (RelativeLayout) findViewById(R.id.chuck_bottle_layout);
		content=getIntent().getStringExtra("content");
		Animation animationR = AnimationUtils.loadAnimation(this, R.anim.anim_set);
		Animation animationS = AnimationUtils.loadAnimation(this, R.anim.anim_set);
		Animation animationT = AnimationUtils.loadAnimation(this, R.anim.chuck_bottle_translate);
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(animationR);
		set.addAnimation(animationS);
		set.addAnimation(animationT);
		doStartAnimation(set);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if(content==null) return;
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", dao.queryUserByNewTime().getUsername());
					map.put("content", content);
					String result = HelperUtil.postRequest(HttpConstantUtil.Throwball, map);
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.optInt("result") > 0) {
					} else {
						new Handler().post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								HelperUtil.totastShow("失败", getApplicationContext());
							}
						});
					}
				} catch (Exception e) {
					new Handler().post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							HelperUtil.totastShow(PublicConstant.ToastCatch, getApplicationContext());
						}
					});
				}
			}
		}).start();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
			}
		}, 2000);

	}

	private void doStartAnimation(AnimationSet set) {
		// chuck_empty1.startAnimation(set);
		// chuck_empty2.startAnimation(set);
		chuck_bottle_layout.startAnimation(set);
	}

}
