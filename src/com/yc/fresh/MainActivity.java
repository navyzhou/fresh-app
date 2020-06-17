package com.yc.fresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	NotificationManager notificationManager =  null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 获取消息管理器
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// 创建一个标题栏通知
		Notification notify = new Notification(R.drawable.copylogo, "您有新的消息", System.currentTimeMillis());
		notify.flags |= Notification.FLAG_AUTO_CANCEL; // 打开应用程序后图标消失
		Intent intent = new Intent(MainActivity.this, ContentActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
		notify.setLatestEventInfo(MainActivity.this, "新品上市", "点击查看详细内容...", pendingIntent); // 设置事件信息
		notificationManager.notify(101, notify); // 通过通知管理器发送通知，第一个参数消息ID随便给定，不重复就行

		final TextView text = (TextView) this.findViewById(R.id.textView1);

		/**
		 * CountDownTimer timer = new CountDownTimer(5000, 1000)中，
		 * 第一个参数表示总时间，第二个参数表示间隔时间。
		 * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
		 */
		CountDownTimer timer = new CountDownTimer(5000, 1000) {
			public void onTick(long millisUntilFinished) {
				text.setText(millisUntilFinished / 1000 + "秒");
			}

			public void onFinish() {
				text.setText("进入商店...");
				Intent intent = new Intent(MainActivity.this, ContentActivity.class);
				startActivity(intent); // 切换页面
			}
		};
		//调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
		timer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
