package com.beibeilian.beibeilian.privateletter.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.circle.widgets.CircularImage;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.model.PayRule;
import com.beibeilian.beibeilian.orderdialog.OrderDailog;
import com.beibeilian.beibeilian.predestined.PersionDetailActivity;
import com.beibeilian.beibeilian.privateletter.model.HomeMessage;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.DateFormatUtil;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.SensitiveWordsUtil;

import java.util.List;

public class PrivateletterChatAdapter extends BaseAdapter {

	private List<HomeMessage> listMessage;

	private Context context;

	private String minephoto;

	private String youphoto;

	private String youuser;

	private String youname;

	private String mysex;

	private String yousex;

	/** 弹出的更多选择框 */
	private PopupWindow popupWindow;

	/** 复制，删除 */
	private TextView copy, delete;

	private LayoutInflater inflater;
	/**
	 * 执行动画的时间
	 */
	protected long mAnimationTime = 150;

	private BBLDao dao;

	private String nickname;

	private int vipState=0;

	private String relation="1";


	public PrivateletterChatAdapter(Context context, List<HomeMessage> listMessage, String minephoto, String youphoto,
									String youuser, String youname, String mysex, String yousex, BBLDao dao, String nickname) {
		this.context = context;
		this.listMessage = listMessage;

		this.youuser = youuser;
		this.youname = youname;
		this.mysex = mysex;
		this.yousex = yousex;
		this.dao = dao;
		inflater = LayoutInflater.from(context);
		initPopWindow();
		this.nickname = nickname;
		String username=dao.queryUserByNewTime().getUsername();
		this.vipState=dao.findVipMember(username);
		PayRule mPayRule=dao.findPayRule();
		if(mPayRule!=null)
		{
			relation=mPayRule.getRelation();
		}
		else
		{
			relation="1";
		}

		if(HelperUtil.flagISNoNull(youphoto))
		{
			if (youphoto.contains("http")) {
				youphoto = youphoto.substring(youphoto.lastIndexOf("/") + 1);
			}
			youphoto = BBLConstant.PHOTO_BEFORE_URL + youphoto;
		}
		if(HelperUtil.flagISNoNull(minephoto))
		{
			if (minephoto.contains("http")) {
				minephoto = minephoto.substring(minephoto.lastIndexOf("/") + 1);
			}
			minephoto = BBLConstant.PHOTO_BEFORE_URL + minephoto;
		}
		this.minephoto = minephoto;
		this.youphoto = youphoto;
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

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		HomeMessage homeMessage = (HomeMessage) getItem(position);

		if (homeMessage.getFrom().equals("OUT")) {
			return 0;
		} else {
			return 1;
		}

	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final HomeMessage homeMessage = (HomeMessage) getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			if (homeMessage.getFrom().equals("OUT")) {
				convertView = LayoutInflater.from(context).inflate(R.layout.row_sent_message, null);
			} else {
				convertView = LayoutInflater.from(context).inflate(R.layout.row_received_message, null);
			}
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.timestamp);
			viewHolder.img_head = (CircularImage) convertView.findViewById(R.id.iv_userhead);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_chatcontent);
			viewHolder.img_state = (ImageView) convertView.findViewById(R.id.msg_status);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (HelperUtil.flagISNoNull(homeMessage.getTime()))
			viewHolder.tv_time.setText(DateFormatUtil.getDDTime(homeMessage.getTime()));

		// new LoadChatMessageTask(context, viewHolder.tv_content,
		// homeMessage.getContent()).execute(null);

		viewHolder.tv_content.setOnLongClickListener(new popAction(convertView, position, homeMessage.getFrom()));

		viewHolder.tv_content
				.setText(HelperUtil.convertNormalStringToSpannableString(context, homeMessage.getContent(), true));

		if (homeMessage.getFrom().equals("OUT")) {
			if (homeMessage.sendstate.equals("0")) // 0代表正在发送中,-1代表发送失败,1代表成功
			{
				if (viewHolder.img_state != null)
					viewHolder.img_state.setVisibility(View.GONE);
			}
			if (homeMessage.sendstate.equals("1")) // 0代表正在发送中,-1代表发送失败,1代表成功
			{
				if (viewHolder.img_state != null)
					viewHolder.img_state.setVisibility(View.GONE);
			}
			if (homeMessage.sendstate.equals("-1")) // 0代表正在发送中,-1代表发送失败,1代表成功
			{
				if (viewHolder.img_state != null)
					viewHolder.img_state.setVisibility(View.VISIBLE);
			}
			try {

				HelperUtil.getPicassoImageByUrl(context, minephoto,
						context.getResources().getDrawable(R.drawable.icon_touxiang), viewHolder.img_head);
//				if (HelperUtil.flagISNoNull(mysex) && mysex.equals("男")) {
//					// new AsynImageLoader().showImageAsyn(
//					// viewHolder.img_head, minephoto, R.drawable.nan);
//					HelperUtil.getPicassoImageByUrl(context, minephoto,
//							context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//				} else {
//					// new AsynImageLoader().showImageAsyn(
//					// viewHolder.img_head, minephoto, R.drawable.nv);
//					HelperUtil.getPicassoImageByUrl(context, minephoto,
//							context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//				}
			} catch (Exception e) {

			}

			viewHolder.img_state.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("温馨提示");
					builder.setMessage("您确定重发吗?").setCancelable(false)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Intent intent = new Intent(ReceiverConstant.MESSAGE_SEND_ACTION);
									intent.putExtra("toID", youuser);
									intent.putExtra("nickname", nickname);
									intent.putExtra("message", homeMessage.getContent());
									context.sendBroadcast(intent);
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					}).show();

				}
			});

		} else {
			if (SensitiveWordsUtil.sensitiveWords(homeMessage.getContent())&&vipState<=0&&relation.equals("1")) {
				viewHolder.tv_content.setText("【温馨提醒:此条信息中可能包含联系方式,VIP会员才能查看.点击开通VIP会员】");
			}
			viewHolder.tv_content.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (SensitiveWordsUtil.sensitiveWords(homeMessage.getContent())&&vipState<=0&&relation.equals("1")) {
						context.startActivity(new Intent(context,OrderDailog.class));
					}
				}
			});

			viewHolder.img_head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, PersionDetailActivity.class);
					intent.putExtra("toUser", youuser);
					intent.putExtra("toName", youname);
					context.startActivity(intent);
				}
			});

			if (HelperUtil.flagISNoNull(youphoto)) {
				try {
					HelperUtil.getPicassoImageByUrl(context, youphoto,
							context.getResources().getDrawable(R.drawable.icon_touxiang	), viewHolder.img_head);
//					if (HelperUtil.flagISNoNull(yousex)) {
//						if (yousex.equals("男")) {
//							// new AsynImageLoader().showImageAsyn(
//							// viewHolder.img_head, youphoto,
//							// R.drawable.nan);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//						} else {
//							// new AsynImageLoader().showImageAsyn(
//							// viewHolder.img_head, youphoto,
//							// R.drawable.nv);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//						}

//					} else {
//						if (HelperUtil.flagISNoNull(mysex) && mysex.equals("男")) {
//							// new AsynImageLoader().showImageAsyn(
//							// viewHolder.img_head, youphoto,
//							// R.drawable.nv);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//						} else {
//							// new AsynImageLoader().showImageAsyn(
//							// viewHolder.img_head, youphoto,
//							// R.drawable.nan);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//						}
//					}
				} catch (Exception e) {
				}
			} else {
				try {
					HelperUtil.getPicassoImageByUrl(context, youphoto,
							context.getResources().getDrawable(R.drawable.icon_touxiang), viewHolder.img_head);
//					if (HelperUtil.flagISNoNull(yousex)) {
//						if (yousex.equals("男")) {
//							// new
//							// AsynImageLoader().showImageAsyn(viewHolder.img_head,
//							// youphoto, R.drawable.nan);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//						} else {
//							// new
//							// AsynImageLoader().showImageAsyn(viewHolder.img_head,
//							// youphoto, R.drawable.nv);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//						}
//					} else {
//						if (HelperUtil.flagISNoNull(mysex) && mysex.equals("男")) {
//							// new AsynImageLoader().showImageAsyn(
//							// viewHolder.img_head, youphoto, R.drawable.nv);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nv), viewHolder.img_head);
//
//						} else {
//							// new AsynImageLoader().showImageAsyn(
//							// viewHolder.img_head, youphoto, R.drawable.nan);
//							HelperUtil.getPicassoImageByUrl(context, youphoto,
//									context.getResources().getDrawable(R.drawable.nan), viewHolder.img_head);
//
//						}
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return convertView;
	}

	static class ViewHolder {

		TextView tv_time;

		CircularImage img_head;

		TextView tv_content;

		ImageView img_state;

		ProgressBar pb_sending;

	}

	/**
	 * 屏蔽listitem的所有事件
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	/**
	 * 初始化弹出的pop
	 */
	private void initPopWindow() {
		View popView = inflater.inflate(R.layout.chat_item_copy_delete_menu, null);
		copy = (TextView) popView.findViewById(R.id.chat_copy_menu);
		delete = (TextView) popView.findViewById(R.id.chat_delete_menu);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		// 设置popwindow出现和消失动画
		// popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
	}

	/**
	 * 显示popWindow
	 */
	public void showPop(View parent, int x, int y, final View view, final int position, final String fromOrTo) {
		// 设置popwindow显示位置
		popupWindow.showAtLocation(parent, 0, x, y);
		// 获取popwindow焦点
		popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		// 为按钮绑定事件
		// 复制
		copy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				// 获取剪贴板管理服务
				ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				// 将文本数据复制到剪贴板
				cm.setText(listMessage.get(position).getContent());
			}
		});
		// 删除
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				// if (fromOrTo.equals("IN")) {
				// // from
				// leftRemoveAnimation(view, position);
				// } else if (fromOrTo.equals("OUT")) {
				// // to
				// rightRemoveAnimation(view, position);
				// }
				dao.delIMOne(listMessage.get(position).getImid());
				listMessage.remove(position);
				notifyDataSetChanged();

			}
		});
		popupWindow.update();
		if (popupWindow.isShowing()) {

		}
	}

	/**
	 * 每个ITEM中more按钮对应的点击动作
	 */
	public class popAction implements OnLongClickListener {
		int position;
		View view;
		String fromOrTo;

		public popAction(View view, int position, String fromOrTo) {
			this.position = position;
			this.view = view;
			this.fromOrTo = fromOrTo;
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			int[] arrayOfInt = new int[2];
			// 获取点击按钮的坐标
			v.getLocationOnScreen(arrayOfInt);
			int x = arrayOfInt[0];
			int y = arrayOfInt[1];
			// System.out.println("x: " + x + " y:" + y + " w: " +
			// v.getMeasuredWidth() + " h: " + v.getMeasuredHeight() );
			showPop(v, x, y, view, position, fromOrTo);
			return true;
		}
	}

	// /**
	// * item删除动画
	// * */
	// private void rightRemoveAnimation(final View view, final int position) {
	// final Animation animation = (Animation) AnimationUtils.loadAnimation(
	// context, R.anim.chatto_remove_anim);
	// animation.setAnimationListener(new AnimationListener() {
	// public void onAnimationStart(Animation animation) {
	// }
	//
	// public void onAnimationRepeat(Animation animation) {
	// }
	//
	// public void onAnimationEnd(Animation animation) {
	// // view.setAlpha(0);
	// performDismiss(view, position);
	// animation.cancel();
	// }
	// });
	//
	// view.startAnimation(animation);
	// }

	// /**
	// * item删除动画
	// * */
	// private void leftRemoveAnimation(final View view, final int position) {
	// final Animation animation = (Animation)
	// AnimationUtils.loadAnimation(context, R.anim.chatfrom_remove_anim);
	// animation.setAnimationListener(new AnimationListener() {
	// public void onAnimationStart(Animation animation) {
	// }
	//
	// public void onAnimationRepeat(Animation animation) {
	// }
	//
	// public void onAnimationEnd(Animation animation) {
	// // view.setAlpha(0);
	// performDismiss(view, position);
	// animation.cancel();
	// }
	// });
	//
	// view.startAnimation(animation);
	// }

	// /**
	// * 在此方法中执行item删除之后，其他的item向上或者向下滚动的动画，并且将position回调到方法onDismiss()中
	// *
	// * @param dismissView
	// * @param dismissPosition
	// */
	// private void performDismiss(final View dismissView,
	// final int dismissPosition) {
	// final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();//
	// 获取item的布局参数
	// final int originalHeight = dismissView.getHeight();// item的高度
	//
	// ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0)
	// .setDuration(mAnimationTime);
	// animator.start();
	//
	// animator.addListener(new AnimatorListenerAdapter() {
	// @Override
	// public void onAnimationEnd(Animator animation) {
	// listMessage.remove(dismissPosition);
	// notifyDataSetChanged();
	// // 这段代码很重要，因为我们并没有将item从ListView中移除，而是将item的高度设置为0
	// // 所以我们在动画执行完毕之后将item设置回来
	// ViewHelper.setAlpha(dismissView, 1f);
	// ViewHelper.setTranslationX(dismissView, 0);
	// ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
	// lp.height = originalHeight;
	// dismissView.setLayoutParams(lp);
	// }
	// });
	//
	// animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator valueAnimator) {
	// // 这段代码的效果是ListView删除某item之后，其他的item向上滑动的效果
	// lp.height = (Integer) valueAnimator.getAnimatedValue();
	// dismissView.setLayoutParams(lp);
	// }
	// });
	//
	// }
	//

}
