package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

/**
 * @author 86186
 */
public class AppLauncher {
    public static void openApp(Context context, String appName) {
        String qqPackageName = "com.tencent.mobileqq";
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(qqPackageName);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            // QQ应用未安装
            Log.e("QQ","NO");
        }
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
}
