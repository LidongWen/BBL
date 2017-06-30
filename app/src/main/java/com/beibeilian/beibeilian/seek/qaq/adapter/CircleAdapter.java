package com.beibeilian.beibeilian.seek.qaq.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.circle.bean.ActionItem;
import com.beibeilian.beibeilian.circle.bean.CircleItem;
import com.beibeilian.beibeilian.circle.bean.CommentItem;
import com.beibeilian.beibeilian.circle.bean.FavortItem;
import com.beibeilian.beibeilian.circle.bean.User;
import com.beibeilian.beibeilian.circle.contral.CirclePublicCommentContral;
import com.beibeilian.beibeilian.circle.mvp.presenter.CirclePresenter;
import com.beibeilian.beibeilian.circle.mvp.view.ICircleViewUpdate;
import com.beibeilian.beibeilian.circle.spannable.ISpanClick;
import com.beibeilian.beibeilian.circle.widgets.AppNoScrollerListView;
import com.beibeilian.beibeilian.circle.widgets.CircularImage;
import com.beibeilian.beibeilian.circle.widgets.FavortListView;
import com.beibeilian.beibeilian.circle.widgets.MultiImageView;
import com.beibeilian.beibeilian.circle.widgets.SnsPopupWindow;
import com.beibeilian.beibeilian.circle.widgets.dialog.CommentDialog;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.model.UserInfoEntiy;
import com.beibeilian.beibeilian.predestined.PersionDetailActivity;
import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.seek.qaq.ImagePagerActivity;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CircleAdapter extends BaseAdapter implements ICircleViewUpdate {
	private static final int ITEM_VIEW_TYPE_DEFAULT = 0;
	private static final int ITEM_VIEW_TYPE_URL = 1;
	private static final int ITEM_VIEW_TYPE_IMAGE = 2;

	private static final String ITEM_TYPE_URL = "1";
	private static final String ITEM_TYPE_IMAGE = "2";
	private static final int ITEM_VIEW_TYPE_COUNT = 3;

	private Context mContext;
	private CirclePresenter mPresenter;
	private CirclePublicCommentContral mCirclePublicCommentContral;
	private List<CircleItem> datas = new ArrayList<CircleItem>();
	private String username,nickname;
	private User curUser;
	public void setmCirclePublicCommentContral(CirclePublicCommentContral mCirclePublicCommentContral) {
		this.mCirclePublicCommentContral = mCirclePublicCommentContral;
	}

	public List<CircleItem> getDatas() {
		return datas;
	}

	public void setDatas(List<CircleItem> datas) {
		if (datas != null) {
			this.datas = datas;
		}
	}

	public CircleAdapter(Context context) {
		mContext = context;
		BBLDao dao = new BBLDao(mContext, null, null, 1);
		mPresenter = new CirclePresenter(this);
		UserInfoEntiy model=dao.queryUserByNewTime();
		username = model.getUsername();
		nickname=model.getNickname();
		curUser=new User(username, nickname, "");
	}

	@Override
	public int getItemViewType(int position) {
		int itemType = ITEM_VIEW_TYPE_DEFAULT;
		CircleItem item = datas.get(position);
		if (ITEM_TYPE_URL.equals(item.getType())) {
			itemType = ITEM_VIEW_TYPE_URL;
		} else if (ITEM_TYPE_IMAGE.equals(item.getType())) {
			itemType = ITEM_VIEW_TYPE_IMAGE;
		} else {
			itemType = ITEM_VIEW_TYPE_DEFAULT;
		}
		return itemType;
	}

	@Override
	public int getViewTypeCount() {
		return ITEM_VIEW_TYPE_COUNT;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int itemViewType = getItemViewType(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_circle_item, null);
			ViewStub linkOrImgViewStub = (ViewStub) convertView.findViewById(R.id.linkOrImgViewStub);
			switch (itemViewType) {
				case ITEM_VIEW_TYPE_URL:// 链接view
					linkOrImgViewStub.setLayoutResource(R.layout.viewstub_urlbody);
					linkOrImgViewStub.inflate();
					LinearLayout urlBodyView = (LinearLayout) convertView.findViewById(R.id.urlBody);
					if (urlBodyView != null) {
						holder.urlBody = urlBodyView;
						holder.urlImageIv = (ImageView) convertView.findViewById(R.id.urlImageIv);
						holder.urlContentTv = (TextView) convertView.findViewById(R.id.urlContentTv);
					}
					break;
				case ITEM_VIEW_TYPE_IMAGE:// 图片view
					linkOrImgViewStub.setLayoutResource(R.layout.viewstub_imgbody);
					linkOrImgViewStub.inflate();
					MultiImageView multiImageView = (MultiImageView) convertView.findViewById(R.id.multiImagView);
					if (multiImageView != null) {
						holder.multiImageView = multiImageView;
					}
					break;
				default:
					break;
			}
			holder.headIv = (CircularImage) convertView.findViewById(R.id.headIv);
			holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
			holder.digLine = convertView.findViewById(R.id.lin_dig);

			holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
			holder.urlTipTv = (TextView) convertView.findViewById(R.id.urlTipTv);
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			holder.deleteBtn = (TextView) convertView.findViewById(R.id.deleteBtn);
			holder.snsBtn = (ImageView) convertView.findViewById(R.id.snsBtn);
			holder.favortListTv = (FavortListView) convertView.findViewById(R.id.favortListTv);

			holder.digCommentBody = (LinearLayout) convertView.findViewById(R.id.digCommentBody);

			holder.commentList = (AppNoScrollerListView) convertView.findViewById(R.id.commentList);

			holder.bbsAdapter = new CommentAdapter(mContext);
			holder.favortListAdapter = new FavortListAdapter();

			holder.favortListTv.setAdapter(holder.favortListAdapter);
			holder.commentList.setAdapter(holder.bbsAdapter);

			holder.snsPopupWindow = new SnsPopupWindow(mContext);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CircleItem circleItem = datas.get(position);
		final String circleId = circleItem.getId();
		final String name = circleItem.getUser().getName();
		String headImg = circleItem.getUser().getHeadUrl();
		String content = circleItem.getContent();
		String createTime = circleItem.getCreateTime();
		final List<FavortItem> favortDatas = circleItem.getFavorters();
		final List<CommentItem> commentsDatas = circleItem.getComments();
		boolean hasFavort = circleItem.hasFavort();
		boolean hasComment = circleItem.hasComment();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		if(HelperUtil.flagISNoNull(headImg))
		{
			if (headImg.contains("http")) {
				headImg = headImg.substring(headImg.lastIndexOf("/") + 1);
			}
			headImg = BBLConstant.PHOTO_BEFORE_URL + headImg;
		}
		imageLoader.displayImage(headImg, holder.headIv);
		holder.nameTv.setText(name);
		holder.timeTv.setText(createTime);
		holder.contentTv.setText(content);

		holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

		if (username.equals(circleItem.getUser().getId())) {
			holder.deleteBtn.setVisibility(View.VISIBLE);
		} else {
			holder.deleteBtn.setVisibility(View.GONE);
		}
		holder.headIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						PersionDetailActivity.class);
				intent.putExtra("toUser",circleItem.getUser().getId());
				intent.putExtra("toName", name);
				intent.putExtra("toPhoto","");
				mContext.startActivity(intent);
			}
		});

		holder.deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", circleId);
							final JSONObject jsonObject = new JSONObject(
									HelperUtil.postRequest(HttpConstantUtil.Delcomplanitbyid, map));
							if (jsonObject.getInt("result") > 0) {
								Looper.prepare();
								new Handler().post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										handler.sendEmptyMessage(1);
										// 删除
										mPresenter.deleteCircle(circleId);

									}
								});
								Looper.loop();
							} else {
								handler.sendEmptyMessage(0);
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							handler.sendEmptyMessage(-1);
						}
					}
				}).start();



			}
		});
		if (hasFavort || hasComment) {
			if (hasFavort) {// 处理点赞列表
				holder.favortListTv.setSpanClickListener(new ISpanClick() {
					@Override
					public void onClick(int position) {
//						String userName = favortDatas.get(position).getUser().getName();
//						String userId = favortDatas.get(position).getUser().getId();
//						Toast.makeText(BBLApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT)
//								.show();
					}
				});
				holder.favortListAdapter.setDatas(favortDatas);
				holder.favortListAdapter.notifyDataSetChanged();
				holder.favortListTv.setVisibility(View.VISIBLE);
			} else {
				holder.favortListTv.setVisibility(View.GONE);
			}
			if (hasComment) {// 处理评论列表
				holder.bbsAdapter.setDatasource(commentsDatas);
				holder.bbsAdapter.setCommentClickListener(new CommentAdapter.ICommentItemClickListener() {
					@Override
					public void onItemClick(int commentPosition) {

						CommentItem commentItem = commentsDatas.get(commentPosition);
						if (username.equals(commentItem.getUser().getId())) {// 复制或者删除自己的评论

							CommentDialog dialog = new CommentDialog(mContext, mPresenter, commentItem, position);
							dialog.show();
						} else {// 回复别人的评论

							if (mCirclePublicCommentContral != null) {
								mCirclePublicCommentContral.editTextBodyVisible(View.VISIBLE, mPresenter, position,
										TYPE_REPLY_COMMENT, commentItem.getUser(), commentPosition, circleId,circleItem.getId());
							}
						}
					}
				});
				holder.bbsAdapter.notifyDataSetChanged();
				holder.commentList.setVisibility(View.VISIBLE);
				holder.commentList.setOnItemClickListener(null);
				holder.commentList.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View view, final int commentPosition, long id) {

						// 长按进行复制或者删除
						CommentItem commentItem = commentsDatas.get(commentPosition);
						CommentDialog dialog = new CommentDialog(mContext, mPresenter, commentItem, position);
						dialog.show();
						return true;
					}
				});
			} else {

				holder.commentList.setVisibility(View.GONE);
			}
			holder.digCommentBody.setVisibility(View.VISIBLE);
		} else {
			holder.digCommentBody.setVisibility(View.GONE);
		}

		holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

		final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
		// 判断是否已点赞
		String curUserFavortId = circleItem.getCurUserFavortId(username);
		if (!TextUtils.isEmpty(curUserFavortId)) {
			snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
		} else {
			snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
		}
		snsPopupWindow.update();
		snsPopupWindow.setmItemClickListener(new PopupItemClickListener(position, circleItem, curUserFavortId));
		holder.snsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// 弹出popupwindow
				snsPopupWindow.showPopupWindow(view);
			}
		});

		holder.urlTipTv.setVisibility(View.GONE);
		switch (itemViewType) {
			case ITEM_VIEW_TYPE_URL:// 处理链接动态的链接内容和和图片
				// String linkImg = circleItem.getLinkImg();
				// String linkTitle = circleItem.getLinkTitle();
				// ImageLoader.getInstance().displayImage(linkImg,
				// holder.urlImageIv);
				// holder.urlContentTv.setText(linkTitle);
				// holder.urlBody.setVisibility(View.VISIBLE);
				// holder.urlTipTv.setVisibility(View.VISIBLE);
				break;
			case ITEM_VIEW_TYPE_IMAGE:// 处理图片
				final List<String> photos = circleItem.getPhotos();
				if (photos != null && photos.size() > 0) {
					holder.multiImageView.setVisibility(View.VISIBLE);
					holder.multiImageView.setList(photos);
					holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
						@Override
						public void onItemClick(View view, int position) {
							ImagePagerActivity.imageSize = new ImageSize(view.getWidth(), view.getHeight());
							ImagePagerActivity.startImagePagerActivity(mContext, photos, position);
						}
					});
				} else {
					holder.multiImageView.setVisibility(View.GONE);
				}
				break;
			default:
				break;
		}
		return convertView;
	}

	class ViewHolder {
		public CircularImage headIv;
		public TextView nameTv;
		public TextView urlTipTv;
		/** 动态的内容 */
		public TextView contentTv;
		public TextView timeTv;
		public TextView deleteBtn;
		public ImageView snsBtn;
		/** 点赞列表 */
		public FavortListView favortListTv;

		public LinearLayout urlBody;
		public LinearLayout digCommentBody;
		public View digLine;

		/** 评论列表 */
		public AppNoScrollerListView commentList;
		/** 链接的图片 */
		public ImageView urlImageIv;
		/** 链接的标题 */
		public TextView urlContentTv;
		/** 图片 */
		public MultiImageView multiImageView;
		// ===========================
		public FavortListAdapter favortListAdapter;
		public CommentAdapter bbsAdapter;
		public SnsPopupWindow snsPopupWindow;
	}

//	SweetAlertDialog dialog;

	private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
		private String mFavorId;
		// 动态在列表中的位置
		private int mCirclePosition;
		private long mLasttime = 0;
		private CircleItem mCircleItem;

		public PopupItemClickListener(int circlePosition, CircleItem circleItem, String favorId) {
			this.mFavorId = favorId;
			this.mCirclePosition = circlePosition;
			this.mCircleItem = circleItem;
		}

		@Override
		public void onItemClick(final ActionItem actionitem, int position) {
			switch (position) {
				case 0:// 点赞、取消点赞
					if (System.currentTimeMillis() - mLasttime < 700) // 防止快速点击操作
						return;
					mLasttime = System.currentTimeMillis();
//				dialog = new SweetAlertDialog(mContext);
//				dialog.setTitleText("请稍候...");
//				dialog.show();
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Map<String, String> map = new HashMap<String, String>();
								map.put("id", mCircleItem.getId());
								map.put("username", username);
								final JSONObject jsonObject = new JSONObject(
										HelperUtil.postRequest(HttpConstantUtil.AddComplanitZan, map));
								if (jsonObject.getInt("result") > 0) {
									Looper.prepare();
									new Handler().post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											handler.sendEmptyMessage(1);
											try
											{

												if ("赞".equals(actionitem.mTitle.toString())) {
													mPresenter.addFavort(mCirclePosition,String.valueOf(jsonObject.getInt("result")));
												} else {// 取消点赞
													mPresenter.deleteFavort(mCirclePosition, mFavorId);
												}
												Intent intent = new Intent(ReceiverConstant.MESSAGE_ZAN_ACTION);
												intent.putExtra("toID", mCircleItem.getUser().getId());
												intent.putExtra("nickname", nickname);
												mContext.sendBroadcast(intent);
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
										}
									});
									Looper.loop();
								} else {
									handler.sendEmptyMessage(0);
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								handler.sendEmptyMessage(-1);
							}
						}
					}).start();

					break;
				case 1:// 发布评论
					if (mCirclePublicCommentContral != null) {
						mCirclePublicCommentContral.editTextBodyVisible(View.VISIBLE, mPresenter, mCirclePosition,
								TYPE_PUBLIC_COMMENT, null, 0, mCircleItem.getId(),mCircleItem.getUser().getId());
					}
					break;
				default:
					break;
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
//				if (dialog != null)
//					dialog.dismiss();
					HelperUtil.totastShow("失败", mContext);

					break;
				case 1:
//				if (dialog != null)
//					dialog.dismiss();
					HelperUtil.totastShow("成功", mContext);
					break;
				case -1:
//				if (dialog != null)
//					dialog.dismiss();
					HelperUtil.totastShow(PublicConstant.ToastCatch, mContext);
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void update2DeleteCircle(String circleId) {
		for (int i = 0; i < datas.size(); i++) {
			if (circleId.equals(datas.get(i).getId())) {
				getDatas().remove(i);
				notifyDataSetChanged();
				return;
			}
		}
	}

	@Override
	public void update2AddFavorite(int circlePosition,String id) {
		FavortItem item = new FavortItem();
		item.setId(id);
		item.setUser(curUser);
		getDatas().get(circlePosition).getFavorters().add(item);
		notifyDataSetChanged();
	}

	@Override
	public void update2DeleteFavort(int circlePosition, String favortId) {
		List<FavortItem> items = getDatas().get(circlePosition).getFavorters();
		for (int i = 0; i < items.size(); i++) {
			if (favortId.equals(items.get(i).getId())) {
				getDatas().get(circlePosition).getFavorters().remove(i);
				notifyDataSetChanged();
				return;
			}
		}
	}

	@Override
	public void update2AddComment(int circlePosition, int type, User replyUser, String id) {
		CommentItem newItem = null;
		String content = "";
		if (mCirclePublicCommentContral != null) {
			content = mCirclePublicCommentContral.getEditTextString();
		}
		if (type == TYPE_PUBLIC_COMMENT) {
			newItem  = new CommentItem();
			newItem.setId(id);
			newItem.setContent(content);
			newItem.setUser(curUser);
		} else if (type == TYPE_REPLY_COMMENT) {
			newItem  = new CommentItem();
			newItem.setId(id);
			newItem.setContent(content);
			newItem.setUser(curUser);
			newItem.setToReplyUser(replyUser);
		}
		getDatas().get(circlePosition).getComments().add(newItem);
		notifyDataSetChanged();
		if (mCirclePublicCommentContral != null) {
			mCirclePublicCommentContral.clearEditText();
		}
	}

	@Override
	public void update2DeleteComment(int circlePosition, String commentId) {
		List<CommentItem> items = getDatas().get(circlePosition).getComments();
		for (int i = 0; i < items.size(); i++) {
			if (commentId.equals(items.get(i).getId())) {
				getDatas().get(circlePosition).getComments().remove(i);
				notifyDataSetChanged();
				return;
			}
		}
	}

}
