package com.beibeilian.beibeilian.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;

import java.util.List;

public class MeDataDialogAdapter extends BaseAdapter{

	public List<String> list;
	
	public Context context;
	
	public MeDataDialogAdapter(List<String> list,Context context)
	{
		this.list=list;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return getItem(arg0);
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
		if(convertView==null)
		{
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.me_data_dialog_item,
					null);
			viewHolder.tv_content=(TextView)convertView.findViewById(R.id.me_data_dialog_item_id);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.tv_content.setText(list.get(position).toString());
		return convertView;
	}

	static class ViewHolder{
		TextView tv_content;		
	}
	
	
}
