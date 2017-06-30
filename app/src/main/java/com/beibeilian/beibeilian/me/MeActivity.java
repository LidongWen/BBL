package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.circle.widgets.CircularImage;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.model.UserInfoEntiy;
import com.beibeilian.beibeilian.me.model.Version;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.seek.qaq.MeCircleMainActivity;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.PreferencesUtils;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.UpdateManager;

import java.io.ByteArrayOutputStream;

public class MeActivity extends Activity implements OnClickListener {
	private RelativeLayout me_data;
	private RelativeLayout me_lonelycondition;
	private RelativeLayout me_photo;
	private RelativeLayout me_dubai;
	private RelativeLayout me_account;
	private RelativeLayout me_set;
	private RelativeLayout me_member;
	private RelativeLayout me_visit;
	private RelativeLayout me_anlian;
	private RelativeLayout me_auth;
	private RelativeLayout me_qaq;

	private CircularImage headImg;
	private BBLDao dao;
	private TextView tv_username;
	private TextView tv_nickname;
	private TextView tv_level;
	private UserInfoEntiy user;
	private Dialog dialog;
	private IntentFilter intentFilter;
	private FileUploadReceiver fileUploadReceiver;
	private TextView tv_version;
	private TextView tv_visitstate, remind_anlian, tv_myqaq;
	private IntentFilter mIntentFilter;
	private RemianReceiver mRemianReceiver;
	private int vipState = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me);
		ExitApplication.addActivity(MeActivity.this);
		dao = new BBLDao(MeActivity.this, null, null, 1);

		me_account = (RelativeLayout) findViewById(R.id.me_zhanghao_id);
		me_data = (RelativeLayout) findViewById(R.id.me_data_id);
		me_lonelycondition = (RelativeLayout) findViewById(R.id.me_lonelycondition_id);
		me_photo = (RelativeLayout) findViewById(R.id.me_photo_id);
		me_dubai = (RelativeLayout) findViewById(R.id.me_dubai_id);
		me_set = (RelativeLayout) findViewById(R.id.me_set_id);
		me_member = (RelativeLayout) findViewById(R.id.me_member_id);
		me_visit = (RelativeLayout) findViewById(R.id.me_myvisit_id);
		me_anlian = (RelativeLayout) findViewById(R.id.me_anlian_id);
		me_auth = (RelativeLayout) findViewById(R.id.me_auth_id);
		me_qaq = (RelativeLayout) findViewById(R.id.me_qaq_id);

		headImg = (CircularImage) findViewById(R.id.me_headimage_id);
		tv_level = (TextView) findViewById(R.id.me_dengji_id);
		tv_nickname = (TextView) findViewById(R.id.me_nickname_id);
		tv_username = (TextView) findViewById(R.id.me_account_id);
		tv_version = (TextView) findViewById(R.id.version_remind_id);

		tv_visitstate = (TextView) findViewById(R.id.myvisit_remind_id);
		remind_anlian = (TextView) findViewById(R.id.anlian_remind_id);
		tv_myqaq = (TextView) findViewById(R.id.qaq_remind_id);
		fileUploadReceiver = new FileUploadReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(ReceiverConstant.ME_HEADIMAGE_FILEPATH_ACTION);
		registerReceiver(fileUploadReceiver, intentFilter);
		user = dao.queryUserByNewTime();
//		vipState = dao.findVipMember(user.getUsername());
//		if(vipState>0)
//		{
//			me_member.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			me_member.setVisibility(View.GONE);
//		}
		tv_level.setText(user.getLevel() + "级");
		tv_nickname.setText(user.getNickname());
		tv_username.setText(user.getUsername());
		String photourl = null;
		UserInfo userinfo = dao.queryPhoto(user.getUsername());
		if (userinfo != null)
			photourl = userinfo.getPhoto() == null ? "" : userinfo.getPhoto();

		if (HelperUtil.flagISNoNull(photourl) && photourl.contains(".jpg")) {
			try {
				HelperUtil.getPicassoImageByUrl(MeActivity.this, photourl,
						getResources().getDrawable(R.drawable.icon_touxiang), headImg);
			} catch (Exception e) {
			}
		} else {
			headImg.setImageResource(R.drawable.icon_touxiang);
		}
		me_data.setOnClickListener(this);
		me_lonelycondition.setOnClickListener(this);
		me_photo.setOnClickListener(this);
		me_dubai.setOnClickListener(this);
		me_account.setOnClickListener(this);
		me_set.setOnClickListener(this);
		me_member.setOnClickListener(this);
		me_anlian.setOnClickListener(this);
		me_visit.setOnClickListener(this);
		me_auth.setOnClickListener(this);
		me_qaq.setOnClickListener(this);
		Version version = dao.queryVersion();

		if (version != null) {
			if (HelperUtil.flagISNoNull(version.getCode())) {
				if (HelperUtil.isUpdateVersion(HelperUtil.getVersionCode(MeActivity.this), version.getCode())) {
					tv_version.setVisibility(View.VISIBLE);
				}
			}
		}

		mIntentFilter = new IntentFilter();
		mRemianReceiver = new RemianReceiver();
		mIntentFilter.addAction(ReceiverConstant.TAB_FOUR_REMAIND_ACTION);
		registerReceiver(mRemianReceiver, mIntentFilter);

	}

	private class RemianReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ReceiverConstant.TAB_FOUR_REMAIND_ACTION)) {
				String type = intent.getStringExtra("type");
				if (type.equals(PublicConstant.VISITTYPE)) {
					updateVisitState();
					tv_visitstate.setVisibility(View.VISIBLE);
				}
				if (type.equals(PublicConstant.ANLIANTYPE)) {
					updateAnliantState();
					remind_anlian.setVisibility(View.VISIBLE);
				}
				if (type.equals(PublicConstant.MYQAQTYPE)) {
					updateMyqaqState();
					tv_myqaq.setVisibility(View.VISIBLE);
				}

			}
		}
	}

	public void updateVisitState() {
		PreferencesUtils.putInt(MeActivity.this, "visitstate", 0);
	}

	public void updateAnliantState() {
		PreferencesUtils.putInt(MeActivity.this, "anlianstate", 0);
	}

	public void updateMyqaqState() {
		PreferencesUtils.putInt(MeActivity.this, "myqaqstate", 0);
	}

	@Override
	public void onResume() {
		super.onResume();
		user = dao.queryUserByNewTime();
		tv_nickname.setText(user.getNickname());
		if (HelperUtil.flagISNoNull(user.getSex()) && user.getSex().equals("男")) {
			tv_nickname.setTextColor(getResources().getColor(R.color.nan));
		} else {
			tv_nickname.setTextColor(getResources().getColor(R.color.nv));
		}

		int visitstate = PreferencesUtils.getInt(MeActivity.this, "visitstate");
		if (visitstate == 0) {
			tv_visitstate.setVisibility(View.VISIBLE);
		} else {
			tv_visitstate.setVisibility(View.GONE);
		}
		int remindanlian = PreferencesUtils.getInt(MeActivity.this, "anlianstate");
		if (remindanlian == 0) {
			remind_anlian.setVisibility(View.VISIBLE);
		} else {
			remind_anlian.setVisibility(View.GONE);
		}
		int myqaq = PreferencesUtils.getInt(MeActivity.this, "myqaqstate");
		if (myqaq == 0) {
			tv_myqaq.setVisibility(View.VISIBLE);
		} else {
			tv_myqaq.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
			case R.id.me_data_id:
				intent = new Intent(MeActivity.this, MeDataActivity.class);
				startActivity(intent);
				break;
			case R.id.me_lonelycondition_id:
				intent = new Intent(MeActivity.this, MeLonelyconditionActivity.class);
				startActivity(intent);
				break;

			case R.id.me_photo_id:
				intent = new Intent(MeActivity.this, MePhotoActivity.class);
				startActivity(intent);
				break;
			case R.id.me_dubai_id:
				intent = new Intent(MeActivity.this, MeMonologueActivity.class);
				startActivity(intent);
				break;
			case R.id.me_zhanghao_id:

				Intent intentFromGallery = new Intent();
				intentFromGallery.setType("image/*"); // 设置文件类型
				intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intentFromGallery, 1);
				break;
			case R.id.me_set_id:
				intent = new Intent(MeActivity.this, MeSetActivity.class);
				startActivity(intent);
				break;
			case R.id.me_member_id:
				intent = new Intent(MeActivity.this, MeMemberActivity.class);
				startActivity(intent);
				break;
			case R.id.me_anlian_id:
				PreferencesUtils.putInt(MeActivity.this, "anlianstate", 1);
				startActivity(new Intent(MeActivity.this, MeAnlianActivity.class));
				break;
			case R.id.me_myvisit_id:
				PreferencesUtils.putInt(MeActivity.this, "visitstate", 1);
				startActivity(new Intent(MeActivity.this, MeVisitActivity.class));
				break;
			case R.id.me_auth_id:
				startActivity(new Intent(MeActivity.this, MeAuthActivity.class));
				break;
			case R.id.me_qaq_id:
				PreferencesUtils.putInt(MeActivity.this, "myqaqstate", 1);
				startActivity(new Intent(MeActivity.this, MeCircleMainActivity.class));
				break;
			default:
				break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != MeActivity.this.RESULT_CANCELED) {
			switch (requestCode) {
				case 1:
					Bitmap bmp = null;
					Uri uri = data.getData();
					ContentResolver cr = this.getContentResolver();
					try {
						bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ByteArrayOutputStream baostemp = null;
						bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						if (baos.size() > 300000) {
							// baos.flush();
							// baos.close();
							// System.out.println("io==" + baos.size());
							baostemp = new ByteArrayOutputStream();
							HelperUtil.compressImage(baos).compress(Bitmap.CompressFormat.JPEG, 100, baostemp);
							Intent intent = new Intent(MeActivity.this, MeHeadImageActivity.class);
							intent.putExtra("bitmap", baostemp.toByteArray());
							startActivity(intent);
						} else {
							Intent intent = new Intent(MeActivity.this, MeHeadImageActivity.class);
							intent.putExtra("bitmap", baos.toByteArray());
							startActivity(intent);
						}
						// System.out.println(baostemp.size());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				// case 2:
				//
				// getImageToView(data);
				// break;
			}
		}

	}

	/**
	 * 裁剪图片方法实现
	 *
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	private class FileUploadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals(ReceiverConstant.ME_HEADIMAGE_FILEPATH_ACTION)) {
				String filepath = intent.getStringExtra("getfilepath");
				Bitmap bm = BitmapFactory.decodeFile(filepath);
				headImg.setImageBitmap(bm);
			}
		}

	}

	// /**
	// * 保存裁剪之后的图片数据 处理上传
	// *
	// * @param picdata
	// */
	// private static String filepath;
	// private static String filename;
	//
	// private void getImageToView(final Intent data) {
	// dialog = new Dialog(MeActivity.this, R.style.theme_dialog_alert);
	// HelperUtil.customDialogShow(dialog, MeActivity.this, "正在上传...");
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// try {
	// filename = dao.queryUserByNewTime().getUsername()
	// + HelperUtil.DateTime() + ".jpg";
	// filepath = PublicConstant.FilePath + filename;
	// Bundle extras = data.getExtras();
	// if (extras != null) {
	// Bitmap photo = extras.getParcelable("data");
	// File filedir = new File(PublicConstant.FilePath);
	// if (!filedir.exists()) filedir.mkdirs();
	// File file = new File(filepath);
	// file.createNewFile();
	// FileOutputStream out = new FileOutputStream(file);
	// if (photo
	// .compress(Bitmap.CompressFormat.JPEG, 100, out)) {
	// out.flush();
	// out.close();
	// }
	// FileUploadMultipartPost fileUploadMultipartPost = new
	// FileUploadMultipartPost(
	// MeActivity.this);
	// fileUploadMultipartPost.execute(filepath);
	//
	// // int res = new UploadFileUtil(filepath,
	// // HttpConstantUtil.FILEUpload).call();
	// // if (res == 0) {
	// // handler.sendEmptyMessage(-1);
	// // return;
	// // }
	//
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// handler.sendEmptyMessage(-1);
	// }
	// }
	// }).start();
	//
	// }

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				// case 1:
				// if (dialog != null)
				// dialog.dismiss();
				// dao.updatePhoto(user.getUsername(), HttpConstantUtil.PreUrl
				// + "upload/" + filename, user.getNickname(),user.getSex());
				// HelperUtil.totastShow("上传成功", MeActivity.this);
				// Bitmap bm = BitmapFactory.decodeFile(filepath);
				// headImg.setImageBitmap(bm);
				// break;
				case -1:
					if (dialog != null)
						dialog.dismiss();
					HelperUtil.totastShow("请检查网络是否可用或稍候再试", MeActivity.this);
					break;
				case 2:
					HelperUtil.totastShow("当前已是最新版本", MeActivity.this);
					break;
				case 3:
					if (dialog != null)
						dialog.dismiss();
					break;
				// case 4:
				// fileuploadThread();
				// break;
				case 0:
					new UpdateManager(MeActivity.this).startUpdateInfo();
					break;
				default:
					break;
			}
		}
	};

	// private void fileuploadThread() {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// try {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("username", user.getUsername());
	// map.put("photourl", filename);
	// JSONObject jsonObject = new JSONObject(
	// HelperUtil.postRequest(
	// HttpConstantUtil.UpHeadImage, map));
	// int result = jsonObject.getInt("result");
	// if (result > 0) {
	// handler.sendEmptyMessage(1);
	// }
	// } catch (Exception e) {
	// handler.sendEmptyMessage(-1);
	// }
	// }
	// }).start();
	// }
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (fileUploadReceiver != null) {
			unregisterReceiver(fileUploadReceiver);
		}

	}
}
