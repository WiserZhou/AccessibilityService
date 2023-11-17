package com.example.myapplication;
import android.content.Context;
import android.os.PowerManager;
import android.view.View;
public class MyButtonClickListener  implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        // 模拟按下电源键
        PowerManager powerManager = (PowerManager) v.getContext().getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
            wakeLock.acquire(500);  // 模拟按下 0.5 秒
        }
    }

}
