package com.beibeilian.beibeilian.orderdialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.WelcomeActivity;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import c.b.BP;
import c.b.PListener;

public class OrderDailog extends Activity {

	private RelativeLayout rl_order_one_ck;

	private RelativeLayout rl_order_two_ck;

	private RelativeLayout rl_order_three_ck;

	private RelativeLayout rl_order_four_ck;

	private RelativeLayout rl_order_zfb_ck;

	private RelativeLayout rl_order_wx_ck;

	private TextView tv_cancel, tv_order;

	private ImageView img_one, img_two, img_three, img_four, img_zfb, img_wx;

	private String APPID = "c0861759d6e7ed16c942a573de7fc70b";

	private int PLUGINVERSION = 7;

	private ProgressDialog dialog;

	private int select_value = 1;// 10元

	private int payway_value = 1;

	private BBLDao dao;

	private Dialog mdialog;

	private String member_value = null;

	private String username = "";

	private double price = 10;

	private String orderno = "";

	private String price_15="5",price_30="10",price_180="50",price_360="100";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_dialog);
		dao = new BBLDao(OrderDailog.this, null, null, 1);
		PayRule mPayRule=dao.findPayRule();
		if(mPayRule!=null&& HelperUtil.flagISNoNull(mPayRule.getPrice_15()))
		{
			price_15=mPayRule.getPrice_15();
			price_30=mPayRule.getPrice_30();
			price_180=mPayRule.getPrice_180();
			price_360=mPayRule.getPrice_360();
		}
		mdialog = new Dialog(OrderDailog.this, R.style.theme_dialog_alert);
		username = dao.queryUserByNewTime().getUsername();
		rl_order_one_ck = (RelativeLayout) findViewById(R.id.rl_order_one);
		rl_order_two_ck = (RelativeLayout) findViewById(R.id.rl_order_two);
		rl_order_three_ck = (RelativeLayout) findViewById(R.id.rl_order_three);
		rl_order_four_ck = (RelativeLayout) findViewById(R.id.rl_order_four);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_order = (TextView) findViewById(R.id.tv_order);
		rl_order_zfb_ck = (RelativeLayout) findViewById(R.id.rl_order_zhifubao);
		rl_order_wx_ck = (RelativeLayout) findViewById(R.id.rl_order_weixin);
		img_one = (ImageView) findViewById(R.id.img_one);
		img_two = (ImageView) findViewById(R.id.img_two);
		img_three = (ImageView) findViewById(R.id.img_three);
		img_four = (ImageView) findViewById(R.id.img_four);
		img_zfb = (ImageView) findViewById(R.id.img_zfb);
		img_wx = (ImageView) findViewById(R.id.img_wx);
		member_value = getIntent().getStringExtra("member_value");

		TextView tv15=(TextView) findViewById(R.id.tv_price_15);
		TextView tv30=(TextView) findViewById(R.id.tv_price_30);
		TextView tv180=(TextView) findViewById(R.id.tv_price_180);
		TextView tv360=(TextView) findViewById(R.id.tv_price_360);
		tv15.setText(price_15+"元/15天");
		tv30.setText(price_30+"元/1个月");
		tv180.setText(price_180+"元/半年");
		tv360.setText(price_360+"元/1年");

		rl_order_one_ck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_one.setImageDrawable(getResources().getDrawable(R.drawable.select));
				img_two.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_three.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_four.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				select_value = 0;

			}
		});
		rl_order_two_ck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_one.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_two.setImageDrawable(getResources().getDrawable(R.drawable.select));
				img_three.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_four.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				select_value = 1;

			}
		});
		rl_order_three_ck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_one.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_two.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_three.setImageDrawable(getResources().getDrawable(R.drawable.select));
				img_four.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				select_value = 2;

			}
		});
		rl_order_four_ck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_one.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_two.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_three.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_four.setImageDrawable(getResources().getDrawable(R.drawable.select));
				select_value = 3;

			}
		});

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				BP.init(APPID);
//				if(payway_value==0)
//				{
//					pay(true);
//				}
//				else
//				{
//					pay(false);
//				}
//				HelperUtil.sendUserPayAction(OrderDailog.this, username, BBLConstant.ACTION_PAY_CONFIRM, "0", "",
//						String.valueOf(price));
			}
		});
		rl_order_zfb_ck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				img_zfb.setImageDrawable(getResources().getDrawable(R.drawable.select));
				img_wx.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				payway_value = 0;
			}
		});
		rl_order_wx_ck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_zfb.setImageDrawable(getResources().getDrawable(R.drawable.unenable));
				img_wx.setImageDrawable(getResources().getDrawable(R.drawable.select));
				payway_value = 1;

			}
		});
//		int pluginVersion = BP.getPluginVersion(this);
//		if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
//			Toast.makeText(OrderDailog.this,
//					pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)" : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
//					0).show();
//			installBmobPayPlugin("bp.db");
//		}

	}

	private void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName + ".apk");
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	int error=0;
	/**
	 * 调用支付
	 *
	 * @param alipayOrWechatPay
	 *            支付类型，true为支付宝支付,false为微信支付
	 */
	private void pay(final boolean alipayOrWechatPay) {
		showDialog("正在获取订单...");
		String orderlong = "一个月";
		if (select_value == 0) {
			orderlong = "十五天";
			price =Double.parseDouble(price_15);
		} else if (select_value == 1) {
			orderlong = "一个月";
			price = Double.parseDouble(price_30);
		} else if (select_value == 2) {
			orderlong = "半年";
			price = Double.parseDouble(price_180);
		} else if (select_value == 3) {
			orderlong = "一年";
			price = Double.parseDouble(price_360);
		}
		BP.pay("背背恋VIP会员购买" + orderlong, orderlong, price, alipayOrWechatPay, new PListener() {

			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				error=1;
				HelperUtil.totastShow("支付失败", OrderDailog.this);
				hideDialog();
				HelperUtil.sendUserPayAction(OrderDailog.this, username, BBLConstant.ACTION_PAY_UNKNOW,
						"支付结果未知,订单号:" + orderno, "", String.valueOf(price));
				sendOrderInfo("0","未知");
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				error=0;
				HelperUtil.totastShow("支付成功,将会重新启动!", OrderDailog.this);
				hideDialog();
				dao.insertVipMember(username);
				handler.sendEmptyMessage(0);
				sendOrderInfo("1","0");
				HelperUtil.sendUserPayAction(OrderDailog.this, username, BBLConstant.ACTION_PAY_SUCCESS,
						"订单号:" + orderno, "", String.valueOf(price));

			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				orderno = orderId;
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				showDialog("获取订单成功!请等待跳转到支付页面~");
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {
				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				if (code == -3) {
					HelperUtil.totastShow("监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付", OrderDailog.this);
					installBmobPayPlugin("bp.db");
				} else if (code == -2) {
					error=1;
					HelperUtil.totastShow("支付失败", OrderDailog.this);
					HelperUtil.sendUserPayAction(OrderDailog.this, username, BBLConstant.ACTION_PAY_FAILE,
							"订单号:" + orderno + ",失败原因:" + reason + ",code:" + code, "", String.valueOf(price));
					sendOrderInfo("0","失败原因:" + reason+",code:" + code);
				} else {
					error=1;
					HelperUtil.totastShow("支付失败", OrderDailog.this);
					HelperUtil.sendUserPayAction(OrderDailog.this, username, BBLConstant.ACTION_PAY_UNKNOW_NO,
							"订单号:" + orderno + ",失败原因:" + reason + ",code:" + code, "", String.valueOf(price));
					sendOrderInfo("0","失败原因:" + reason+",code:" + code);
				}
				hideDialog();
			}
		});
	}

	private void sendOrderInfo(final String state,final String errorinfo) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO AutoS-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", dao.queryUserByNewTime().getUsername());
					map.put("payway", String.valueOf(payway_value));
					map.put("membertype", String.valueOf(select_value));
					map.put("orderno", orderno);
					map.put("version", HelperUtil.getVersionCode(OrderDailog.this));
					map.put("imie", HelperUtil.getIMIE(OrderDailog.this));
					map.put("price", String.valueOf(price));
					map.put("state",state);
					map.put("errorinfo",errorinfo);
					map.put("uploadtype","0");
					map.put("channel",HelperUtil.getChannelId(OrderDailog.this) );
					map.put("appid",HelperUtil.getAPPlId(OrderDailog.this));
					HelperUtil.postRequest(HttpConstantUtil.SendOrderInfo, map);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					HelperUtil.customDialogShow(mdialog, OrderDailog.this, "请稍候...");
					break;
				case 1:
					if (mdialog != null)
						mdialog.dismiss();
//				if (member_value == null || !member_value.equals("1")) {
//					dao.updateImSeeState(getIntent().getStringExtra("toUser"));
//					Intent intent = new Intent(OrderDailog.this, PrivateletterChatActivity.class);
//					intent.putExtra("toUser", getIntent().getStringExtra("toUser"));
//					intent.putExtra("toName", getIntent().getStringExtra("toName"));
//					startActivity(intent);
//				} else {
//					sendBroadcast(new Intent(ReceiverConstant.ME_MEMBER_REFESH_ACTION));
//				}
//				finish();
					if(error==1)
					{
						return;
					}
					Intent intent = new Intent(OrderDailog.this,
							WelcomeActivity.class);
					PendingIntent restartIntent = PendingIntent
							.getActivity(
									OrderDailog.this,
									0,
									intent,
									Intent.FLAG_ACTIVITY_NEW_TASK);
					AlarmManager mgr = (AlarmManager) OrderDailog.this
							.getSystemService(Context.ALARM_SERVICE);
					mgr.set(AlarmManager.RTC,
							System.currentTimeMillis() + 2000,
							restartIntent);
					ExitApplication.exit();
					android.os.Process
							.killProcess(android.os.Process
									.myUid());
					System.exit(0);
					break;

				case -1:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("网请检查网络是否可用或稍候重试", getApplicationContext());
					break;
				default:
					break;
			}
		}
	};

	private void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
		}
	}

	private void hideDialog() {
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}
}
