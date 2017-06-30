package com.beibeilian.beibeilian.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileUtil {

	private static final String TAG = "FileUtil";

	public static File getCacheFile(String imageUri) {
		File cacheFile = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			String fileName = getFileName(imageUri);
			File dir = new File(PublicConstant.FilePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			cacheFile = new File(dir, fileName);
			Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir + ",file:" + fileName);
		}

		return cacheFile;
	}

	public static String getFileName(String path) {
		int index = path.lastIndexOf("/");
		return path.substring(index + 1);
	}
}
