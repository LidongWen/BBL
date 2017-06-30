package com.beibeilian.beibeilian.seek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.seek.model.Group;

import java.util.List;

public class SeekGroupAdapter extends BaseAdapter {
	List<Group> list;
	Context mContext;

	public SeekGroupAdapter(List<Group> list, Context mContext) {
		this.list = list;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.seek_group_item, null);
			viewHolder.tvgroup = (TextView) convertView.findViewById(R.id.groupid);
			viewHolder.tvnumber = (TextView) convertView.findViewById(R.id.numberid);
			viewHolder.tvremind = (TextView) convertView.findViewById(R.id.group_remind_id);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Group model = list.get(position);
		if (model != null) {
			viewHolder.tvgroup.setText(model.getName());
			viewHolder.tvnumber.setText(model.getNumber());
			if(model.getState()==1)
			{
				viewHolder.tvremind.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.tvremind.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvgroup;
		TextView tvnumber;
		TextView tvremind;
	}
}
