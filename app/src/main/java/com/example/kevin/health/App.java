package com.example.kevin.health;

import android.app.Application;

import org.litepal.LitePal;

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
        LitePal.initialize(getApplicationContext());
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
