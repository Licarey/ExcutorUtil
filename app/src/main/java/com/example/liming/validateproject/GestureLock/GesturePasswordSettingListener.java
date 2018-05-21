package com.example.liming.validateproject.GestureLock;

/**
 * Created by liming on 2017/11/23.
 * email liming@finupgroup.com
 */

public interface GesturePasswordSettingListener {
    boolean onFirstInputComplete(int length);
    void onSuccess();
    void onFail();
}
