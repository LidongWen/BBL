package com.beibeilian.beibeilian.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 *
 * 安全退出activity
 *
 * @author DingCuilin
 *
 */
public class ExitApplication {

	public static ArrayList<Activity> activities = new ArrayList<Activity>();;

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	/**
	 * 从集合中移除一个
	 *
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void exit() {
		for (Activity activity : activities) {
			if (activity != null) {
				activity.finish();
			}
		}
	}

}
