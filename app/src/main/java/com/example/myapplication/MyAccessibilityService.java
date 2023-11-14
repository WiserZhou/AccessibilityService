package com.example.myapplication;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_LONG_CLICKED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOWS_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
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

import java.time.Clock;
import java.util.List;
import java.util.Stack;

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

    //    TYPE_VIEW_CLICKED - 点击事件
//    TYPE_VIEW_LONG_CLICKED - 长按事件
//    TYPE_VIEW_SELECTED - 选中事件
//    TYPE_VIEW_FOCUSED - 获取焦点事件
//    TYPE_VIEW_TEXT_CHANGED - 文本变化事件
//    TYPE_WINDOW_STATE_CHANGED - 窗口状态变化事件
//    TYPE_NOTIFICATION_STATE_CHANGED - 通知状态变化事件
//    TYPE_VIEW_HOVER_ENTER - 悬停进入事件
//    TYPE_VIEW_HOVER_EXIT - 悬停退出事件
//    TYPE_TOUCH_EXPLORATION_GESTURE_START - 触摸浏览手势开始事件
//    TYPE_TOUCH_EXPLORATION_GESTURE_END - 触摸浏览手势结束事件
//    TYPE_VIEW_SCROLLED - 滚动事件
//    TYPE_VIEW_TEXT_SELECTION_CHANGED - 文本选中变化事件
//    TYPE_ANNOUNCEMENT - 公告事件
//    TYPE_VIEW_ACCESSIBILITY_FOCUSED - 获取辅助功能焦点事件
//    TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED - 清除辅助功能焦点事件
//    TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY - 在运动粒度遍历文本事件
//    TYPE_GESTURE_DETECTION_START - 手势识别开始事件
//    TYPE_GESTURE_DETECTION_END - 手势识别结束事件
//    TYPE_TOUCH_INTERACTION_START - 触摸交互开始事件
//    TYPE_TOUCH_INTERACTION_END - 触摸交互结束事件
//    TYPE_WINDOWS_CHANGED - 窗口变化事件
//    TYPE_VIEW_CONTEXT_CLICKED - 上下文点击事件
//    TYPE_ASSIST_READING_CONTEXT - 辅助阅读上下文事件
    static int index = 0;
    static boolean STATE_CHANGE_FLAG = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        boolean flag = false;

//        Log.e("OPEN", "start to find");
        if (event.getEventType() == TYPE_WINDOW_STATE_CHANGED && !STATE_CHANGE_FLAG) {
            Log.e("OPEN", "TYPE_WINDOW_STATE_CHANGED");
//            triggerAccessibilityEvent(400, 400);


            flag = true;
            STATE_CHANGE_FLAG = true;
        } else if (event.getEventType() == TYPE_VIEW_CLICKED) {
            Log.e("OPEN", "TYPE_VIEW_CLICKED");
//            triggerAccessibilityEvent(400, 400);
            flag = true;
            STATE_CHANGE_FLAG = false;
        } else if (event.getEventType() == TYPE_VIEW_FOCUSED) {
            Log.e("OPEN", "TYPE_VIEW_FOCUSED");
            flag = true;
            STATE_CHANGE_FLAG = false;
        } else if (event.getEventType() == TYPE_VIEW_SCROLLED) {
            Log.e("OPEN", "TYPE_VIEW_SCROLLED");
            flag = true;
            STATE_CHANGE_FLAG = false;
        } else if (event.getEventType() == TYPE_VIEW_LONG_CLICKED) {
            Log.e("OPEN", "TYPE_VIEW_LONG_CLICKED");
            flag = true;
            STATE_CHANGE_FLAG = false;
        }

        if (flag) {
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if (rootNode != null) {
                traverseAndPrintNode(rootNode);
            }
            flag = false;
            index = 0;
        }

//
    }

    private void traverseAndPrintNode(AccessibilityNodeInfo node) {

        if (node != null) {
            String nodeName = node.getClassName().toString();
//            ("android.widget.Button".equals(nodeName)
            if ("android.widget.EditText".equals(nodeName)) {
                String nodeId = "id=" + index;
                index++;
                String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
                System.out.println("<input " + nodeId + nodeLabel + ">" + node.getText() + "</input>");
            } else if (hasContent(node) || "android.widget.TextView".equals(nodeName) || "android.widget.Button".equals(nodeName) || "android.widget.ImageButton".equals(nodeName)) {
                if (node.getText() != null) {
                    String nodeId = "id=" + index;
                    index += 1;
                    String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
                    System.out.println("<button " + nodeId + nodeLabel + ">" + node.getText() + "</button>");
                }
            } else {
                String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
                if (nodeLabel == "" && !hasContent(node)) {
                } else {
                    String nodeId = "id=" + index;
                    index++;
                    System.out.println("<p " + nodeId + nodeLabel + "></p>");
                }
            }

            for (int i = 0; i < node.getChildCount(); i++) {
                traverseAndPrintNode(node.getChild(i));
            }
        }
    }

    private boolean hasContent(AccessibilityNodeInfo node) {
        CharSequence text = node.getText();
        return text != null && !text.toString().isEmpty();
    }

    private void traverseAndPrintNode2(AccessibilityNodeInfo node) {
        if (node != null) {
            String nodeName = node.getClassName().toString();
            String nodeId = "id=" + node.getWindowId();
            String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
            System.out.println("<" + nodeName + " " + nodeId + nodeLabel + "></" + nodeName + ">");

            for (int i = 0; i < node.getChildCount(); i++) {
                traverseAndPrintNode2(node.getChild(i));
            }
        }
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
///        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT);

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
