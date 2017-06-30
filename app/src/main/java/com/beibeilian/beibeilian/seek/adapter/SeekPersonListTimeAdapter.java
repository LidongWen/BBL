package com.beibeilian.beibeilian.seek.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.circle.widgets.CircularImage;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.predestined.PersionDetailActivity;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.DateFormatUtil;
import com.beibeilian.beibeilian.util.HelperUtil;

import java.util.List;

public class SeekPersonListTimeAdapter extends BaseAdapter {

	private Context context;
	private List<UserInfo> userInfo;

	public SeekPersonListTimeAdapter(Context context, List<UserInfo> userInfo) {
		this.context = context;
		this.userInfo = userInfo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.seek_personlist_item_time, null);
			viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.nicknameid);
			viewHolder.tv_year = (TextView) convertView.findViewById(R.id.yearid);
			// viewHolder.tv_distance = (TextView) convertView
			// .findViewById(R.id.distanceid);
			// viewHolder.tv_state = (TextView) convertView
			// .findViewById(R.id.stateid);
			viewHolder.tv_monologue = (TextView) convertView.findViewById(R.id.monologueid);
			viewHolder.tv_place = (TextView) convertView.findViewById(R.id.placeid);
			viewHolder.tv_photo = (CircularImage) convertView.findViewById(R.id.touxiangid);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.logintime_id);
			convertView.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) convertView.getTag();
		}
		final UserInfo model = (UserInfo) getItem(position);
		try {
			if (model != null) {

				String url = model.getPhoto();
				if(HelperUtil.flagISNoNull(url))
				{
					if (url.contains("http")) {
						url = url.substring(url.lastIndexOf("/") + 1);
					}
					url = BBLConstant.PHOTO_BEFORE_URL + url;
					model.setPhoto(url);
				}
				else
				{
					model.setPhoto("");
				}

				viewHolder.tv_nickname.setText(model.getNickname());

				viewHolder.tv_place.setText(model.getLives());
				viewHolder.tv_year.setText(model.getBirthday());

//				if(model.getHeartduibaistate()==0)
//				{
//					viewHolder.tv_monologue.setText(model.getHeartdubai());
//				}
//				else
//				{
//					viewHolder.tv_monologue.setText("");
//				}
				String duibai=model.getHeartdubai();
				if(HelperUtil.flagISNoNull(duibai))
				{
					if(!duibai.equals("δͨڽʳۋ"))
					{
						viewHolder.tv_monologue.setText(duibai);
					}
					else
					{
						viewHolder.tv_monologue.setText("");
					}
				}
				else
				{
					viewHolder.tv_monologue.setText("");
				}
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(context, PersionDetailActivity.class);
						intent.putExtra("toUser", model.getUsername());
						intent.putExtra("toName", model.getNickname());
						intent.putExtra("toPhoto", model.getPhoto() != null ? model.getPhoto() : "");
						context.startActivity(intent);
					}
				});
			}

			if (HelperUtil.flagISNoNull(model.getSex()) && model.getSex().equals("Ő")) {
				viewHolder.tv_nickname.setTextColor(context.getResources().getColor(R.color.nan));
			} else {
				viewHolder.tv_nickname.setTextColor(context.getResources().getColor(R.color.nv));
			}

			if (HelperUtil.flagISNoNull(model.getTime())) {
				viewHolder.tv_time.setText(DateFormatUtil.getDDTime(model.getTime()));
			}

			if (model != null && HelperUtil.flagISNoNull(model.getPhoto())) {
				HelperUtil.getPicassoImageByUrl(context, model.getPhoto(), context.getResources().getDrawable(R.drawable.icon_touxiang), viewHolder.tv_photo);

//				if (model.getSex().equals("Ů")) {
////					new AsynImageLoaderAccount().showImageAsyn(viewHolder.tv_photo,
////							model.getPhoto(), R.drawable.nv,model.getUsername());
//					HelperUtil.getPicassoImageByUrl(context, model.getPhoto(), context.getResources().getDrawable(R.drawable.nv), viewHolder.tv_photo);
//				} else {
////					new AsynImageLoaderAccount().showImageAsyn(viewHolder.tv_photo,
////							model.getPhoto(), R.drawable.nan,model.getUsername());
//					HelperUtil.getPicassoImageByUrl(context, model.getPhoto(), context.getResources().getDrawable(R.drawable.nan), viewHolder.tv_photo);
//
//				}
			} else {
				viewHolder.tv_photo.setImageResource(R.drawable.icon_touxiang);

//				if (model.getSex().equals("Ů")) {
//					viewHolder.tv_photo.setImageResource(R.drawable.nv);
//				} else {
//					viewHolder.tv_photo.setImageResource(R.drawable.nan);
//				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView tv_nickname;
		TextView tv_year;
		// TextView tv_distance;
		// TextView tv_state;
		TextView tv_monologue;
		TextView tv_place;
		CircularImage tv_photo;
		TextView tv_time;

	}
}
