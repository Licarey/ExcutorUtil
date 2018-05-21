package com.example.liming.validateproject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liming.validateproject.GestureLock.GestureEventListener;
import com.example.liming.validateproject.GestureLock.GestureLockViewGroup;
import com.example.liming.validateproject.GestureLock.GesturePasswordSettingListener;
import com.example.liming.validateproject.GestureLock.GestureUnmatchedExceedListener;

/**
 * Created by liming on 2017/11/23.
 * email liming@finupgroup.com
 */

public class GestureLockActivty extends Activity implements View.OnClickListener {
    private GestureLockViewGroup mGestureLockViewGroup;
    private TextView tv_state;
    private RelativeLayout rl_reset;
    private boolean isReset = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_lock_main);
        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.gesturelock);
        tv_state = findViewById(R.id.tv_state);
        rl_reset = findViewById(R.id.rl_reset);
        rl_reset.setOnClickListener(this);
        gestureEventListener();
        gesturePasswordSettingListener();
        gestureRetryLimitListener();
        setGestureWhenNoSet();
    }

    private void gestureEventListener() {
        mGestureLockViewGroup.setGestureEventListener(new GestureEventListener() {
            @Override
            public void onGestureEvent(boolean matched) {
                Log.e("LM" , "onGestureEvent matched: " + matched);
                if (!matched) {
                    tv_state.setTextColor(Color.RED);
                    tv_state.setText("手势密码错误");
                } else {
                    if (isReset) {
                        isReset = false;
                        Toast.makeText(GestureLockActivty.this, "清除成功!", Toast.LENGTH_SHORT).show();
                        resetGesturePattern();
                    } else {
                        tv_state.setTextColor(Color.WHITE);
                        tv_state.setText("手势密码正确");
                    }
                }
            }
        });
    }

    private void gesturePasswordSettingListener() {
        mGestureLockViewGroup.setGesturePasswordSettingListener(new GesturePasswordSettingListener() {
            @Override
            public boolean onFirstInputComplete(int len) {
                Log.e("LM" , "onFirstInputComplete");
                if (len > 3) {
                    tv_state.setTextColor(Color.WHITE);
                    tv_state.setText("再次绘制手势密码");
                    return true;
                } else {
                    tv_state.setTextColor(Color.RED);
                    tv_state.setText("最少连接4个点，请重新输入!");
                    return false;
                }
            }

            @Override
            public void onSuccess() {
                Log.e("LM" , "onSuccess");
                tv_state.setTextColor(Color.WHITE);
                Toast.makeText(GestureLockActivty.this, "密码设置成功!", Toast.LENGTH_SHORT).show();
                tv_state.setText("请输入手势密码解锁!");
            }

            @Override
            public void onFail() {
                Log.e("LM" , "onFail");
                tv_state.setTextColor(Color.RED);
                tv_state.setText("与上一次绘制不一致，请重新绘制");
            }
        });
    }

    private void gestureRetryLimitListener() {
        mGestureLockViewGroup.setGestureUnmatchedExceedListener(3, new GestureUnmatchedExceedListener() {
            @Override
            public void onUnmatchedExceedBoundary() {
                tv_state.setTextColor(Color.RED);
                tv_state.setText("错误次数过多，请稍后再试!");
            }
        });
    }

    private void setGestureWhenNoSet() {
        if (!mGestureLockViewGroup.isSetPassword()){
            Log.e("LM" , "未设置密码，开始设置密码");
            tv_state.setTextColor(Color.WHITE);
            tv_state.setText("绘制手势密码");
        }
    }

    private void resetGesturePattern() {
        mGestureLockViewGroup.removePassword();
        setGestureWhenNoSet();
        mGestureLockViewGroup.resetView();
    }

    @Override
    public void onClick(View v) {
        isReset = true;
        tv_state.setTextColor(Color.WHITE);
        tv_state.setText("请输入原手势密码");
        mGestureLockViewGroup.resetView();
    }
}
