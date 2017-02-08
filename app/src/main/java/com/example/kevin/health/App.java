package com.example.kevin.health;

import android.app.Application;

/**
 * Created by hyx on 2017/2/7.
 */

public class App extends Application {
    private static App app;

    public static App getApp(){
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }

    /**
     * Created by hyx on 2017/2/7.
     */

    public static class AppTool {

        public static <T> T checkNotNull(T reference) {
            if (reference == null) {
                throw new NullPointerException();
            }
            return reference;
        }
    }
}
