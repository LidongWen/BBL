package com.beibeilian.beibeilian.im.video;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.PreferencesUtils;
import com.beibeilian.beibeilian.util.SweetAlertDialog;

public class VideoCallActivity extends Activity implements OnClickListener {

	private SurfaceView surfaceView;
	private SeekBar skbProgress;
	private Player player;
	private String videoUrl = "输入您的视频资源文件路径";//例如：http://192.168.10.120:8080/video/aaa.mp4
	private Button btnAnswercall;
	private Button btnRefusecall;
	private int start = 0;
	private int count = 0;
	private PopTimer popTimer;
	private PlayerTimer playerTimer;
	private String vip;
	private SweetAlertDialog dialog;
	private TextView callStateTextView;
	private TextView nickTextView;
	private ImageView imgJY,imgMD;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video_call);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setViews();

	}

	private void setViews() {
		playerTimer = new PlayerTimer(getApplicationContext());
		callStateTextView = (TextView) findViewById(R.id.tv_call_state);
		nickTextView = (TextView) findViewById(R.id.tv_nick);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		skbProgress = (SeekBar) findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		btnAnswercall = (Button) findViewById(R.id.btn_answer_call);
		btnRefusecall = (Button) findViewById(R.id.btn_refuse_call);
		imgJY=(ImageView) findViewById(R.id.img_jingyin);
		imgMD=(ImageView) findViewById(R.id.img_miandi);

		callStateTextView.setText("邀请您视频通话");
		nickTextView.setText("小晴晴");
		player = new Player(surfaceView, skbProgress, null, null);
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyReceiver.STARTPOP);
		registerReceiver(mReceiver, filter);

		vip = PreferencesUtils.getString(VideoCallActivity.this, "pay_vip");
		btnAnswercall.setOnClickListener(this);
		btnRefusecall.setOnClickListener(this);
//		imgJY.setOnClickListener(this);
//		imgMD.setOnClickListener(this);

	}

	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			if (dialog != null) {
				dialog.dismiss();
			}
			this.progress = progress * player.mediaPlayer.getDuration() / seekBar.getMax();
			if (progress >= 5) {
				if (TextUtils.isEmpty(vip) || !vip.equals("1")) {
					try {
						if (playerTimer != null) {
							playerTimer.stopPop();
						}
						if (popTimer != null) {
							popTimer.stopPop();
						}
						player.pause();
					} catch (Exception e) {
						// TODO: handle exception
					}
					sendBroadcast(new Intent(ReceiverConstant.StartPayDialogACTION));
					finish();
				}
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			player.mediaPlayer.seekTo(progress);
			player.setTv();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (popTimer != null) {
				popTimer.stopPop();
			}
			player.stop();
			finish();
			return true;
		}
		return true;

	}

//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		int touchEvent = event.getAction();
//		switch (touchEvent) {
//		case MotionEvent.ACTION_DOWN:
//			if (playerTimer != null) {
//				playerTimer.stopPop();
//			}
//			if (popTimer != null) {
//				popTimer.stopPop();
//			}
//			popTimer = new PopTimer(getApplicationContext());
//			popTimer.startPop();
//			break;
//		case MotionEvent.ACTION_UP:
//			break;
//		case MotionEvent.ACTION_MOVE:
//			break;
//		default:
//			break;
//		}
//		return super.onTouchEvent(event);
//	}

	@Override
	protected void onDestroy() {
		player.stop();
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent2) {
			if (MyReceiver.STARTPOP.equals(intent2.getAction())) {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
				getWindow().setAttributes(lp);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
				if (count == 0) {
					player.playUrl(videoUrl);
					playerTimer.stopPop();
					count++;
					start++;
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_answer_call:
				try {
					callStateTextView.setText("");
					dialog = new SweetAlertDialog(VideoCallActivity.this);
					dialog.setTitle("请稍候...");
					dialog.show();
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							playerTimer.startPop();
							if (playerTimer != null) {
								playerTimer.stopPop();
							}
							if (start == 0) {
								player.playUrl(videoUrl);
								start++;
							} else {
								player.start();
							}
						}
					}).start();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case R.id.btn_refuse_call:
				try {
					player.pause();
					if (popTimer != null) {
						popTimer.stopPop();
					}
					player.stop();
					finish();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;

			default:
				break;
		}
	}

}
