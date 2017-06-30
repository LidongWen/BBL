package com.beibeilian.beibeilian.me.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.me.adapter.MeDataDialogAdapter;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.PublicConstant;

import java.util.ArrayList;
import java.util.List;

public class MeDataDialogActivity extends Activity {

	private List<String> list_province = new ArrayList<String>();
	private List<String> list_city = new ArrayList<String>();
	private List<String> list_year = new ArrayList<String>();
	private List<String> list_conditionyear = new ArrayList<String>();
	private List<String> list_height = new ArrayList<String>();
	private List<String> list_weight = new ArrayList<String>();
	private List<String> list_blood = new ArrayList<String>();
	private List<String> list_education = new ArrayList<String>();
	private List<String> list_job = new ArrayList<String>();
	private List<String> list_monthly = new ArrayList<String>();
	private List<String> list_house = new ArrayList<String>();
	private List<String> list_likeoppositesex = new ArrayList<String>();
	private List<String> list_marriagestatus = new ArrayList<String>();
	private List<String> list_placeotherlove = new ArrayList<String>();
	private List<String> list_iswantchild = new ArrayList<String>();
	private List<String> list_sex = new ArrayList<String>();
	private MeDataDialogAdapter meDataDialogAdapter;

	private ListView listView;

	private String Tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_data_dialog);
		init();
	}

	private void init() {
		listView = (ListView) findViewById(R.id.me_data_listview);
		Tag = getIntent().getStringExtra("Tag");
		CharSequence[] items_pro = this.getResources().getStringArray(R.array.province);
		CharSequence[] items_blood = this.getResources().getStringArray(R.array.blood);
		CharSequence[] items_education = this.getResources().getStringArray(R.array.education);
		CharSequence[] items_havehouse = this.getResources().getStringArray(R.array.havehouse);
		CharSequence[] items_job = this.getResources().getStringArray(R.array.job);
		CharSequence[] items_income = this.getResources().getStringArray(R.array.income);
		CharSequence[] items_likeoppositesex = this.getResources().getStringArray(R.array.likeoppositesex);
		CharSequence[] items_marriagestatus = this.getResources().getStringArray(R.array.marriagestatus);
		CharSequence[] items_placeotherlove = this.getResources().getStringArray(R.array.placeofother);
		CharSequence[] items_iswantchild = this.getResources().getStringArray(R.array.wantchild);
		CharSequence[] items_height = this.getResources().getStringArray(R.array.height);
		CharSequence[] items_weight = this.getResources().getStringArray(R.array.weight);
		CharSequence[] items_year = this.getResources().getStringArray(R.array.year);

		for (int i = 0; i < items_pro.length; i++) {
			list_province.add(items_pro[i].toString());
		}
		for (int i = 0; i < items_blood.length; i++) {
			list_blood.add(items_blood[i].toString());
		}
		for (int i = 0; i < items_education.length; i++) {
			list_education.add(items_education[i].toString());
		}
		for (int i = 0; i < items_havehouse.length; i++) {
			list_house.add(items_havehouse[i].toString());
		}
		for (int i = 0; i < items_havehouse.length; i++) {
			list_house.add(items_havehouse[i].toString());
		}
		for (int i = 0; i < items_job.length; i++) {
			list_job.add(items_job[i].toString());
		}
		for (int i = 0; i < items_income.length; i++) {
			list_monthly.add(items_income[i].toString());
		}
		for (int i = 0; i < items_likeoppositesex.length; i++) {
			list_likeoppositesex.add(items_likeoppositesex[i].toString());
		}
		for (int i = 0; i < items_marriagestatus.length; i++) {
			list_marriagestatus.add(items_marriagestatus[i].toString());
		}
		for (int i = 0; i < items_placeotherlove.length; i++) {
			list_placeotherlove.add(items_placeotherlove[i].toString());
		}
		for (int i = 0; i < items_iswantchild.length; i++) {
			list_iswantchild.add(items_iswantchild[i].toString());
		}

		for (int i = 0; i < items_year.length; i++) {
			list_conditionyear.add(items_year[i].toString());
		}
		for (int i = 16; i <= 80; i++) {
			list_year.add(String.valueOf(i));
		}
		for (int i = 0; i < items_height.length; i++) {
			list_height.add(items_height[i].toString());
		}
		for (int i = 0; i < items_weight.length; i++) {
			list_weight.add(items_weight[i].toString());
		}
		list_sex.add("��");
		list_sex.add("Ů");
		if (Tag.equals(PublicConstant.MeDataDialogProvinceTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_province, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		// if(Tag.equals(PublicConstant.MeDataDialogCityTag))
		// {
		// meDataDialogAdapter=new
		// MeDataDialogAdapter(list_city,MeDataDialogActivity.this);
		// listView.setAdapter(meDataDialogAdapter);
		// }
		if (Tag.equals(PublicConstant.MeDataDialogBloodTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_blood, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}

		if (Tag.equals(PublicConstant.MeDataDialogEducationTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_education, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogHeightTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_height, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogHouseTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_house, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}

		if (Tag.equals(PublicConstant.MeDataDialogIswantchildTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_iswantchild, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}

		if (Tag.equals(PublicConstant.MeDataDialogJobTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_job, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogLikeoppositesexTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_likeoppositesex, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogMarriagestatusTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_marriagestatus, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogMonthlyTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_monthly, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogPlaceotherloveTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_placeotherlove, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogWeightTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_weight, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogYearTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_year, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogConditionYearTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_conditionyear, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogMarriageSexTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_placeotherlove, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}

		if (Tag.equals(PublicConstant.MeDataDialogANDFMOMTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_iswantchild, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		if (Tag.equals(PublicConstant.MeDataDialogSEXTag)) {
			meDataDialogAdapter = new MeDataDialogAdapter(list_sex, MeDataDialogActivity.this);
			listView.setAdapter(meDataDialogAdapter);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ReceiverConstant.MeDataDialogPassValue_ACTION);
				if (Tag.equals(PublicConstant.MeDataDialogProvinceTag)) {
					intent.putExtra("TagValue", list_province.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogBloodTag)) {
					intent.putExtra("TagValue", list_blood.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogEducationTag)) {
					intent.putExtra("TagValue", list_education.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogHeightTag)) {
					intent.putExtra("TagValue", list_height.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogHouseTag)) {
					intent.putExtra("TagValue", list_house.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogIswantchildTag)) {
					intent.putExtra("TagValue", list_iswantchild.get(position));
				}

				if (Tag.equals(PublicConstant.MeDataDialogJobTag)) {
					intent.putExtra("TagValue", list_job.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogLikeoppositesexTag)) {
					intent.putExtra("TagValue", list_likeoppositesex.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogMarriagestatusTag)) {
					intent.putExtra("TagValue", list_marriagestatus.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogMonthlyTag)) {
					intent.putExtra("TagValue", list_monthly.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogPlaceotherloveTag)) {
					intent.putExtra("TagValue", list_placeotherlove.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogWeightTag)) {
					intent.putExtra("TagValue", list_weight.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogYearTag)) {
					intent.putExtra("TagValue", list_year.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogConditionYearTag)) {
					intent.putExtra("TagValue", list_conditionyear.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogMarriageSexTag)) {
					intent.putExtra("TagValue", list_placeotherlove.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogANDFMOMTag)) {
					intent.putExtra("TagValue", list_iswantchild.get(position));
				}
				if (Tag.equals(PublicConstant.MeDataDialogSEXTag)) {
					intent.putExtra("TagValue", list_sex.get(position));
				}
				intent.putExtra("Tag", Tag);
				sendBroadcast(intent);
				finish();
			}
		});
	}
}
