package com.beibeilian.beibeilian.privateletter.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.circle.widgets.CircularImage;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.privateletter.LoadNickNameAndPhotoTask;
import com.beibeilian.beibeilian.privateletter.model.MessageList;
import com.beibeilian.beibeilian.seek.model.UserInfo;
import com.beibeilian.beibeilian.util.DateFormatUtil;
import com.beibeilian.beibeilian.util.HelperUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PrivateletterAdapter extends BaseAdapter {

	private List<MessageList> listMessage;

	private Context context;

	private BBLDao dao;

	private String username;

	private String sex;

	public PrivateletterAdapter(Context context, List<MessageList> listMessage, String username) {
		this.username = username;
		this.context = context;
		this.listMessage = listMessage;
		dao = new BBLDao(context, null, null, 1);
		sex = dao.queryUserByNewTime().getSex();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMessage.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listMessage.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.privateletter_listview_item, null);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.letter_item_time);
			viewHolder.img_head = (CircularImage) convertView.findViewById(R.id.letter_item_icon);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.letter_item_msg);
			viewHolder.tv_see_state = (TextView) convertView.findViewById(R.id.letter_item_unreadmsg);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.letter_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		try {
			MessageList messageList = (MessageList) getItem(position);
			if (messageList != null) {
				if (HelperUtil.flagISNoNull(messageList.getFromtime()))
					viewHolder.tv_time.setText(DateFormatUtil.getDDTime(messageList.getFromtime()));
				if (messageList.getFromuser().equals(username)) {
					viewHolder.tv_content.setText("给您发来了新的消息");
					int count = dao.queryCountByUser(messageList.getOutuser());
					if (count != 0) {
						viewHolder.tv_see_state.setText(String.valueOf(count));
						viewHolder.tv_see_state.setVisibility(View.VISIBLE);
					} else {
						viewHolder.tv_see_state.setVisibility(View.GONE);
					}
					UserInfo model = new UserInfo();
					model = dao.queryPhoto(messageList.getOutuser());

					if (model != null) {
						if (HelperUtil.flagISNoNull(model.getSex()) && model.getSex().equals("男")) {
							viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.nan));
						} else {
							viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.nv));
						}

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
					} else {
						new LoadNickNameAndPhotoTask(viewHolder.tv_name, viewHolder.img_head, messageList.getOutuser(),
								context, dao).execute(0);
						// if (HelperUtil.flagISNoNull(sex)) {
						// if (sex.equals("男")) {
						// viewHolder.tv_name.setTextColor(context
						// .getResources().getColor(R.color.nv));
						// } else {
						// viewHolder.tv_name.setTextColor(context
						// .getResources().getColor(R.color.nan));
						// }
						// }
					}

					if (model != null && HelperUtil.flagISNoNull(model.getPhoto())) {
						HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
								context.getResources().getDrawable(R.drawable.icon_touxiang), viewHolder.img_head);

//						if (model != null && HelperUtil.flagISNoNull(model.getSex())) {
//							if (model.getSex().equals("女")) {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nv);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//							} else {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nan);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//							}
//							viewHolder.tv_name.setText(model.getNickname());
//						} else {
//							if (sex.equals("女")) {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nan);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//							} else {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nv);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//							}
//							viewHolder.tv_name.setText(messageList.getOutuser());
//
//						}
					} else {
						if (model != null && HelperUtil.flagISNoNull(model.getSex())) {
							viewHolder.img_head.setImageResource(R.drawable.icon_touxiang);
//							if (model.getSex().equals("女"))
//								viewHolder.img_head.setImageResource(R.drawable.nv);
//							else
//								viewHolder.img_head.setImageResource(R.drawable.nan);
							viewHolder.tv_name.setText(model.getNickname());
						} else {
							viewHolder.img_head.setImageResource(R.drawable.icon_touxiang);
//							if (sex.equals("女")) {
//								viewHolder.img_head.setImageResource(R.drawable.nan);
//							} else {
//								viewHolder.img_head.setImageResource(R.drawable.nv);
//							}
							viewHolder.tv_name.setText(messageList.getOutuser());
						}
					}
				} else {

					viewHolder.tv_content.setText(HelperUtil.convertNormalStringToSpannableString(context,
							messageList.getFromcontent(), true));
					int count = dao.queryCountByUser(messageList.getFromuser());
					if (count != 0) {
						viewHolder.tv_see_state.setText(String.valueOf(count));
						viewHolder.tv_see_state.setVisibility(View.VISIBLE);
					} else {
						viewHolder.tv_see_state.setVisibility(View.GONE);
					}
					UserInfo model = new UserInfo();
					model = dao.queryPhoto(messageList.getFromuser());
					if (model != null) {
						if (HelperUtil.flagISNoNull(model.getSex()) && model.getSex().equals("男")) {
							viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.nan));
						} else {
							viewHolder.tv_name.setTextColor(context.getResources().getColor(R.color.nv));
						}
					} else {
						// if (HelperUtil.flagISNoNull(sex)) {
						// if (sex.equals("男")) {
						// viewHolder.tv_name.setTextColor(context
						// .getResources().getColor(R.color.nv));
						// } else {
						// viewHolder.tv_name.setTextColor(context
						// .getResources().getColor(R.color.nan));
						// }
						// }

						new LoadNickNameAndPhotoTask(viewHolder.tv_name, viewHolder.img_head, messageList.getFromuser(),
								context, dao).execute(0);

					}

					if (model != null && HelperUtil.flagISNoNull(model.getPhoto())) {
						HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
								context.getResources().getDrawable(R.drawable.icon_touxiang), viewHolder.img_head);
						if (model != null && HelperUtil.flagISNoNull(model.getSex())) {
//							if (model.getSex().equals("女")) {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nv);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//							} else {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nan);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//							}
							viewHolder.tv_name.setText(model.getNickname());

						} else {
//							if (sex.equals("女")) {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nan);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//							} else {
//								// new AsynImageLoader().showImageAsyn(
//								// viewHolder.img_head, model.getPhoto(),
//								// R.drawable.nv);
//								HelperUtil.getPicassoImageByUrl(context, model.getPhoto(),
//										context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//							}

							viewHolder.tv_name.setText(messageList.getFromuser());

						}
					} else {
						viewHolder.img_head.setImageResource(R.drawable.icon_touxiang);

						if (model != null && HelperUtil.flagISNoNull(model.getSex())) {
//							if (model.getSex().equals("女"))
//								viewHolder.img_head.setImageResource(R.drawable.nv);
//							else
//								viewHolder.img_head.setImageResource(R.drawable.nan);
							viewHolder.tv_name.setText(model.getNickname());

						} else {
//							if (sex.equals("女")) {
//								viewHolder.img_head.setImageResource(R.drawable.nan);
//							} else {
//								viewHolder.img_head.setImageResource(R.drawable.nv);
//							}
							viewHolder.tv_name.setText(messageList.getFromuser());

						}
					}
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return convertView;
	}

	private SpannableStringBuilder handler(final TextView gifTextView, String content) {

		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			try {
				String tempText = m.group();
				String png = tempText.substring("#[".length(), tempText.length() - "]#".length());
				sb.setSpan(new ImageSpan(context, BitmapFactory.decodeStream(context.getAssets().open(png))), m.start(),
						m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {
				System.out.println("报错" + e.toString());
			}
		}
		return sb;
	}

	static class ViewHolder {

		TextView tv_time;

		CircularImage img_head;

		TextView tv_content;

		TextView tv_see_state;

		TextView tv_name;
	}

}
