package com.beibeilian.beibeilian.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.beibeilian.beibeilian.MainActivity;
import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.dialog.MeDataDialogActivity;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MeLonelyconditionActivity extends Activity {
	private TextView juzhu;
	private TextView year;
	private TextView shengao;
	private TextView xueli;
	private TextView shouru;
	private TextView weight;

	private String juzhuString;
	private String yearString;
	private String shengaoString;
	private String xueliString;
	private String shouruString;
	private String weightString;

	private Button savebtn;

	private BBLDao dao;

	private Intent intent;

	private IntentFilter intentFilter;

	private DialogPassValueReceiver dialogPassValueReceiver;

	private Dialog dialog;

	private Button btnBack;

	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_lonelycondition);
		dialog = new Dialog(MeLonelyconditionActivity.this,
				R.style.theme_dialog_alert);
		intentFilter = new IntentFilter();
		dialogPassValueReceiver = new DialogPassValueReceiver();
		intentFilter.addAction(ReceiverConstant.MeDataDialogPassValue_ACTION);
		registerReceiver(dialogPassValueReceiver, intentFilter);
		dao = new BBLDao(MeLonelyconditionActivity.this, null, null, 1);
		username=dao.queryUserByNewTime().getUsername();
		juzhu = (TextView) findViewById(R.id.juzhu_content_id);
		year = (TextView) findViewById(R.id.year_content_id);
		shengao = (TextView) findViewById(R.id.shengao_left_id);
		xueli = (TextView) findViewById(R.id.xueli_content_id);
		shouru = (TextView) findViewById(R.id.shouru_content_id);
		weight = (TextView) findViewById(R.id.weight_content_id);
		savebtn = (Button) findViewById(R.id.save_btnid);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MeLonelyconditionActivity.this,
						MainActivity.class));
				finish();

			}
		});

		juzhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeLonelyconditionActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogProvinceTag);
				startActivity(intent);
			}
		});

		year.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeLonelyconditionActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogConditionYearTag);
				startActivity(intent);
			}
		});

		shengao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeLonelyconditionActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogHeightTag);
				startActivity(intent);
			}
		});
		shouru.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeLonelyconditionActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogMonthlyTag);
				startActivity(intent);
			}
		});
		xueli.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeLonelyconditionActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogEducationTag);
				startActivity(intent);
			}
		});
		weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeLonelyconditionActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogWeightTag);
				startActivity(intent);
			}
		});

		savebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				juzhuString = juzhu.getText().toString();
				yearString = year.getText().toString();
				shengaoString = shengao.getText().toString();
				xueliString = xueli.getText().toString();
				shouruString = shouru.getText().toString();
				weightString = weight.getText().toString();
				save(juzhuString, shengaoString, xueliString, shouruString,
						yearString, weightString);
			}
		});

		init();

	}

	private class DialogPassValueReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					ReceiverConstant.MeDataDialogPassValue_ACTION)) {
				String tagValue = intent.getStringExtra("TagValue");
				String tag = intent.getStringExtra("Tag");
				if (tag.equals(PublicConstant.MeDataDialogEducationTag)) {
					xueli.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogHeightTag)) {
					shengao.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogMonthlyTag)) {
					shouru.setText(tagValue);
				}

				if (tag.equals(PublicConstant.MeDataDialogProvinceTag)) {
					juzhu.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogWeightTag)) {
					weight.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogConditionYearTag)) {
					year.setText(tagValue);
				}
			}
		}

	}

	private void save(final String lives, final String height,
					  final String xueli, final String shouru, final String year,
					  final String weight) {
		HelperUtil.customDialogShow(dialog, MeLonelyconditionActivity.this,
				"正在保存中...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					map.put("lonelylive", lives);
					map.put("lonelyheight", height);
					map.put("lonelyeducation", xueli);
					map.put("lonelymonthly", shouru);
					map.put("lonelyyear", year);
					map.put("lonelyweight", weight);
					JSONObject jsonObject = new JSONObject(HelperUtil
							.postRequest(HttpConstantUtil.UplonelyInfo, map));

					MeLonelyconditionActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (dialog != null) {
										dialog.dismiss();
									}
								}
							});
					if (jsonObject.length() > 0) {
						int res = jsonObject.getInt("result");
						if (res > 0) {
							MeLonelyconditionActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											HelperUtil
													.totastShow(
															"保存成功",
															MeLonelyconditionActivity.this);
										}
									});
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					MeLonelyconditionActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (dialog != null) {
										dialog.dismiss();
									}
									HelperUtil.totastShow("请检查网络或稍候再试",
											MeLonelyconditionActivity.this);
								}
							});
				}

			}
		}).start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		return false;
	}

	private void init() {

		HelperUtil.customDialogShow(dialog, MeLonelyconditionActivity.this,
				"正在加载中...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username",username);
					final JSONArray jsonArray = new JSONArray(HelperUtil
							.postRequest(HttpConstantUtil.LonelyconditionInfo,
									map));
					MeLonelyconditionActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (dialog != null) {
										dialog.dismiss();
									}
								}
							});
					if (jsonArray.length() > 0) {

						MeLonelyconditionActivity.this
								.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										try {
											juzhu.setText(jsonArray
													.optJSONObject(0)
													.getString("lonelylives"));
											shengao.setText(jsonArray
													.optJSONObject(0)
													.getString("lonelyheight"));
											xueli.setText(jsonArray
													.optJSONObject(0)
													.getString(
															"lonelyeducation"));
											shouru.setText(jsonArray
													.optJSONObject(0)
													.getString("lonelymonthly"));
											year.setText(jsonArray
													.optJSONObject(0)
													.getString("lonelyyear"));
											weight.setText(jsonArray
													.optJSONObject(0)
													.getString("lonelyweight"));
										} catch (Exception e) {
										}
									}
								});

					}
				} catch (Exception e) {
					// TODO: handle exception
					MeLonelyconditionActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (dialog != null) {
										dialog.dismiss();
									}
									HelperUtil.totastShow("请检查网络或稍候再试",
											MeLonelyconditionActivity.this);
								}
							});

				}
			}
		}).start();
	}

}
