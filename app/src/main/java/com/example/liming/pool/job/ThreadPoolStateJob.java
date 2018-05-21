package com.example.liming.pool.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 收集所有线程池的状态信息，统计并输出汇总信息。
 */
public class ThreadPoolStateJob extends AbstractJob {

    private static Logger _logger = LoggerFactory.getLogger(ThreadPoolStateJob.class);
    
    private Map<String, ExecutorService> _multiThreadPool;
    
    public ThreadPoolStateJob(Map<String, ExecutorService> multiThreadPool, int interval) {
        this._multiThreadPool = multiThreadPool;
        super._interval = interval;
    }
    
    @Override
    protected void execute() {
        Set<Entry<String, ExecutorService>> poolSet = _multiThreadPool.entrySet();
        for (Entry<String, ExecutorService> entry : poolSet) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) entry.getValue();
            _logger.info("ThreadPool:{}, ActiveThread:{}, TotalTask:{}, CompletedTask:{}, Queue:{}", 
                    entry.getKey(), pool.getActiveCount(), pool.getTaskCount(), pool.getCompletedTaskCount(), pool.getQueue().size());
        }
        
        super.sleep();
    }

}
