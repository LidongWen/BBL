package com.beibeilian.beibeilian.im.video;

import android.content.Context;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 延时弹框下载
 *
 */
public class PlayerTimer {
	private int interval = 1; // 间隔
	private int times = 1; // 次数 默认1
	private Timer timer = null; // 定时器
	private TimerTask task; // 任务
	private Context context;

	public PlayerTimer(final Context context) {
		timer = new Timer();
		this.context = context;
		times = 1;
		task = new TimerTask() {
			@Override
			public void run() {
				if (times <= 0) {
					stopPop();
					return;
				}
				// 弹框操作
				Intent intent3 = new Intent();
				intent3.setAction(MyReceiver.STARTPOP);
				context.sendBroadcast(intent3);
				times--;
			}
		};
	}

	public void startPop() {
		timer.schedule(task, interval * 1000, interval * 1000);
	}

	public void stopPop() {
		timer.cancel();
	}

}
