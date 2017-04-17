package com.ykrank.utils;

import android.os.Build;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by ykrank on 2016/9/6.
 */
public class SystemCompact {
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    private static final String KEY_EMUI_VERSION_CODE = "ro.build.hw_emui_api_level";

    public static boolean isEMUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_EMUI_VERSION_CODE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public static int getEMUICode() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            String valueString = prop.getProperty(KEY_EMUI_VERSION_CODE);
            try {
                return Integer.parseInt(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        } catch (final IOException e) {
            return 0;
        }
    }
}
