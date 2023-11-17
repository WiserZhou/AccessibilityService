package com.example.myapplication;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ComponentName;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button mBtnClick;
    private Button mBtnClick2;

    private Button mBtnQuery;
    private Button mOpenFloatBtn;
    private TextView mTvResult;

    private Button mBtnStartSpeechInput;

    private static final int REQUEST_CODE_FLOATING_WINDOW = 1001;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1002;
    private static final String OPPO_PACKAGE_NAME = "com.nearme.rom.floatwindow";
    private static final String OPPO_ACTIVITY_NAME = "com.nearme.gamecenter.floatwindow.FloatWindowActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openButton = findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOPPOSettings();
            }
        });

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
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            AutoClickUtil.autoClick(getApplicationContext(), 0.5F, 0.5F);
            Intent intent = new Intent(getApplicationContext(), MyAccessibilityService.class);
            startService(intent);
            Log.e("OPEN", "MyAccessibilityService.class");
        } else {
            // 未打开无障碍服务，跳转到无障碍设置页面
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }


        // 启动语音输入
        mBtnStartSpeechInput = findViewById(R.id.buttonSpeechInput);
        mBtnStartSpeechInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechInput();
            }
        });
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
                AppLauncher.openApp(getApplicationContext(), "open");
            }
        });
        mBtnQuery = findViewById(R.id.button4);
        mBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenAIManager openAIManager = new OpenAIManager();
                String prompt = "hello";
                openAIManager.queryGPTV2(prompt, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            System.out.println("Response: " + responseData);
                        } else {
                            System.out.println("Request failed: " + response.code() + " - " + response.message());
                        }
                    }
                });


            }
        });
        mOpenFloatBtn = findViewById(R.id.button3);
        mOpenFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button3) {
                    if (Settings.canDrawOverlays(getApplicationContext())) {

                        // 已经拥有悬浮窗权限，启动悬浮窗服务
                        startFloatingWindowService();
                    } else {
                        // 请求悬浮窗权限
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivityForResult(intent, REQUEST_CODE_FLOATING_WINDOW);
                    }
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FLOATING_WINDOW) {
            if (Settings.canDrawOverlays(this)) {
                // 用户已授予悬浮窗权限，启动悬浮窗服务
                startFloatingWindowService();
            }
        }

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String spokenText = result.get(0);
                mTvResult.setText(spokenText);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
//            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//
//            // 显示语音输入的结果
//            String spokenText = result.get(0);
//            tvResult.setText(spokenText);
//        }
    }

    private void startFloatingWindowService() {
        Intent intent = new Intent(this, FloatingWindowService.class);
        startService(intent);
    }

    private void startSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Sorry, your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }
    private void openOPPOSettings() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);

        // 模拟用户点击操作，进入小布助手
        ComponentName componentName = new ComponentName(OPPO_PACKAGE_NAME, OPPO_ACTIVITY_NAME);
        Intent oppoIntent = new Intent();
        oppoIntent.setComponent(componentName);
        startActivity(oppoIntent);
    }
}
