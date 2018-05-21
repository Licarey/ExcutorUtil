package com.example.liming;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.example.liming.pool.ThreadPoolManager;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class APP extends MultiDexApplication {
    private static ThreadPoolManager tpm;
    private static APP instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static APP getInstance(){
        return instance;
    }

    public static ThreadPoolManager getTpm(){
        tpm = ThreadPoolManager.getSingleton();
        tpm.init();
        return tpm;
    }

    public static void destroy(){
        if(tpm != null){
            tpm.destroy();
        }
    }
}
