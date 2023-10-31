package com.example.myapplication;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import java.util.Collections;
import java.util.List;

/**
 * @author 86186
 */
public class AppLauncher {
    public static void openApp(Context context, String appName) {
        new AppLauncher().getAllApp(context);
//        E  pkg:com.zhihu.android —— cls:com.zhihu.android.app.ui.activity.LauncherActivity
        Intent intent = new Intent();
        //这里跳转的是淘宝的启动页
        ComponentName comp = new ComponentName("com.zhihu.android", "com.zhihu.android.app.ui.activity.LauncherActivity");
        intent.setComponent(comp);
        //为三方的activity新开任务栈
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


        AutoClickUtil.autoClick(context, 0.5F, 0.5F);
//        String packageName = "com.taobao.taobao"; // 淘宝应用程序的包名
//
//        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//        if (launchIntent != null) {
//            context.startActivity(launchIntent);
//        } else {
//            // 未找到淘宝应用程序
//            Log.e("OPEN","ERROR");
//        }


//        PackageManager packageManager = context.getPackageManager();
//        List<ApplicationInfo> appList = packageManager.getInstalledApplications(0);
//
//
//        for (ApplicationInfo appInfo : appList) {
//            appName = packageManager.getApplicationLabel(appInfo).toString();
//            Log.e("APP", appName);
//            if (appName.equalsIgnoreCase(appName)) {
//                Intent appIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName);
//                if (appIntent != null) {
//                    context.startActivity(appIntent);
//                    return;
//                }
//            }
//        }
//
//        Log.e("AppLauncher", "未找到应用程序：" + appName);
//// 如果没有找到对应名称的应用程序，则进行相应的处理逻辑
//// TODO: 添加未找到应用的处理逻辑

    }

    public void getAllApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> appsInfo = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(appsInfo, new ResolveInfo.DisplayNameComparator(packageManager));
        for (ResolveInfo info : appsInfo) {
            String pkg = info.activityInfo.packageName;
            String cls = info.activityInfo.name;
            Log.e("app_info", "pkg:" + pkg + " —— cls:" + cls);
        }
    }
}
