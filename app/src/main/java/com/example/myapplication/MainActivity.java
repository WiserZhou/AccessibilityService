package com.example.myapplication;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mBtnClick;
    private Button mBtnClick2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setThings();

        // 获取系统的AccessibilityManager
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);

// 判断是否已打开无障碍服务
        boolean isAccessibilityEnabled = false;
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo service : accessibilityServices) {
            if (service.getId().equals(getPackageName() + "/" + MyAccessibilityService.class.getName())) {
                isAccessibilityEnabled = true;
                break;
            }
        }

        if (isAccessibilityEnabled) {
            // 已打开无障碍服务，执行相应操作
            // ...
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AutoClickUtil.autoClick(getApplicationContext(), 0.5F, 0.5F);
        } else {
            // 未打开无障碍服务，跳转到无障碍设置页面
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }




    }
    @Override
    protected void onPause() {
        super.onPause();

        Intent serviceIntent = new Intent(getApplicationContext(), MyBackgroundService.class);
        startService(serviceIntent);

    }

    public void setThings() {
        mBtnClick = findViewById(R.id.button);
        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AutoClickUtil.autoClick(getApplicationContext(), 0.5F, 0.5F);
            }
        });
        mBtnClick2 = findViewById(R.id.button2);
        mBtnClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AppLauncher.openApp(getApplicationContext(),"open");
            }
        });
    }
}
