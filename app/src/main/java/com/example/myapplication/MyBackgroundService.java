package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * @author 86186
 */
public class MyBackgroundService extends Service {
    private Handler handler;
    private Runnable clickRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        clickRunnable = new Runnable() {
            @Override
            public void run() {
                // 在此处编写逻辑来模拟点击操作
                // ...
                Log.e("OPEN", "Triggered onAccessibilityEvent()");
                AutoClickUtil.autoClick(getApplicationContext(),0.5F,0.5F);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(clickRunnable, 5000); // 5秒后执行点击操作
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(clickRunnable); // 移除延迟任务
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
