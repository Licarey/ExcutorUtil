package com.example.liming.validateproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.liming.validateproject.FIngerPrintLock.FingerprintCore;
import com.example.liming.validateproject.FIngerPrintLock.FingerprintUtil;

import java.util.List;

/**
 * Created by liming on 2017/11/24.
 * email liming@finupgroup.com
 */

public class FingerprintMainActivity extends Activity implements View.OnClickListener {
    private FingerprintCore mFingerprintCore;
    private Toast mToast;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_print_lock_main);
        findViewById(R.id.fingerprint_recognition_start).setOnClickListener(this);
        findViewById(R.id.fingerprint_recognition_cancel).setOnClickListener(this);
        sharedPreferences = getSharedPreferences("isActiveTag" , Context.MODE_PRIVATE);
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
            case R.id.fingerprint_recognition_start:
                startFingerprintRecognition();
                break;
            case R.id.fingerprint_recognition_cancel:
                cancelFingerprintRecognition();
                break;
        }
    }

    /**
     * 开始指纹识别
     */
    private void startFingerprintRecognition() {
        if (mFingerprintCore.isSupport()) {
            if (!mFingerprintCore.isHasEnrolledFingerprints()) {
                toastTipMsg(R.string.fingerprint_recognition_not_enrolled);
                FingerprintUtil.openFingerPrintSettingPage(this);
                return;
            }
            toastTipMsg(R.string.fingerprint_recognition_tip);
            if (mFingerprintCore.isAuthenticating()) {
                toastTipMsg(R.string.fingerprint_recognition_authenticating);
            } else {
                mFingerprintCore.startAuthenticate();
            }
        } else {
            toastTipMsg(R.string.fingerprint_recognition_not_support);
        }
    }

    private void cancelFingerprintRecognition() {
        if (mFingerprintCore.isAuthenticating()) {
            mFingerprintCore.cancelAuthenticate();
        }
    }


    private void toastTipMsg(int messageId) {
        if (mToast == null) {
            mToast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT);
        }
        mToast.setText(messageId);
        mToast.cancel();
        mHandler.removeCallbacks(mShowToastRunnable);
        mHandler.postDelayed(mShowToastRunnable, 0);
    }

    private Runnable mShowToastRunnable = new Runnable() {
        @Override
        public void run() {
            mToast.show();
        }
    };

    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
            toastTipMsg(R.string.fingerprint_recognition_success);
        }

        @Override
        public void onAuthenticateFailed(int helpId) {
            toastTipMsg(R.string.fingerprint_recognition_failed);
        }

        @Override
        public void onAuthenticateError(int errMsgId) {
            toastTipMsg(R.string.fingerprint_recognition_error);
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(!sharedPreferences.getBoolean("isActive" , true)){
            sharedPreferences.edit().putBoolean("isActive" , true).apply();
            Log.e("LM" , "程序进入前台");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isAppOnForeground()){
            sharedPreferences.edit().putBoolean("isActive" , false).apply();
            Log.e("LM" , "程序进入后台");
        }
    }

    /**
     * 程序是否是前台运行
     * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        if (mFingerprintCore != null) {
            mFingerprintCore.onDestroy();
            mFingerprintCore = null;
        }
        mShowToastRunnable = null;
        mToast = null;
        super.onDestroy();
    }
}
