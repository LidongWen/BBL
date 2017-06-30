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
import android.widget.EditText;
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

public class MeDataActivity extends Activity {

	private EditText nickname_text;
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
	private TextView tv_sex;
	private Button save_btn;

	private Intent intent;

	private String proString;
	private String nicknameString;
	private String yearString;
	private String heightString;
	private String weightString;
	private String bloodString;
	private String educationString;
	private String jobString;
	private String monthlyString;
	private String houseString;
	private String placeotherloveString;
	private String likeothersexString;
	private String marraypresexString;
	private String fathermomliveString;
	private String iswantchildString;
	private String marraystateString;
	private String sexString;
	private BBLDao dao;

	private IntentFilter intentFilter;

	private DialogPassValueReceiver dialogPassValueReceiver;

	private Dialog dialog;

	private Button btnBack;

	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_data);
		dialog = new Dialog(MeDataActivity.this,
				R.style.theme_dialog_alert);
		dao=new BBLDao(MeDataActivity.this, null, null, 1);
		username=dao.queryUserByNewTime().getUsername();
		intentFilter=new IntentFilter();
		dialogPassValueReceiver=new DialogPassValueReceiver();
		intentFilter.addAction(ReceiverConstant.MeDataDialogPassValue_ACTION);
		registerReceiver(dialogPassValueReceiver, intentFilter);
		btnBack=(Button)findViewById(R.id.btnBack);
		marraystate_TextView = (TextView) findViewById(R.id.hunyanzk_content_id);
		province_TextView = (TextView) findViewById(R.id.shengid);
		nickname_text = (EditText) findViewById(R.id.et_nickname_text_id);
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
		tv_sex = (TextView) findViewById(R.id.tv_sex_id);

		save_btn = (Button) findViewById(R.id.save_btnid);

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MeDataActivity.this,MainActivity.class));
				finish();

			}
		});


		province_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogProvinceTag);
				startActivity(intent);
			}
		});
		year_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogYearTag);
				startActivity(intent);
			}
		});

		height_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogHeightTag);
				startActivity(intent);
			}
		});
		weight_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogWeightTag);
				startActivity(intent);
			}
		});
		blood_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogBloodTag);
				startActivity(intent);
			}
		});
		education_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogEducationTag);
				startActivity(intent);
			}
		});
		job_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogJobTag);
				startActivity(intent);
			}
		});
		monthly_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogMonthlyTag);
				startActivity(intent);
			}
		});

		house_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogHouseTag);
				startActivity(intent);
			}
		});
		placeotherlove_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogPlaceotherloveTag);
				startActivity(intent);
			}
		});
		likeothersex_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogLikeoppositesexTag);
				startActivity(intent);
			}
		});
		marraypresex_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogMarriageSexTag);
				startActivity(intent);
			}
		});
		marraystate_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogMarriagestatusTag);
				startActivity(intent);
			}
		});
		fathermomlive_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogANDFMOMTag);
				startActivity(intent);
			}
		});

		iswantchild_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogIswantchildTag);
				startActivity(intent);
			}
		});

		tv_sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MeDataActivity.this,
						MeDataDialogActivity.class);
				intent.putExtra("Tag", PublicConstant.MeDataDialogSEXTag);
				startActivity(intent);
			}
		});



		save_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				nicknameString = nickname_text.getText().toString().trim();
				if(nicknameString.length()==0||nicknameString.length()>5)
				{
					HelperUtil.totastShow("昵称不能为空且不能超过5个字符",MeDataActivity.this);
					return;
				}
				HelperUtil.customDialogShow(dialog, MeDataActivity.this, "正在保存中...");
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {

							proString = province_TextView.getText().toString();

							yearString = year_TextView.getText().toString();
							heightString = height_TextView.getText().toString();
							weightString = weight_TextView.getText().toString();
							bloodString = blood_TextView.getText().toString();
							educationString = education_TextView.getText().toString();
							jobString = job_TextView.getText().toString();
							monthlyString = monthly_TextView.getText().toString();
							houseString = house_TextView.getText().toString();
							placeotherloveString = placeotherlove_TextView.getText().toString();
							likeothersexString = likeothersex_TextView.getText().toString();
							marraypresexString = marraypresex_TextView.getText().toString();
							fathermomliveString = fathermomlive_TextView.getText().toString();
							iswantchildString = iswantchild_TextView.getText().toString();
							marraystateString = marraystate_TextView.getText().toString();
							sexString = tv_sex.getText().toString();

							JSONObject jsonObject = UserSave(username,
									nicknameString, yearString, proString,
									heightString, weightString, houseString,
									jobString, bloodString, iswantchildString,
									fathermomliveString, marraypresexString,
									likeothersexString, placeotherloveString,
									educationString, marraystateString,
									monthlyString,sexString);
							MeDataActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {

											if(dialog!=null)
											{
												dialog.dismiss();
											}
										}
									});
							int res = jsonObject.getInt("result");
							if (res == 1) {
								MeDataActivity.this
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												dao.updateNickname(username, nicknameString);
												HelperUtil.totastShow("保存成功",
														MeDataActivity.this);
											}
										});

							} else {
								MeDataActivity.this
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												HelperUtil.totastShow(
														"保存失败,请检查网络是否异常",
														MeDataActivity.this);
											}
										});

							}
						} catch (Exception e) {
							e.printStackTrace();
							MeDataActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {

											if(dialog!=null)
											{
												dialog.dismiss();
											}
											HelperUtil.totastShow(
													"请检查网络是否异常或稍候再试",
													MeDataActivity.this);
										}
									});
						}
					}
				}).start();

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
				if (tag.equals(PublicConstant.MeDataDialogBloodTag)) {
					blood_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogEducationTag)) {
					education_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogHeightTag)) {
					height_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogHouseTag)) {
					house_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogIswantchildTag)) {
					iswantchild_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogJobTag)) {
					job_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogLikeoppositesexTag)) {
					likeothersex_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogMarriagestatusTag)) {
					marraystate_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogMonthlyTag)) {
					monthly_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogPlaceotherloveTag)) {
					placeotherlove_TextView.setText(tagValue);
				}
				if(tag.equals(PublicConstant.MeDataDialogMarriageSexTag))
				{
					marraypresex_TextView.setText(tagValue);
				}

				if (tag.equals(PublicConstant.MeDataDialogProvinceTag)) {
					province_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogWeightTag)) {
					weight_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogYearTag)) {
					year_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogANDFMOMTag)) {
					fathermomlive_TextView.setText(tagValue);
				}
				if (tag.equals(PublicConstant.MeDataDialogSEXTag)) {
					tv_sex.setText(tagValue);
				}

			}
		}

	}

	private void init() {

		HelperUtil.customDialogShow(dialog, MeDataActivity.this, "正在加载中...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("username",username);
					final JSONArray jsonArray = new JSONArray(HelperUtil.postRequest(
							HttpConstantUtil.FindInfo, map));
					MeDataActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {

									if(dialog!=null)
									{
										dialog.dismiss();
									}
								}
							});
					if (jsonArray.length() > 0) {

						MeDataActivity.this
								.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method
										// stub



										nickname_text.setText(jsonArray.optJSONObject(0)
												.optString("nickname"));
										province_TextView.setText(jsonArray.optJSONObject(0).optString("lives"));
										year_TextView.setText(jsonArray.optJSONObject(0).optString("birthday"));
										height_TextView.setText(jsonArray.optJSONObject(0)
												.optString("height"));
										weight_TextView.setText(jsonArray.optJSONObject(0)
												.optString("weight"));
										house_TextView.setText(jsonArray.optJSONObject(0)
												.optString("house"));
										job_TextView.setText(jsonArray.optJSONObject(0)
												.optString("job"));
										blood_TextView.setText(jsonArray.optJSONObject(0)
												.optString("blood"));
										iswantchild_TextView.setText(jsonArray.optJSONObject(0)
												.optString("child"));
										fathermomlive_TextView.setText(jsonArray.optJSONObject(0).optString("fathermonlive"));
										marraypresex_TextView.setText(jsonArray
												.optJSONObject(0).optString("premaritalsex"));
										likeothersex_TextView.setText(jsonArray
												.optJSONObject(0).optString("likeoppositesex"));
										placeotherlove_TextView.setText(jsonArray
												.optJSONObject(0).optString("placeofother"));
										education_TextView.setText(jsonArray.optJSONObject(0)
												.optString("education"));
										marraystate_TextView.setText(jsonArray.optJSONObject(0)
												.optString("maritalstatus"));
										monthly_TextView.setText(jsonArray.optJSONObject(0)
												.optString("monthly"));
										tv_sex.setText(jsonArray.optJSONObject(0)
												.optString("sex"));
									}
								});
					}
				} catch (Exception e) {
					// TODO: handle exception
					MeDataActivity.this
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {

									if(dialog!=null)
									{
										dialog.dismiss();
									}
									HelperUtil
											.totastShow(
													"请检查网络是否可用或稍候再试",
													MeDataActivity.this);
								}
							});
				}
			}
		}).start();
	}

	private JSONObject UserSave(String username, String nickname,
								String birthday, String lives, String height, String weight,
								String house, String job, String blood, String child,
								String fathermomlive, String premaritalsex, String likeoppositesex,
								String placeofother, String education, String maritalstatus,
								String monthly,String sex) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("nickname", nickname);
		map.put("birthday", birthday);
		map.put("lives", lives);
		map.put("height", height);
		map.put("weight", weight);
		map.put("house", house);
		map.put("job", job);
		map.put("blood", blood);
		map.put("child", child);
		map.put("fathermomlive", fathermomlive);
		map.put("premaritalsex", premaritalsex);
		map.put("likeoppositesex", likeoppositesex);
		map.put("placeofother", placeofother);
		map.put("education", education);
		map.put("maritalstatus", maritalstatus);
		map.put("monthly", monthly);
		map.put("sex", sex);

		return new JSONObject(HelperUtil.postRequest(HttpConstantUtil.UpInfo,
				map));
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(dialogPassValueReceiver!=null)
		{
			unregisterReceiver(dialogPassValueReceiver);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		return false;
	}
}
