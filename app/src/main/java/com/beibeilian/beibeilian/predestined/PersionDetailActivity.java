package com.beibeilian.beibeilian.predestined;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.MePhotoActivity;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.privateletter.PrivateletterChatActivity;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.ExitApplication;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersionDetailActivity extends Activity {

	private TextView nickname_text;
	private TextView province_TextView; // 省
	// private TextView city_TextView; // 市
	private TextView year_TextView;
	private TextView height_TextView;
	private TextView weight_TextView;
	private TextView blood_TextView;
	private TextView education_TextView;
	private TextView job_TextView;
	private TextView monthly_TextView;
	private TextView house_TextView;
	private TextView placeotherlove_TextView;
	private TextView likeothersex_TextView;
	private TextView marraypresex_TextView;
	private TextView fathermomlive_TextView;
	private TextView iswantchild_TextView;
	private TextView marraystate_TextView;
	private TextView tv_level;

	private TextView juzhu;
	private TextView year;
	private TextView shengao;
	private TextView xueli;
	private TextView shouru;
	private TextView weight;

	private TextView dubai;
	private Button btnBack;

	private Button btnSend;

	private String toUser;
	private String toName;

	private InitThread initThread;

	private Dialog mdialog;

	private TextView tv_title;

	private BBLDao dao;

	private TextView tv_photo;

	private TextView tv_report;

	private TextView tv_sex;

	private String username;

	private TextView tv_anlian;

	private String chat="1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_detail);
		ExitApplication.addActivity(PersionDetailActivity.this);
		initView();

	}

	private void initView() {

		dao = new BBLDao(PersionDetailActivity.this, null, null, 1);
		username = dao.queryUserByNewTime().getUsername();
		PayRule mPayRule=dao.findPayRule();
		if(mPayRule!=null) chat=mPayRule.getChat();
		toUser = getIntent().getStringExtra("toUser");
		toName = getIntent().getStringExtra("toName");
		mdialog = new Dialog(PersionDetailActivity.this, R.style.theme_dialog_alert);
		province_TextView = (TextView) findViewById(R.id.shengid);
		tv_title = (TextView) findViewById(R.id.titile);
		nickname_text = (TextView) findViewById(R.id.nickname_text_id);
		year_TextView = (TextView) findViewById(R.id.shengri_year_id);
		height_TextView = (TextView) findViewById(R.id.shengao_content_id);
		weight_TextView = (TextView) findViewById(R.id.tizhong_content_id);
		blood_TextView = (TextView) findViewById(R.id.xuexing_content_id);
		education_TextView = (TextView) findViewById(R.id.xueli_content_id);
		job_TextView = (TextView) findViewById(R.id.zhiye_id);
		monthly_TextView = (TextView) findViewById(R.id.yueshouru_content_id);
		house_TextView = (TextView) findViewById(R.id.fangzi_content_id);
		placeotherlove_TextView = (TextView) findViewById(R.id.yidilian_content_id);
		likeothersex_TextView = (TextView) findViewById(R.id.likeyixing_content_id);
		marraypresex_TextView = (TextView) findViewById(R.id.hunqiansex_content_id);
		fathermomlive_TextView = (TextView) findViewById(R.id.hefumu_content_id);
		iswantchild_TextView = (TextView) findViewById(R.id.isxiaohai_content_id);
		marraystate_TextView = (TextView) findViewById(R.id.hunyanzk_content_id);
		tv_sex = (TextView) findViewById(R.id.sex_text_id);
		tv_level = (TextView) findViewById(R.id.level_text_id);
		juzhu = (TextView) findViewById(R.id.juzhu_content_id);
		year = (TextView) findViewById(R.id.year_content_id);
		shengao = (TextView) findViewById(R.id.shengao_left_id);
		xueli = (TextView) findViewById(R.id.tv_xueli_content_id);
		shouru = (TextView) findViewById(R.id.shouru_content_id);
		weight = (TextView) findViewById(R.id.weight_content_id);
		tv_photo = (TextView) findViewById(R.id.photoid);
		dubai = (TextView) findViewById(R.id.dubaicontentid);
		tv_report = (TextView) findViewById(R.id.reportid);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnSend = (Button) findViewById(R.id.btnSend);
		tv_anlian = (TextView) findViewById(R.id.anlianid);
		TextView tvzhaohu=(TextView) findViewById(R.id.zhaohuid);
		if (HelperUtil.flagISNoNull(toUser) && toUser.equals(dao.queryUserByNewTime().getUsername())) {
			btnSend.setVisibility(View.GONE);
			tv_report.setVisibility(View.GONE);
			tv_photo.setVisibility(View.GONE);
			tv_anlian.setVisibility(View.GONE);
			tvzhaohu.setVisibility(View.GONE);
		}
		if (HelperUtil.flagISNoNull(toUser) && !toUser.equals(dao.queryUserByNewTime().getUsername())) {
			Intent intent = new Intent(ReceiverConstant.MESSAGE_VISIT_ACTION);
			intent.putExtra("toID", toUser);
			intent.putExtra("nickname", dao.queryUserByNewTime().getNickname());
			sendBroadcast(intent);
		}

		tv_anlian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkAnlian();
			}
		});
		tv_report.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(PersionDetailActivity.this);
				builder.setTitle("温馨提示");
				builder.setMessage("您确定要举报" + tv_title.getText().toString() + "吗?").setCancelable(false)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								HelperUtil.customDialogShow(mdialog, PersionDetailActivity.this, "请稍候...");
								new Thread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method
										// stub
										try {
											Map<String, String> map = new HashMap<String, String>();
											map.put("username", dao.queryUserByNewTime().getUsername());
											map.put("touser", toUser);
											JSONObject jsonObject = new JSONObject(
													HelperUtil.postRequest(HttpConstantUtil.ReportUser, map));
											if (jsonObject.length() > 0) {
												if (jsonObject.optInt("result") > 0) {
													PersionDetailActivity.this.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															// TODO Auto-generated
															// method stub
															if (mdialog != null) {
																mdialog.dismiss();
															}
															HelperUtil.totastShow("举报成功", PersionDetailActivity.this);
														}

													});
												}
											}
										} catch (Exception e) {
											// TODO: handle exception
											PersionDetailActivity.this.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													// TODO Auto-generated method stub
													if (mdialog != null) {
														mdialog.dismiss();
													}
													HelperUtil.totastShow("请检查网络是否可用或稍候再试", PersionDetailActivity.this);
												}

											});
										}

									}
								}).start();

							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();

			}
		});
		tv_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersionDetailActivity.this, MePhotoActivity.class);
				intent.putExtra("toUser", toUser);
				intent.putExtra("toName", toName);
				startActivity(intent);
			}
		});
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(HelperUtil.flagISNoNull(chat)&&chat.equals("1"))
				{
					checkMemberState();
				}
				else
				{
					Intent intent = new Intent(PersionDetailActivity.this, PrivateletterChatActivity.class);
					dao.updateImSeeState(toUser);
					intent.putExtra("toUser", toUser);
					intent.putExtra("toName", toName);
					startActivity(intent);
				}

			}
		});

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});


		tvzhaohu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (HelperUtil.flagISNoNull(toUser) && !toUser.equals(dao.queryUserByNewTime().getUsername())) {
					Intent intent = new Intent(ReceiverConstant.MESSAGE_ZHAOHU_ACTION);
					intent.putExtra("toID", toUser);
					intent.putExtra("nickname", dao.queryUserByNewTime().getNickname());
					sendBroadcast(intent);
					HelperUtil.totastShow("打招呼成功!", getApplicationContext());
				}

			}
		});
		HelperUtil.customDialogShow(mdialog, PersionDetailActivity.this, "请稍候...");
		initThread = new InitThread();
		initThread.start();
		visitInsert();


	}

	private void checkMemberState() {
		HelperUtil.customDialogShow(mdialog, PersionDetailActivity.this, "请稍候...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stu
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					map.put("touser", toUser);
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.CheckONMember, map));
					handler.sendEmptyMessage(0);
					if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_NUMBER_OUT) {
						Intent intent = new Intent(PersionDetailActivity.this, OrderDailog.class);
						intent.putExtra("toUser", toUser);
						intent.putExtra("toName", toName);
						startActivity(intent);
					} else if (jsonObject.optInt("result") == BBLConstant.MEMBER_STATE_OUT) {
						handler.sendEmptyMessage(4);
					} else {
						Intent intent = new Intent(PersionDetailActivity.this, PrivateletterChatActivity.class);
						dao.updateImSeeState(toUser);
						intent.putExtra("toUser", toUser);
						intent.putExtra("toName", toName);
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
				case 0:
					if (mdialog != null)
						mdialog.dismiss();
					break;
				case 1:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("请检查网络是否可用或稍候重试", getApplicationContext());
					break;
				case 3:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("暗恋失败", getApplicationContext());
					break;
				case 4:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("会员已过期,请重新购买！", getApplicationContext());
					startActivity(new Intent(PersionDetailActivity.this, OrderDailog.class));
					break;
				case 5:

					break;
				case 6:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("已经暗恋过了!", getApplicationContext());
					break;
				case 7:
					if (mdialog != null)
						mdialog.dismiss();
					HelperUtil.totastShow("暗恋成功!", getApplicationContext());
					if (HelperUtil.flagISNoNull(toUser) && !toUser.equals(dao.queryUserByNewTime().getUsername())) {
						Intent intent = new Intent(ReceiverConstant.MESSAGE_ANLIAN_ACTION);
						intent.putExtra("toID", toUser);
						intent.putExtra("nickname", dao.queryUserByNewTime().getNickname());
						sendBroadcast(intent);
					}
					break;
				default:
					break;
			}
		}
	};

	private void checkAnlian() {
		HelperUtil.customDialogShow(mdialog, PersionDetailActivity.this, "请稍候...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					map.put("touser", toUser);
					JSONObject jsonObject = new JSONObject(HelperUtil.postRequest(HttpConstantUtil.CheckAnLian, map));
					handler.sendEmptyMessage(0);
					if (jsonObject.optInt("result") == BBLConstant.ANLIAN_STATE_YES) {
						handler.sendEmptyMessage(7);
					} else {
						handler.sendEmptyMessage(6);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}
		}).start();

	}

	private void visitInsert() {

		String username = dao.queryUserByNewTime().getUsername();
		if (!username.equals(toUser)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put("username", dao.queryUserByNewTime().getUsername());
						map.put("touser", toUser);
						HelperUtil.postRequest(HttpConstantUtil.UpdateVisiter, map);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}).start();
		}
	}

	private class InitThread extends Thread {
		@Override
		public synchronized void run() {
			try {

				Map<String, String> map = new HashMap<String, String>();
				map.put("username", toUser);
				final JSONObject jsonObject = new JSONObject(
						HelperUtil.postRequest(HttpConstantUtil.FindPersonDetail, map));
				PersionDetailActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mdialog != null) {
							mdialog.dismiss();
						}
						if (jsonObject.length() > 0) {

							// TODO Auto-generated method stub

							nickname_text.setText(jsonObject.optString("id"));
							tv_level.setText(jsonObject.optString("level") + "级");
							province_TextView.setText(jsonObject.optString("lives"));
							year_TextView.setText(jsonObject.optString("birthday"));
							height_TextView.setText(jsonObject.optString("height"));
							weight_TextView.setText(jsonObject.optString("weight"));
							house_TextView.setText(jsonObject.optString("house"));
							job_TextView.setText(jsonObject.optString("job"));
							blood_TextView.setText(jsonObject.optString("blood"));
							iswantchild_TextView.setText(jsonObject.optString("child"));
							fathermomlive_TextView.setText(jsonObject.optString("fathermonlive"));
							marraypresex_TextView.setText(jsonObject.optString("premaritalsex"));
							likeothersex_TextView.setText(jsonObject.optString("likeoppositesex"));
							placeotherlove_TextView.setText(jsonObject.optString("placeofother"));
							education_TextView.setText(jsonObject.optString("education"));
							marraystate_TextView.setText(jsonObject.optString("maritalstatus"));
							monthly_TextView.setText(jsonObject.optString("monthly"));
							juzhu.setText(jsonObject.optString("lonelylives"));
							shengao.setText(jsonObject.optString("lonelyheight"));
							xueli.setText(jsonObject.optString("lonelyeducation"));
							shouru.setText(jsonObject.optString("lonelymonthly"));
							year.setText(jsonObject.optString("lonelyyear"));
							weight.setText(jsonObject.optString("lonelyweight"));
							int duibaistate = jsonObject.optInt("heartduibaistate");
							if (duibaistate == 0) {
								dubai.setText(jsonObject.optString("heartdubai"));
							} else {
								dubai.setText("");
							}
							tv_sex.setText(jsonObject.optString("sex"));
							String nickname = jsonObject.optString("nickname");
							String photo = jsonObject.optString("photo");
							tv_title.setText(nickname);
							dao.updatePhoto(toUser, photo, nickname, jsonObject.optString("sex"));
							// try
							// {
							// Instant.init(PersionDetailActivity.this, 0);
							// }
							// catch(Exception e)
							// {
							// e.printStackTrace();
							// }
						}
					}
				});

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				PersionDetailActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mdialog != null) {
							mdialog.dismiss();
						}
						HelperUtil.totastShow(PublicConstant.ToastCatch, PersionDetailActivity.this);
					}
				});
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (initThread != null) {
			initThread.interrupt();
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
