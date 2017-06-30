package com.beibeilian.beibeilian.me.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.beibeilian.beibeilian.R;
import com.beibeilian.beibeilian.constant.BBLConstant;
import com.beibeilian.beibeilian.util.CrashHandler;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class BBLApplication extends Application {
	public static final int NUM_PAGE = 1;// 总共有多少页
	public static int NUM = 20;// 每页20个表情,还有最后一个删除button
	private static Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
	private static Context mContext;
	// 默认存放图片的路径
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator
			+ "CircleDemo" + File.separator + "Images" + File.separator;

	public static int mKeyBoardH = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		initFaceMap();
		HelperUtil.sendUserPayAction(getApplicationContext(), "0", BBLConstant.ACTION_APP_START, "0", "0", "0");
		mContext = getApplicationContext();
		initImageLoader();
	}

	/** 初始化imageLoader */
	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.color.bg_no_photo)
				.showImageOnFail(R.color.bg_no_photo).showImageOnLoading(R.color.bg_no_photo).cacheInMemory(true)
				.cacheOnDisk(true).build();

		File cacheDir = new File(DEFAULT_SAVE_IMAGE_PATH);
		ImageLoaderConfiguration imageconfig = new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(200).diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).defaultDisplayImageOptions(options).build();

		ImageLoader.getInstance().init(imageconfig);
	}

	public static Context getContext() {
		return mContext;
	}

	public static Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

	private void initFaceMap() {
		// TODO Auto-generated method stub
		mFaceMap.put("[呲牙]", R.drawable.f_static_000);
		mFaceMap.put("[调皮]", R.drawable.f_static_001);
		mFaceMap.put("[微笑]", R.drawable.f_static_023);
		mFaceMap.put("[偷笑]", R.drawable.f_static_003);
		mFaceMap.put("[可爱]", R.drawable.f_static_018);
		mFaceMap.put("[色]", R.drawable.f_static_019);
		mFaceMap.put("[害羞]", R.drawable.f_static_020);
		mFaceMap.put("[敲打]", R.drawable.f_static_005);
		mFaceMap.put("[饭]", R.drawable.f_static_058);
		mFaceMap.put("[猪头]", R.drawable.f_static_007);
		mFaceMap.put("[玫瑰]", R.drawable.f_static_008);
		mFaceMap.put("[流泪]", R.drawable.f_static_009);
		mFaceMap.put("[爱心]", R.drawable.f_static_028);
		mFaceMap.put("[白眼]", R.drawable.f_static_030);
		mFaceMap.put("[爱情]", R.drawable.f_static_038);
		mFaceMap.put("[拥抱]", R.drawable.f_static_045);
		mFaceMap.put("[握手]", R.drawable.f_static_054);
		mFaceMap.put("[强]", R.drawable.f_static_052);
		mFaceMap.put("[月亮]", R.drawable.f_static_068);
		mFaceMap.put("[再见]", R.drawable.f_static_004);

	}
}
