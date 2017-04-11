package com.example.jh.textviewforhtml;

import android.app.Application;
import android.content.Context;

/**
 * Created by jh on 2017/1/20.
 */
public class BaseApplcation extends Application {

    private static Context mContetx;

    @Override
    public void onCreate() {
        mContetx = getContetx();
        super.onCreate();
    }

    public static Context getContetx(){
        return mContetx;
    }
}
