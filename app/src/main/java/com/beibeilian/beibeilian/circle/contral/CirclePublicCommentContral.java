package com.beibeilian.beibeilian.circle.contral;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.circle.bean.User;
import com.beibeilian.beibeilian.circle.mvp.presenter.CirclePresenter;
import com.beibeilian.beibeilian.circle.mvp.view.ICircleViewUpdate;
import com.beibeilian.beibeilian.circle.utils.CommonUtils;
import com.beibeilian.beibeilian.circle.widgets.AppNoScrollerListView;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.application.BBLApplication;
import com.beibeilian.beibeilian.me.model.UserInfoEntiy;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.seek.qaq.CircleMainActivity;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class CirclePublicCommentContral {
	private static final String TAG = CirclePublicCommentContral.class.getSimpleName();
	private View mEditTextBody;
	private EditText mEditText;
	private View mSendBt;
	private CirclePresenter mCirclePresenter;
	private int mCirclePosition;
	private int mCommentType;
	private User mReplyUser;
	private int mCommentPosition;
	private ListView mListView;
	private Context mContext;
	private String id,touser;
	// SweetAlertDialog dialog;
	BBLDao dao;
	/**
	 * 选择动态条目的高
	 */
	private int mSelectCircleItemH;
	/**
	 * 选择的commentItem距选择的CircleItem底部的距离
	 */
	private int mSelectCommentItemBottom;

	public ListView getmListView() {
		return mListView;
	}

	public void setmListView(ListView mListView) {
		this.mListView = mListView;
	}
    String username,nickname;
	public CirclePublicCommentContral(final Context context, View editTextBody, final EditText editText, View sendBt,
			final BBLDao dao) {
		mContext = context;
		mEditTextBody = editTextBody;
		mEditText = editText;
		mSendBt = sendBt;

		this.dao = dao;
		UserInfoEntiy model=dao.queryUserByNewTime();
		username=model.getUsername();
		nickname=model.getNickname();
		// dialog = new SweetAlertDialog(context);
		mSendBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				editTextBodyVisible(View.GONE);
				final String content = editText.getText().toString().trim();
				if (content.length() == 0) {
					HelperUtil.totastShow("内容不能为空", context);
					return;
				}
				// dialog.setTitleText("请稍候...");
				// dialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("username",username);
							map.put("content", content);
							map.put("id", id);
							final JSONObject jsonObject = new JSONObject(
									HelperUtil.postRequest(HttpConstantUtil.AddComplanitCommit, map));
							handler.sendEmptyMessage(0);
							if (jsonObject.length() > 0) {
								if (jsonObject.getInt("result") > 0) {
									Looper.prepare();
									new Handler().post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											HelperUtil.totastShow("发送成功", mContext);
											if (mCirclePresenter != null) {
												// 发布评论
												try {
													mCirclePresenter.addComment(mCirclePosition, mCommentType,
															mReplyUser, String.valueOf(jsonObject.getInt("result")));
													Intent intent = new Intent(ReceiverConstant.MESSAGE_COMMIT_ACTION);
													intent.putExtra("toID", touser);
													intent.putExtra("nickname", nickname);
													mContext.sendBroadcast(intent);
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
										}
									});
									Looper.loop();
								} else {
									handler.sendEmptyMessage(0);
								}
							}

						} catch (Exception e) {
							// TODO: handle exception
							handler.sendEmptyMessage(-1);
						}
					}
				}).start();
				//

			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// if (dialog != null)
				// dialog.dismiss();

				break;
			case 1:
				// if (dialog != null)
				// dialog.dismiss();

				// if (HelperUtil.flagISNoNull(mReplyUser)) {
				// Intent intent = new
				// Intent(ReceiverConstant.MESSAGE_COMMIT_ACTION);
				// intent.putExtra("toID", mReplyUser);
				// intent.putExtra("nickname",
				// dao.queryUserByNewTime().getNickname());
				// mContext.sendBroadcast(intent);
				// }
				break;
			case -1:
				// if (dialog != null)
				// dialog.dismiss();
				HelperUtil.totastShow(PublicConstant.ToastCatch, mContext);
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 
	 * @Title: editTextBodyVisible @Description:
	 *         评论时显示发布布局，评论完隐藏，根据不同位置调节listview的滑动 @param visibility @param
	 *         mCirclePresenter @param mCirclePosition @param commentType 0:发布评论
	 *         1：回复评论 @param replyUser @return void 返回类型 @throws
	 */
	public void editTextBodyVisible(int visibility, CirclePresenter mCirclePresenter, int mCirclePosition,
			int commentType, User replyUser, int commentPosition, String id,String touser) {
		this.mCirclePosition = mCirclePosition;
		this.mCirclePresenter = mCirclePresenter;
		this.mCommentType = commentType;
		this.mReplyUser = replyUser;
		this.mCommentPosition = commentPosition;
		this.id = id;
		this.touser=touser;
		editTextBodyVisible(visibility);

		measure(mCirclePosition, commentType);
	}

	private void measure(int mCirclePosition, int commentType) {
		if (mListView != null) {
			int firstPosition = mListView.getFirstVisiblePosition();
			View selectCircleItem = mListView.getChildAt(mCirclePosition - firstPosition);
			mSelectCircleItemH = selectCircleItem.getHeight();

			if (commentType == ICircleViewUpdate.TYPE_REPLY_COMMENT) {// 回复评论的情况
				AppNoScrollerListView commentLv = (AppNoScrollerListView) selectCircleItem
						.findViewById(R.id.commentList);
				if (commentLv != null) {
					int firstCommentPosition = commentLv.getFirstVisiblePosition();
					// 找到要回复的评论view,计算出该view距离所属动态底部的距离
					View selectCommentItem = commentLv.getChildAt(mCommentPosition - firstCommentPosition);
					if (selectCommentItem != null) {
						mSelectCommentItemBottom = 0;
						View parentView = selectCommentItem;
						do {
							int subItemBottom = parentView.getBottom();
							parentView = (View) parentView.getParent();
							if (parentView != null) {
								mSelectCommentItemBottom += (parentView.getHeight() - subItemBottom);
							}
						} while (parentView != null && parentView != selectCircleItem);
					}
				}
			}
		}
	}

	public void handleListViewScroll() {
		int keyH = BBLApplication.mKeyBoardH;// 键盘的高度
		int editTextBodyH = ((CircleMainActivity) mContext).getEditTextBodyHeight();// 整个EditTextBody的高度
		int screenlH = ((CircleMainActivity) mContext).getScreenHeight();// 整个应用屏幕的高度
		int listviewOffset = screenlH - mSelectCircleItemH - keyH - editTextBodyH;
		Log.d(TAG, "offset=" + listviewOffset + " &mSelectCircleItemH=" + mSelectCircleItemH + " &keyH=" + keyH
				+ " &editTextBodyH=" + editTextBodyH);
		if (mCommentType == ICircleViewUpdate.TYPE_REPLY_COMMENT) {
			listviewOffset = listviewOffset + mSelectCommentItemBottom;
		}
		if (mListView != null) {
			mListView.setSelectionFromTop(mCirclePosition, listviewOffset);
		}

	}

	public void editTextBodyVisible(int visibility) {
		try {
			if (mEditTextBody != null) {
				mEditTextBody.setVisibility(visibility);
				if (View.VISIBLE == visibility) {
					mEditText.requestFocus();
					// 弹出键盘
					CommonUtils.showSoftInput(mContext, mEditText);

				} else if (View.GONE == visibility) {
					// 隐藏键盘
					CommonUtils.hideSoftInput(mContext, mEditText);
				}
			}
		} catch (Exception e) {

		}
	}

	public String getEditTextString() {
		String result = "";
		if (mEditText != null) {
			result = mEditText.getText().toString();
		}
		return result;
	}

	public void clearEditText() {
		if (mEditText != null) {
			mEditText.setText("");
		}
	}

}
