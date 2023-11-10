package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String CHANNEL_ID = "MyAccessibilityServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        // 停止服务
        stopSelf();
    }

    @Override
    public void onServiceConnected() {

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


        triggerAccessibilityEvent(200,200);
        Log.e("OPEN", "start to find  ");
//        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AutoClickUtil.autoClick(getApplicationContext(), 0.5F, 0.5F);
//            }
//        }).start();

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
//        try {
//            //拿到根节点
//            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
//            if (rootInfo == null) {
//                return;
//            }
//            //开始找目标节点，这里拎出来细讲，直接往下看正文
//            if (rootInfo.getChildCount() != 0) {
//                if (rootInfo == null || TextUtils.isEmpty(rootInfo.getClassName())) {
//                    return;
//                }
//                Log.e("START", "start to find");
//                //开始去找
//                findByID(rootInfo, "com.tencent.mobileqq:id/chat_item_content_layout");
//            }
//        } catch (Exception e) {
//            Log.e("not found ERROR!", "not found!");
//        }
    }

    private void findByID(AccessibilityNodeInfo rootInfo, String text) {
        if (rootInfo.getChildCount() > 0) {
            Log.e("START", "start to find 1");
            for (int i = 0; i < rootInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = rootInfo.getChild(i);
                Log.e("START", "start to find 2");
                try {
                    if (child.findAccessibilityNodeInfosByViewId(text).size() > 0) {
                        Log.e("START", "start to find 3");
                        for (AccessibilityNodeInfo info : child.findAccessibilityNodeInfosByViewId(text)) {
                            Log.e("START", "start to find 4");
                            getClickable(info).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Log.e("CLICK", "have clicked");
//                            performClick(getClickable(info));
                            //模仿全局手势
//                        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS);
                        }
                    }
                } catch (NullPointerException e) {
                }
                findByID(child, text);//递归一直找一层层的全部遍历
            }
        }
    }

    //有些节点不可点击 点击交给父级甚至父级的父级...来做的。
    private AccessibilityNodeInfo getClickable(AccessibilityNodeInfo info) {
        Log.i(TAG, info.getClassName() + ": " + info.isClickable());
        if (info.isClickable()) {
            return info;//如果可以点击就返回
        } else {//不可点击就检查父级 一直递归
            return getClickable(info.getParent());
        }
    }

    private AccessibilityNodeInfo findByText(AccessibilityNodeInfo rootInfo, String text) {
        if (rootInfo.getChildCount() > 0) {
            for (int i = 0; i < rootInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = rootInfo.getChild(i);
                try {
                    if (child.findAccessibilityNodeInfosByText(text).size() > 0) {
                        for (AccessibilityNodeInfo info : child.findAccessibilityNodeInfosByText(text)) {
                            getClickable(info).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            performAction(getClickable(info));
                            return null;

                        }
                    }
                } catch (NullPointerException e) {
                }
                findByText(child, text);
            }
        }
        return null;

    }

    private void changeInput(AccessibilityNodeInfo info, String text) {  //改变editText的内容
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                text);
        info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    private void MyGesture() {//仿滑动
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Path path = new Path();
            path.moveTo(1000, 1000);//滑动起点
            path.lineTo(2000, 1000);//滑动终点
            GestureDescription.Builder builder = new GestureDescription.Builder();
            GestureDescription description = builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 100L)).build();
            //100L 第一个是开始的时间，第二个是持续时间
            dispatchGesture(description, new MyCallBack(), null);
        }
    }

    //模拟手势的监听
    @RequiresApi(api = Build.VERSION_CODES.N)
    private class MyCallBack extends GestureResultCallback {
        public MyCallBack() {
            super();
        }

        @Override
        public void onCompleted(GestureDescription gestureDescription) {
            super.onCompleted(gestureDescription);

        }

        @Override
        public void onCancelled(GestureDescription gestureDescription) {
            super.onCancelled(gestureDescription);

        }
    }

    @Override
    public void onInterrupt() {
        // 当服务被中断时，执行清理操作
    }

    public void triggerAccessibilityEvent(int x, int y) {
        // 创建一个模拟点击的手势事件
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        Path clickPath = new Path();
        clickPath.moveTo(x, y); // 设置点击的位置
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(clickPath, 0, 100));

        // 发送手势事件来触发辅助功能服务的onAccessibilityEvent()方法
        dispatchGesture(gestureBuilder.build(), new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
//                Log.e("OPEN", "Triggered onAccessibilityEvent()");
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
