package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.beibeilian.beibeilian.MainActivity;
import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.adapter.MePhotoAdapter;
import com.beibeilian.beibeilian.me.model.Photo;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MePhotoActivity extends Activity {

	private Button btnUpload;
	private ListView listView;
	private MePhotoAdapter mePhotoAdapter;
	private List<Photo> listphoto;
	private BBLDao dao;

	private Dialog mdialog;

	private Button btnBack;

	private InitThread initThread;

	private String toUser;

	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_photo);
		ExitApplication.addActivity(MePhotoActivity.this);
		mdialog = new Dialog(MePhotoActivity.this, R.style.theme_dialog_alert);
		dao = new BBLDao(MePhotoActivity.this, null, null, 1);
		btnUpload = (Button) findViewById(R.id.save_btnid);
		listView = (ListView) findViewById(R.id.me_photo_listview);
		btnBack = (Button) findViewById(R.id.btnBack);
		tv_title=(TextView)findViewById(R.id.titleid);
		toUser=getIntent().getStringExtra("toUser");
		if(HelperUtil.flagISNoNull(toUser))
		{
			String toName=getIntent().getStringExtra("toName");
			if(HelperUtil.flagISNoNull(toName))
			{
				tv_title.setText(toName+"的相册");
			}
			else
			{
				tv_title.setText(toUser+"的相册");
			}
			btnUpload.setVisibility(View.GONE);
		}
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!HelperUtil.flagISNoNull(toUser))
				{
					startActivity(new Intent(MePhotoActivity.this,
							MainActivity.class));
				}
				finish();

			}
		});

		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				HelperUtil.totastShow("请选择高清的图片，系统将会进行压缩处理",MePhotoActivity.this);
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 1);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
									long id) {
				// TODO Auto-generated method stub
				if(listphoto!=null&&listphoto.size()>0)
				{
					Intent intent=new Intent(MePhotoActivity.this,MePhotoItemDeatailActivity.class);
					intent.putExtra("photourl",listphoto.get(postion).getUrl());
					startActivity(intent);
				}
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int position, long id) {
				// TODO Auto-generated method stub
				if(listphoto.size()==0)
				{
					return false;
				}

				if(HelperUtil.flagISNoNull(toUser)&&!toUser.equals(dao.queryUserByNewTime().getUsername()))
				{
					return false;
				}

				final Photo model=listphoto.get(position);
				AlertDialog.Builder builder = new Builder(MePhotoActivity.this);

				builder.setItems(getResources().getStringArray(R.array.item_messagemenu), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{


						if (arg1 == 0)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(MePhotoActivity.this);
							builder.setMessage("您确定要删除此条记录吗?").setCancelable(false)
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											HelperUtil.customDialogShow(mdialog, MePhotoActivity.this, "正在删除中...");
											new Thread(new Runnable() {

												@Override
												public void run() {
													// TODO Auto-generated method stub
													try
													{
														Map<String,String> map=new HashMap<String,String>();
														map.put("id",model.getId());
														JSONObject jsonObject=new JSONObject(HelperUtil.postRequest(HttpConstantUtil.DelPhoto, map));
														if(jsonObject.getInt("result")>0)
														{
															handler.sendEmptyMessage(2);

														}
													}
													catch (Exception e) {
														// TODO: handle exception
														handler.sendEmptyMessage(-1);
													}
												}
											}).start();

										}
									})
									.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									}).show();
						}
						arg0.dismiss();
					}
				});
				builder.show();
				return false;
			}
		});
//		init();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		init();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK
				&& data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String filepath = c.getString(columnIndex);
			c.close();
			Intent intent = new Intent(this,
					MePhotoImageViewUploadActivity.class);
			intent.putExtra("filePath", filepath);
			startActivity(intent);
		}
	}

	private void init() {
		HelperUtil.customDialogShow(mdialog, MePhotoActivity.this, "正在加载中...");
		if(initThread!=null)
		{
			initThread.interrupt();
		}
		initThread=new InitThread();
		initThread.start();

	}

	private class InitThread extends Thread
	{
		@Override
		public void run()
		{

			try {
				listphoto = new ArrayList<Photo>();
				Map<String, String> map = new HashMap<String, String>();
				if(HelperUtil.flagISNoNull(toUser))
				{
					map.put("username", toUser);
				}
				else
				{
					map.put("username", dao.queryUserByNewTime().getUsername());
				}
				JSONArray jsonArray = new JSONArray(HelperUtil.postRequest(
						HttpConstantUtil.FindPhoto, map));
				handler.sendEmptyMessage(0);
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						listphoto.add(new Photo(jsonArray.optJSONObject(i)
								.optString("id"), jsonArray
								.optJSONObject(i).optString("time"),
								jsonArray.optJSONObject(i).optString(
										"photo")));
					}
					handler.sendEmptyMessage(1);
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				handler.sendEmptyMessage(-1);
			}
		}
	}

	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

				case 0:
					if (mdialog != null) {
						mdialog.dismiss();
					}
					break;
				case 1:
					mePhotoAdapter = new MePhotoAdapter(
							MePhotoActivity.this, listphoto,dao.queryUserByNewTime().getSex());
					listView.setAdapter(mePhotoAdapter);
					break;
				case -1:
					if (mdialog != null) {
						mdialog.dismiss();
					}
					HelperUtil.totastShow("请检查网络或稍候再试",
							MePhotoActivity.this);
					break;
				case 2:
					if(initThread!=null)
					{
						initThread.interrupt();
					}
					initThread=new InitThread();
					initThread.start();
					HelperUtil.totastShow("删除成功",
							MePhotoActivity.this);
					break;
				default:
					break;
			}


		}
	};
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(initThread!=null)
		{
			initThread.interrupt();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!HelperUtil.flagISNoNull(toUser))
			{
				startActivity(new Intent(this, MainActivity.class));
			}
			finish();
		}
		return false;
	}
}
