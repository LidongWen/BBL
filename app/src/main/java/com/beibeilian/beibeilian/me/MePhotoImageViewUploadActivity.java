package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.fileupload.FileUploadMultipartPost;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MePhotoImageViewUploadActivity extends Activity {

	private ImageView imageview;
	private Button btnUpload;
	private Button btnCancel;
	private String filePath = null;
	private Dialog dialog;
	private BBLDao dao;

	private IntentFilter intentFilter;

	private String newfilepath;
	private FileUploadReceiver fileUploadReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img_preview);
		ExitApplication.addActivity(this);
		fileUploadReceiver = new FileUploadReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(ReceiverConstant.FileUploadFaile_ACTION);
		intentFilter.addAction(ReceiverConstant.FileUploadSuccess_ACTION);
		registerReceiver(fileUploadReceiver, intentFilter);
		dialog = new Dialog(MePhotoImageViewUploadActivity.this, R.style.theme_dialog_alert);
		dao = new BBLDao(MePhotoImageViewUploadActivity.this, null, null, 1);
		imageview = (ImageView) findViewById(R.id.img_show);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnUpload = (Button) findViewById(R.id.btn_upload);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HelperUtil.customDialogShow(dialog, MePhotoImageViewUploadActivity.this, "正在上传...");
				upload();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		filePath = getIntent().getStringExtra("filePath");

		if (filePath != null) {
			try {
				File file = new File(filePath);
				FileInputStream fs = null;
				BitmapFactory.Options bfOptions = new BitmapFactory.Options();
				bfOptions.inDither = false;
				bfOptions.inPurgeable = true;
				bfOptions.inInputShareable = true;
				bfOptions.inSampleSize = 2;
				bfOptions.inTempStorage = new byte[64 * 1024];
				fs = new FileInputStream(file);
				Bitmap bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
				Drawable drawable = new BitmapDrawable(bm);
				imageview.setBackgroundDrawable(drawable);
			} catch (Exception e) {

			}
		}
	}

	private class FileUploadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ReceiverConstant.FileUploadSuccess_ACTION)) {
				handler.sendEmptyMessage(1);
			}
			if (intent.getAction().equals(ReceiverConstant.FileUploadFaile_ACTION)) {
				handler.sendEmptyMessage(-1);
			}
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					uploadData();
					break;
				case -1:
					if (dialog != null) {
						dialog.dismiss();
					}
					HelperUtil.totastShow("请检查网络是否可用或稍候再试", MePhotoImageViewUploadActivity.this);
					break;
				default:
					break;
			}
		}
	};

	private void uploadData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", dao.queryUserByNewTime().getUsername());
					map.put("photourl", newfilepath.substring(newfilepath.lastIndexOf("/") + 1));
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.UpPhoto, map));
					int result = jsonObject.getInt("result");
					if (result == 1) {
						MePhotoImageViewUploadActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								HelperUtil.totastShow("上传成功", MePhotoImageViewUploadActivity.this);
								if (dialog != null) {
									dialog.dismiss();
								}
								finish();

							}
						});
					}

					if (result == 2) {
						MePhotoImageViewUploadActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								HelperUtil.totastShow("不能上传超过5张照片", MePhotoImageViewUploadActivity.this);
								if (dialog != null) {
									dialog.dismiss();
								}

							}
						});
					}

				} catch (Exception e) {

					MePhotoImageViewUploadActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							HelperUtil.totastShow("请检查网络是否可用或稍候再试", MePhotoImageViewUploadActivity.this);
							if (dialog != null) {
								dialog.dismiss();
							}
						}
					});

				}

			}
		}).start();
	}

	private void upload() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", dao.queryUserByNewTime().getUsername());
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.CheckPhoto, map));
					if (jsonObject.getInt("result") == 1) {
						MePhotoImageViewUploadActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								HelperUtil.totastShow("不能上传超过5张照片", MePhotoImageViewUploadActivity.this);
								if (dialog != null) {
									dialog.dismiss();
								}
							}
						});
						return;
					}
					File file = new File(filePath);
					FileInputStream fs = null;
					// TODO
					BitmapFactory.Options bfOptions = new BitmapFactory.Options();
					bfOptions.inDither = false;
					bfOptions.inPurgeable = true;
					bfOptions.inInputShareable = true;
					bfOptions.inSampleSize = 0;
					bfOptions.inTempStorage = new byte[64 * 1024];
					try {
						fs = new FileInputStream(file);
						Bitmap bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
						// Bitmap newbm = HelperUtil.compressImage(bm);

						newfilepath = PublicConstant.FilePath + dao.queryUserByNewTime().getUsername()
								+ HelperUtil.DateTime() + ".jpg";
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
						new FileUploadMultipartPost(MePhotoImageViewUploadActivity.this).execute(newfilepath);

					} catch (Exception e) {
						// TODO: handle exception
						MePhotoImageViewUploadActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								HelperUtil.totastShow("请检查网络是否可用或稍候再试", MePhotoImageViewUploadActivity.this);
								if (dialog != null) {
									dialog.dismiss();
								}
							}
						});
					}
				} catch (Exception e1) {
					// TODO: handle exception
					MePhotoImageViewUploadActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							HelperUtil.totastShow("请检查网络是否可用或稍候再试", MePhotoImageViewUploadActivity.this);
							if (dialog != null) {
								dialog.dismiss();
							}
						}
					});
				}
			}

		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (fileUploadReceiver != null) {
			unregisterReceiver(fileUploadReceiver);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}
}
