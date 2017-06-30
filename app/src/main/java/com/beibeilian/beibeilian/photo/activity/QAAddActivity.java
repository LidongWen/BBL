package com.beibeilian.beibeilian.photo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.photo.util.Bimp;
import com.beibeilian.beibeilian.photo.util.FileUtils;
import com.beibeilian.beibeilian.photo.util.ImageItem;
import com.beibeilian.beibeilian.photo.util.PublicWay;
import com.beibeilian.beibeilian.photo.util.Res;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.beibeilian.beibeilian.util.fileupload.QAFileUploadMultipartPost;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QAAddActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;
	private BBLDao dao;
	private Dialog dialog;
	EditText et_content;
	private List<String> imgList = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Res.init(QAAddActivity.this);
		// TODO Auto-generated method stub
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(QAAddActivity.this);
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		setContentView(parentView);
		Init();
		dao = new BBLDao(QAAddActivity.this, null, null, 1);
		dialog = new Dialog(QAAddActivity.this, R.style.theme_dialog_alert);

	}

	private class PublishThread extends Thread {

		@Override
		public void run() {
			try {
				String type = "0";
				String photo = "";
				if (Bimp.tempSelectBitmap != null && Bimp.tempSelectBitmap.size() > 0) {
					type = "1";
					photo = new Gson().toJson(imgList).toString();
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", dao.queryUserByNewTime().getUsername());
				map.put("content", et_content.getText().toString().trim());
				map.put("type", type);
				map.put("photo", photo);
				JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.AddComplanit, map));
				if (jsonObject.length() > 0) {
					if (jsonObject.getInt("result") > 0) {
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(0);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("test", e.toString());
				handler.sendEmptyMessage(-1);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					dialog.dismiss();
					HelperUtil.totastShow("发送成功,将进行人工审核...", getApplicationContext());
					sendBroadcast(new Intent(ReceiverConstant.FileUploadSuccess_ACTION));
					finish();
					break;
				case 0:
					dialog.dismiss();
					HelperUtil.totastShow("发送失败,请重试", getApplicationContext());
					break;
				case -1:
					dialog.dismiss();
					HelperUtil.totastShow(PublicConstant.ToastCatch, getApplicationContext());
					break;

				default:
					break;
			}
		}
	};

	public void Init() {
		TextView btsend = (TextView) parentView.findViewById(R.id.activity_selectimg_send);
		et_content = (EditText) parentView.findViewById(R.id.et_cirle_content);
		Button btnBack = (Button) parentView.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Bimp.tempSelectBitmap != null) {
					Bimp.tempSelectBitmap.clear();
					bimap = null;
					adapter=null;
				}
				finish();
			}
		});
		btsend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String cont=et_content.getText().toString().trim();
					if(cont.length()<5)
					{
						HelperUtil.totastShow("内容不能少于5个字",getApplicationContext());
						return;
					}
					if(cont.length()>1000)
					{
						HelperUtil.totastShow("内容太长",getApplicationContext());
						return;
					}
					if(Bimp.tempSelectBitmap != null && Bimp.tempSelectBitmap.size() > 0&&Bimp.tempSelectBitmap.size()>9)
					{
						HelperUtil.totastShow("图片不能超过9个",getApplicationContext());
						return;
					}
					if (imgList != null && imgList.size() > 0)
						imgList.clear();
					HelperUtil.customDialogShow(dialog, QAAddActivity.this, "正在发送中...");
					String username = dao.queryUserByNewTime().getUsername();
					if (Bimp.tempSelectBitmap != null && Bimp.tempSelectBitmap.size() > 0) {
						for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
							String imgurl = Bimp.tempSelectBitmap.get(i).getImagePath();
							String filename = username + HelperUtil.DateTime()+HelperUtil.get6Random() + i + ".jpg";
							imgList.add(filename);
							Log.e("test", "文件路径" + imgurl + "新名" + filename);
							// HelperUtil.getThreadPools().submit(new
							// FileUploadTask(imgurl, filename));
							new QAFileUploadMultipartPost(QAAddActivity.this, imgurl, filename).execute("");
						}
					}
					Log.e("test", "end");

					new PublishThread().start();
				} catch (Exception e) {
					handler.sendEmptyMessage(-1);
				}

			}
		});

		pop = new PopupWindow(QAAddActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);

		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(QAAddActivity.this, AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(
							AnimationUtils.loadAnimation(QAAddActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(QAAddActivity.this, GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image
						.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						adapter.notifyDataSetChanged();
						break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case TAKE_PICTURE:
				if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

					String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUtils.saveBitmap(bm, fileName);

					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);

					Bimp.tempSelectBitmap.add(takePhoto);
				}
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (Bimp.tempSelectBitmap != null) {
				Bimp.tempSelectBitmap.clear();
				bimap = null;
				adapter=null;
			}
			finish();
		}
		return true;
	}

}
