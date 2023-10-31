package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String CHANNEL_ID = "MyAccessibilityServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    Notification notification;
    @Override
    public void onCreate() {
        super.onCreate();

        // 创建通知渠道（仅适用于Android 8.0及更高版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Accessibility Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 创建前台服务通知
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("自动化服务")
                .setContentText("正在运行")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        // 设置服务为前台服务
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 注册前台服务
        startForeground(NOTIFICATION_ID, notification);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 停止前台服务
        stopForeground(true);

        // 停止服务
        stopSelf();
    }
    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT);
        Log.e("OPEN","HELLO");
        new Thread(new Runnable() {
            @Override
            public void run() {
                AutoClickUtil.autoClick(getApplicationContext(), 0.5F, 0.5F);
            }
        }).start();

//        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
//            AccessibilityNodeInfo nodeInfo = event.getSource();
//            if (nodeInfo != null) {
//                // 执行自动点击操作
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.example.myapp:id/button");
//                if (list != null && list.size() > 0) {
//                    AccessibilityNodeInfo button = list.get(0);
//                    button.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
//            }
//        }
    }

    @Override
    public void onInterrupt() {
        // 当服务被中断时，执行清理操作
    }

    public void triggerAccessibilityEvent() {
        // 创建一个模拟点击的手势事件
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        Path clickPath = new Path();
        clickPath.moveTo(100, 100); // 设置点击的位置
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(clickPath, 0, 100));

        // 发送手势事件来触发辅助功能服务的onAccessibilityEvent()方法
        dispatchGesture(gestureBuilder.build(), new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.e("OPEN", "Triggered onAccessibilityEvent()");
            }
        }, null);
    }
//    public void simulateClick() {
//        Log.e("OPEN", "2hello");
//        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow(); // 获取当前活动的根节点
//        if (nodeInfo != null && nodeInfo.isClickable()) {
//            Log.e("OPEN", "2hello");
//            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }else{
//
//        }
//    }
}
