package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.SweetAlertDialog;
import com.beibeilian.beibeilian.util.fileupload.QAFileUploadMultipartPost;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MeAuthActivity extends Activity {
	BBLDao dao;
	String username;
	SweetAlertDialog mDialog;
	String name, card, photo;
	UploadReceiver mUploadReceiver;
	IntentFilter mIntentFilter;
	LinearLayout rl_top;
	RelativeLayout rl_bottom;
	TextView tvstate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_auth);
		rl_top = (LinearLayout) findViewById(R.id.top);
		rl_bottom = (RelativeLayout) findViewById(R.id.bottom);
		mUploadReceiver = new UploadReceiver();
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(ReceiverConstant.FileUploadSuccess_ACTION);
		registerReceiver(mUploadReceiver, mIntentFilter);
		mDialog = new SweetAlertDialog(MeAuthActivity.this);
		dao = new BBLDao(MeAuthActivity.this, null, null, 1);
		username = dao.queryUserByNewTime().getUsername();
		final EditText tv_name = (EditText) findViewById(R.id.name);
		final EditText tv_card = (EditText) findViewById(R.id.card);
		Button btn_upload = (Button) findViewById(R.id.btn_upload_id);
		tvstate = (TextView) findViewById(R.id.tv_state);
		btn_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = tv_name.getText().toString().trim();
				card = tv_card.getText().toString().trim();
				if (name.length() <= 0) {
					HelperUtil.totastShow("姓名不能为空", getApplicationContext());
					return;
				}
				if (card.length() <= 0) {
					HelperUtil.totastShow("身份证不能为空", getApplicationContext());
					return;
				}
				Intent intentFromGallery = new Intent();
				intentFromGallery.setType("image/*"); // 设置文件类型
				intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intentFromGallery, 1);
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
		init();
	}

	private void init() {
		mDialog.setTitleText("请稍候...");
		mDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.FindAuthState, map));
					if (jsonObject.length() > 0) {
						String result = jsonObject.optString("result");
						if (result.equals("0")) {
							handler.sendEmptyMessage(2);
						} else if (result.equals("1")) {
							handler.sendEmptyMessage(3);
						} else {
							handler.sendEmptyMessage(4);
						}
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(-1);
					finish();
				}
			}
		}).start();

	}

	class UploadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ReceiverConstant.FileUploadSuccess_ACTION)) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("username", username);
							map.put("card", card);
							map.put("name", name);
							map.put("photo", photo);
							JSONObject jsonObject = new JSONObject(
									HelperUtil.postRequest(HttpConstantUtil.UploadAuth, map));
							if (jsonObject.length() > 0) {
								if (jsonObject.getInt("result") > 0) {
									handler.sendEmptyMessage(1);
								} else {
									handler.sendEmptyMessage(0);
								}
							}
						} catch (Exception e) {
							handler.sendEmptyMessage(-1);
						}
					}
				}).start();
			}
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					mDialog.dismiss();
					HelperUtil.totastShow("上传成功,将进行人工审核...", getApplicationContext());
					finish();
					break;
				case 0:
					mDialog.dismiss();
					HelperUtil.totastShow("发送失败,请重试", getApplicationContext());
					break;
				case -1:
					mDialog.dismiss();
					HelperUtil.totastShow(PublicConstant.ToastCatch, getApplicationContext());
					break;
				case 2:
					mDialog.dismiss();
					rl_top.setVisibility(View.GONE);
					rl_bottom.setVisibility(View.VISIBLE);
					tvstate.setText("审核中...");
					break;
				case 3:
					mDialog.dismiss();
					rl_top.setVisibility(View.GONE);
					rl_bottom.setVisibility(View.VISIBLE);
					tvstate.setText("已审核通过!");
					break;
				case 4:
					mDialog.dismiss();
					rl_top.setVisibility(View.VISIBLE);
					rl_bottom.setVisibility(View.GONE);
					break;
				default:
					break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != MeAuthActivity.this.RESULT_CANCELED) {
			switch (requestCode) {
				case 1:
					mDialog.setTitleText("请稍候...");
					mDialog.show();
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Bitmap bmp = null;
							Uri uri = data.getData();
							ContentResolver cr = MeAuthActivity.this.getContentResolver();
							try {
								bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
								Bitmap bm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
								photo = username + HelperUtil.DateTime() + HelperUtil.get6Random() + ".jpg";
								String newfilepath = PublicConstant.FilePath + photo;
								File filedir = new File(PublicConstant.FilePath);
								if (!filedir.exists()) {
									filedir.mkdirs();
								}
								File newfile = new File(newfilepath);
								if (!newfile.exists()) {
									newfile.createNewFile();
								}
								FileOutputStream out = new FileOutputStream(newfile);
								if (bm.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
									out.flush();
									out.close();
								}
								new QAFileUploadMultipartPost(MeAuthActivity.this, newfilepath, photo).execute("");
								// System.out.println(baostemp.size());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();

					break;
				default:
					break;
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mUploadReceiver != null)
			unregisterReceiver(mUploadReceiver);
	};
}
