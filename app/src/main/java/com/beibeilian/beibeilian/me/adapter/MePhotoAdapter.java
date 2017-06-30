package com.beibeilian.beibeilian.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.me.model.Photo;
import com.beibeilian.beibeilian.util.DateFormatUtil;
import com.beibeilian.beibeilian.util.HelperUtil;

import java.util.List;

public class MePhotoAdapter extends BaseAdapter{
    private List<Photo> list;
    private Context context;
	private String sex;
	
	public MePhotoAdapter(Context context,List<Photo> list,String sex)
	{
		this.list=list;
		this.context=context;
		this.sex=sex;
		
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
		// TODO Auto-generated method stubtt
		ViewHolder viewHolder;
		if(convertView==null)
		{
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.me_photo_item,null);
			viewHolder.img_image=(ImageView)convertView.findViewById(R.id.me_photo_img);
			viewHolder.tv_time=(TextView)convertView.findViewById(R.id.time);
		    convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		if(position<=getCount())
		{
			Photo photo=(Photo) getItem(position);
			if(photo!=null)
			{
				 String url=photo.getUrl();
				 if(url.contains("http"))
			     {
					 url=url.substring(url.lastIndexOf("/")+1);
			     }
				 url= BBLConstant.PHOTO_BEFORE_URL+url;
				viewHolder.tv_time.setText(DateFormatUtil.getDDTime(photo.getTime()));
				try {
					HelperUtil.getPicassoImageByUrl(context, url, context.getResources().getDrawable(R.drawable.icon_touxiang), viewHolder.img_image);

//					if(sex.equals("��"))
//					{
////						new AsynImageLoader().showImageAsyn(viewHolder.img_image, url, R.drawable.nan);
//						HelperUtil.getPicassoImageByUrl(context, url, context.getResources().getDrawable(R.drawable.nan), viewHolder.img_image);
//
//					}
//					else
//					{
////						new AsynImageLoader().showImageAsyn(viewHolder.img_image, url, R.drawable.nv);	
//						HelperUtil.getPicassoImageByUrl(context, url, context.getResources().getDrawable(R.drawable.nv), viewHolder.img_image);
//
//					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
//				ImageLoader.getInstance().displayImage(photo.getUrl(),viewHolder.img_image, options, animateFirstListener);
			}
		}
		
		
		return convertView;
	}
//	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}
	static class ViewHolder 
	{
		ImageView img_image;
		TextView tv_time;
	} 
}
