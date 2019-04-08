package com.example.demo;

import android.app.Application;
import android.content.Context;

/**
 * @author: Bob
 * @date :2019/3/29 10:54
 */
public class DemoApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
