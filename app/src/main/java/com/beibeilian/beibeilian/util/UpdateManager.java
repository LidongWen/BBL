package com.beibeilian.beibeilian.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.db.BBLDao;
import com.beibeilian.beibeilian.me.model.Version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {
	private Context mContext;
	private Dialog downloadDialog;
	// 下载包安装路径
	private static final String savePath = PublicConstant.APKPATH;

	private static final String saveFileName = savePath + "beibeilian.apk";

	// 进度条与通知ui刷新的hander和msg常量
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private static final int TimeOUT = 3;

	private static final int NoSDK = 4;

	private static final int UpdateIng = 5;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case DOWN_UPDATE:
					mProgress.setProgress(progress);
					break;

				case DOWN_OVER:// 下载结束执行安装
					installApk();
					break;
				case TimeOUT:
					if (downloadDialog != null) {
						downloadDialog.dismiss();
					}
					if (mProgress != null) {
						mProgress.setVisibility(View.GONE);
					}
					HelperUtil.totastShow("请检查网络是否良好或服务器故障", mContext);
					break;
				case NoSDK:
					HelperUtil.totastShow("请插入SD卡", mContext);
					break;
				case UpdateIng:
					downProgressloadDialog();
					break;
				default:
					break;
			}
		};
	};

	private BBLDao dao;
	private Version model;

	public UpdateManager(Context context) {
		this.mContext = context;
		dao = new BBLDao(mContext, null, null, 1);
		model = dao.queryVersion();
		System.out.println(model.getCode() + model.getContent() + model.getSize() + model.getUrl());
	}

	// 外部接口让主activity调用
	public void startUpdateInfo() {

		showDownloadDialog(model.getCode(), model.getSize(), model.getContent());
	}

	private void showDownloadDialog(String code, String size, String content) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("版本更新");
		builder.setMessage("当前有新的版本需要更新,版本号" + code + ",文件大小" + size + "M," + content + ",为了节省流量，您可以选择在WIFI环境下更新。")
				.setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mHandler.sendEmptyMessage(UpdateIng);
				dialog.dismiss();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				if (downLoadThread != null) {
					downLoadThread.interrupt();
				}
			}
		}).show();
		// downloadDialog = builder.create();
		// downloadDialog.show();

	}

	private void downProgressloadDialog() {
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(false);
		builder.setView(v);
		builder.setTitle("正在版本更新...").setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				interceptFlag = true;
				if (downLoadThread != null) {
					downLoadThread.interrupt();
				}
				dialog.dismiss();
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		if (downLoadThread != null) {
			downLoadThread.interrupt();
		}
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();

	}

	private Runnable mdownApkRunnable = new Runnable() {

		@Override
		public void run() {

			try {

				// 判断是否有SD卡
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					mHandler.sendEmptyMessage(NoSDK);
					return;
				}
				URL url = new URL(model.getUrl());// 声明url

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 用于网络访问的对象
				conn.connect();
				conn.setConnectTimeout(10000); // 设置连接超时为10s
				conn.setReadTimeout(10000);
				int length = conn.getContentLength();// 获取访问内容的长度。这里指apk文件的大小
				InputStream is = conn.getInputStream();// 将apk文件
				// 以流的形式写入到该应用程序的内存中
				File file = new File(savePath);// apk保存的根目录------------------//创建文件夹
				if (!file.exists()) {// 如果file不存在 则创建
					file.mkdirs();
				}
				String apkFile = saveFileName;// 保存到sd卡上的apk文件名
				File ApkFile = new File(apkFile);// 创建文件，此时该文件的内容是空的
				if (ApkFile.exists()) {
					ApkFile.delete();
				}
				FileOutputStream fos = new FileOutputStream(ApkFile);// 将apk文件输出到sd卡，此时还没有保存到sd卡
				int count = 0;// 用于获取下载的速度
				byte buf[] = new byte[1024];// 声明byte字节
				// 循环。相当于用户点击开始按钮了，现在开始将apk的内容写入到文件中
				do {
					int numread = is.read(buf);// 每次读取1024个byte字节
					count += numread;
					progress = (int) (((float) count / length) * 100);// 将进度以%制显示
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);// apk的内容写入到文件中
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mHandler.sendEmptyMessage(TimeOUT);
			}
		}
	};

	// /**
	// * 下载apk
	// */
	// private void downloadApk() {
	// downLoadThread = new Thread(mdownApkRunnable);
	// downLoadThread.start();
	// }

	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
