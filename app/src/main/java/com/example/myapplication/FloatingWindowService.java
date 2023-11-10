package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FloatingWindowService extends Service implements View.OnClickListener {

    private WindowManager mWindowManager;
    private View mFloatingView;
    private Button mCloseBtn;

    // 记录上一次触摸位置的坐标
    private float mLastX;
    private float mLastY;
    EditText editText_x;
    EditText editText_y;


    public FloatingWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化悬浮窗视图和按钮
        initFloatingView();

        // 给悬浮窗视图设置触摸监听器
//        mFloatingView.setOnTouchListener(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 显示悬浮窗
        if (mFloatingView != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.CENTER | Gravity.CENTER;

            mWindowManager.addView(mFloatingView, params);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 移除悬浮窗视图
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_open) {
            Log.e("CLICK", "START!!!!!!!!!!");


        }

    }

    private void initFloatingView() {
        // 加载悬浮窗视图
        LayoutInflater inflater = LayoutInflater.from(this);
        mFloatingView = inflater.inflate(R.layout.floating_window_layout, null);

//        // 初始化关闭按钮
//        mCloseBtn = mFloatingView.findViewById(R.id.btn_close);
//        mCloseBtn.setOnClickListener(this);
        //初始化自动点击按钮
        Button openButton = mFloatingView.findViewById(R.id.btn_open);
        openButton.setOnClickListener(this);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        editText_x = mFloatingView.findViewById(R.id.input_x);
        String userInput_x = editText_x.getText().toString();

        editText_y = mFloatingView.findViewById(R.id.input_y);
        String userInput_y = editText_y.getText().toString();

        int x = Integer.parseInt(userInput_x);
        int y = Integer.parseInt(userInput_y);



    }


}