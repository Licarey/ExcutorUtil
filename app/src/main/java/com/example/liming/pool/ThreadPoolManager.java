package com.example.liming.pool;

import android.util.Log;

import cn.aofeng.common4j.ILifeCycle;

/**
 * 线程池实例管理。
 * 
 */
public class ThreadPoolManager implements ILifeCycle {

    private ILifeCycle _threadPool = new ThreadPoolImpl(); 
    
    private static Object _lock = new Object();
    private boolean _initStatus = false;
    private boolean _destroyStatus = false;
    
    private static ThreadPoolManager _instance = new ThreadPoolManager();
    public static ThreadPoolManager getSingleton() {
        return _instance;
    }

    public ThreadPool getThreadPool() {
        return (ThreadPool) _threadPool;
    }
    
    // 用于单元测试和子类扩展
    protected void setThreadPool(ThreadPool threadPool) {
        this._threadPool = (ILifeCycle) threadPool;
    }
    
    @Override
    public void init() {
        synchronized (_lock) {
            Log.e("LM" , "inittatus  " + _initStatus);
            if (_initStatus) {
                return;
            }
            _threadPool.init();
            _initStatus = true;
        }
    }

    @Override
    public void destroy() {
        synchronized (_lock) {
            if (_destroyStatus) {
                return;
            }
            _threadPool.destroy();
            Log.e("LM" , "_threadPool destroy  ");
            _destroyStatus = true;
            _initStatus = false;
        }
    }

}
