package com.beibeilian.beibeilian.util;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.me.application.BBLApplication;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperUtil {

	public static HttpClient httpClient;
	private static ExecutorService mExecutorService;

	public static ExecutorService getThreadPools() {
		if (mExecutorService == null) {
			mExecutorService = Executors.newFixedThreadPool(50);
		}
		return mExecutorService;
	}

	public static String get6Random() {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < 6; i++) {
			result += random.nextInt(10);
		}
		return result;
	}

	public static double formatFileSize(long fileS) {// 转换文件大小
		return (double) fileS / 1048576;
	}

	public static boolean CheckEmail(String email) {
		Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static void totastShow(String content, Context ct) {
		Toast toast = Toast.makeText(ct, content, 3000);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static String longToDateString(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(date * 1000);
		String sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
		return sDateTime;
	}

	public static boolean CheckChinese(String sequence) {
		final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
		boolean result = false;
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(sequence);
		result = matcher.find();
		return result;
	}

	public static boolean isConnect(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		return false;
	}

	public static boolean flagISNoNull(String flag) {
		if (flag != null && !flag.equals("") && !flag.equals("null")) {
			return true;
		}
		return false;
	}

	public static String postRequest(String url, Map<String, String> map) throws Exception {
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 2 * 60 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 2 * 60 * 1000);
			httpClient = new DefaultHttpClient(httpParams);
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : map.keySet()) {
				params.add(new BasicNameValuePair(key, map.get(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
			HttpResponse httpResponse = httpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static String getRequest(String url) throws Exception {
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 2 * 60 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 2 * 60 * 1000);
			httpClient = new DefaultHttpClient(httpParams);
			HttpGet get = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	public static boolean inputMactches(String str) {
		return str.matches("[A-Za-z0-9_]+");
	}

	public static String DateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssms");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String messageTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String messageTimeConvert(long temptime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(temptime);// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String getVersionCode(Context context) {
		String versionCode = "0";
		try {
			// 获取软件版本号，
			versionCode = context.getPackageManager().getPackageInfo("com.beibeilian.android.app", 1).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static boolean isUpdateVersion(String oldVersion, String newVersion) {
		boolean flag = false;
		System.out.println("newv---" + newVersion + "oldv--" + oldVersion);

		if (!newVersion.equals(oldVersion)) {
			System.out.println("不相同");
			return true;
		}
		return flag;
	}

	/**
	 * 自定义dialog
	 *
	 * @param mProgressDialog
	 * @param context
	 * @param content
	 */
	public static void customDialogShow(Dialog mProgressDialog, Context context, String content) {
		TextView load_text;
		mProgressDialog.setContentView(R.layout.loading);
		mProgressDialog.setCancelable(true);
		load_text = (TextView) mProgressDialog.findViewById(R.id.login_load_text_id);
		load_text.setText(content);
		mProgressDialog.show();
	}

	/**
	 * 时间差
	 *
	 * @param startime
	 * @param endtime
	 * @param textView
	 */
	public static void timeDifference(String startime, String endtime, TextView textView) {
		try {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date begin = dfs.parse(startime);
			java.util.Date end = dfs.parse(endtime);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			// long day = between / (24 * 3600);
			long hour = between % (24 * 3600) / 3600;
			long minute = between % 3600 / 60;
			textView.setText(hour + " 小时  " + minute + " 分");
		} catch (Exception e) {

		}
	}

	/**
	 * 压缩图片
	 *
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static Bitmap compressImage(ByteArrayOutputStream mByteArrayOutputStream) throws IOException {
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// try {
		// image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		// int options = 100;
		// int photo_options = 0;
		// while (baos.toByteArray().length / 1024 > 800) { //
		// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
		// baos.reset();// 重置baos即清空baos
		// options -= 30;// 每次都减少10
		// photo_options = photo_options + 30;
		// // String[] args = new String[] { String.valueOf(photo_options) };
		// // android.os.Message msg = handler.obtainMessage();
		// // msg.what = 1;
		// // msg.obj = args;
		// // msg.sendToTarget();
		// image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
		// 这里压缩options%，把压缩后的数据存放到baos中
		// if (options <= 10) {
		// break;
		// }
		//
		// }
		// // android.os.Message msg = handler.obtainMessage();
		// // msg.what = 2;
		// // msg.sendToTarget();
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inInputShareable = true;
		bfOptions.inSampleSize = 8;
		bfOptions.inTempStorage = new byte[64 * 1024];
		ByteArrayInputStream isBm = new ByteArrayInputStream(mByteArrayOutputStream.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, bfOptions);// 把ByteArrayInputStream数据生成图片
		isBm.close();
		return bitmap;
	}

	// public static String versionCode(Context context) {
	// try {
	// String name = context.getPackageManager().getPackageInfo(
	// "com.home", 1).versionName;
	// return name;
	// } catch (NameNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static String getJabberID(String from) {
		String[] res = from.split("/");
		return res[0].toLowerCase();
	}

	private static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	public static CharSequence convertNormalStringToSpannableString(Context context, String message, boolean small) {
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (BBLApplication.getFaceMap().containsKey(str2)) {
					int face = BBLApplication.getFaceMap().get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), face);
					if (bitmap != null) {
						if (small) {
							int rawHeigh = bitmap.getHeight();
							int rawWidth = bitmap.getHeight();
							int newHeight = 30;
							int newWidth = 30;
							// 计算缩放因子
							float heightScale = ((float) newHeight) / rawHeigh;
							float widthScale = ((float) newWidth) / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(heightScale, widthScale);
							// 设置图片的旋转角度
							// matrix.postRotate(-30);
							// 设置图片的倾斜
							// matrix.postSkew(0.1f, 0.1f);
							// 将图片大小压缩
							// 压缩后图片的宽和高以及kB大小均会变化
							bitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
						}
						ImageSpan localImageSpan = new ImageSpan(context, bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}

	public static void cancelNetNotificationManager(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(PublicConstant.NetPend);
	}

	public static void cancelStartCommandNotificationManager(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(PublicConstant.STARTCOMMANDSERVICCEPEND);
	}

	public static void cancelAllNotificationManager(Context context) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(PublicConstant.NetPend);
		notificationManager.cancel(PublicConstant.MessagePend);
		notificationManager.cancel(PublicConstant.VisitPend);
		notificationManager.cancel(PublicConstant.PADPend);
		notificationManager.cancel(PublicConstant.BADPend);
		notificationManager.cancel(PublicConstant.POINTSPend);
		notificationManager.cancel(PublicConstant.ZANPend);
		notificationManager.cancel(PublicConstant.COMMITPend);
		notificationManager.cancel(PublicConstant.ANLIANPend);
		notificationManager.cancel(PublicConstant.GroupMessagePend);

	}

	public static String getIMIE(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		try {
			String deviceid = tm.getDeviceId();
			if (HelperUtil.flagISNoNull(deviceid)) {
				return deviceid;
			} else {
				return HelperUtil.DateTime() + HelperUtil.get6Random();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return HelperUtil.DateTime() + HelperUtil.get6Random();

	}

	public static String getAfter15DAY() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		Calendar cld = Calendar.getInstance();
		cld.set(Calendar.YEAR, year);
		cld.set(Calendar.MONDAY, month);
		cld.set(Calendar.DATE, day);
		cld.add(Calendar.DATE, 15);
		int yearnew = cld.get(Calendar.YEAR);
		int monthnew = cld.get(Calendar.MONTH);
		int daynew = cld.get(Calendar.DATE);
		String monthnews = String.valueOf(monthnew);
		String daynews = String.valueOf(daynew);
		if (monthnew < 10) {
			monthnews = "0" + monthnew;
		}
		if (daynew < 10) {
			daynews = "0" + daynew;
		}
		return yearnew + monthnews + daynews;
	}

	public static String getAfterMonth(int month) {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, month);
		return f.format(c.getTime());
	}

	public static String levelDAY() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		return str;
	}

	public static void getPicassoImageByUrl(Context context, String imageUrl, Drawable defauleimg,
											ImageView vImageView) {
		Picasso.with(context).load(getImageURL(imageUrl)).placeholder(defauleimg).error(defauleimg).tag(context)
				.into(vImageView);
	}

	public static String getImageURL(String url) {
		if (flagISNoNull(url) && url.contains(".jpg") && url.contains("http://")) {
			return BBLConstant.PHOTO_BEFORE_URL + url.substring(url.lastIndexOf("/") + 1);
		} else {
			return url;
		}
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void sendUserPayAction(final Context mcontext, final String username, final String actiontype,
										 final String errorinfo, final String id, final String price) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", username);
					map.put("imei", HelperUtil.getIMIE(mcontext));
					map.put("phonemodel", android.os.Build.MODEL);
					map.put("phoneversion", android.os.Build.VERSION.RELEASE);
					map.put("actiontype", actiontype);
					map.put("errorinfo", errorinfo);
					map.put("recordid", id);
					map.put("price", price);
					map.put("fromtype", "0");
					map.put("appversion", HelperUtil.getVersionCode(mcontext));
					map.put("ip", HelperUtil.getLocalIpAddress());
					map.put("channel",HelperUtil.getChannelId(mcontext));
					map.put("appid",HelperUtil.getAPPlId(mcontext));
					HelperUtil.postRequest(HttpConstantUtil.SEND_USER_PAY_ACTION, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static String getChannelId(Context context) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			return bundle.getString("bbl_channelid");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "bbl_channelid";
	}
	public static String getChannelName(Context context) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			return bundle.getString("bbl_channelname");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "bbl_channelname";
	}
	public static String getAPPlId(Context context) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			return bundle.getString("bbl_appid");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "bbl_appid";
	}

	public static void uploadChannel(final Context mContext) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("imei", HelperUtil.getIMIE(mContext));
					map.put("channelid", HelperUtil.getChannelId(mContext));
					map.put("channelname", HelperUtil.getChannelName(mContext));

					map.put("appid", HelperUtil.getAPPlId(mContext));
					map.put("appversion", HelperUtil.getVersionCode(mContext));
					HelperUtil.postRequest(HttpConstantUtil.UploadChannel, map);
				} catch (Exception e) {

				}
			}
		}).start();
	}
}
