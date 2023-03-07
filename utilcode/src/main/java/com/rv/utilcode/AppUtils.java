package com.rv.utilcode;

import android.os.Build;

/**
 * author : rv
 * github : https://github.com/Mac-sir
 * time   : 2023/3/7
 * 主要功能 : 应用App 管理
 */
public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getUUID() {
        return DeviceUtil.getUUID();
    }

    public static void installApp(final String filePath) {

    }

    public static void exitApp() {
        
        System.exit(0);
    }
}
