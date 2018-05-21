package com.example.liming.validateproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.liming.APP;
import com.example.liming.pool.ThreadPool;
import com.example.liming.pool.handler.DiscardFailHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class Test extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(200 , 50));
        button.setText("触发");
        button.setOnClickListener(this);
        setContentView(button);
    }

    /**
     * 执行不需要返回值的异步任务。
     */
    private void executeRunnableAnsyTask(ThreadPool threadPool) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            //threadPool.submit(new RunnableAsynTask());
            threadPool.submit(new RunnableAsynTask(), "other");

            Thread.sleep(50);
        }
    }

    /**
     * 执行需要返回值的异步任务。
     */
    private void executeCallableAnsyTask(ThreadPool threadPool)
            throws InterruptedException, ExecutionException {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        CallableAnsyTask task = new CallableAnsyTask(arr);
        //Future<Long> future = threadPool.submit(task);
        //Log.e("LM" , "异步任务在线程池default的执行结果为:"+future.get());
        Future<Long> future = threadPool.submit(task, "other");
        Log.e("LM" , "异步任务在线程池other的执行结果为:"+future.get());
    }

    /**
     * 并行调用多个异步任务。
     */
    private void parallelExecuteAnsyTask(ThreadPool threadPool)
            throws InterruptedException, ExecutionException {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
        tasks.add(new CallableAnsyTask(arr));
        tasks.add(new CallableAnsyTask(arr));
        tasks.add(new CallableAnsyTask(arr));

        List<Future<Long>> futures = threadPool.invokeAll(tasks, 1, TimeUnit.SECONDS);
        for (Future<Long> future : futures) {
            Long result = future.get();   // 如果某个任务执行超时，调用该任务对应的future.get时抛出CancellationException异常
            Log.e("LM" , "并行调用，其中一个任务的执行结果为:"+result);
        }

    }

    private void handleSubmitFail(ThreadPool threadPool) {
        // 队列满时，提交失败的任务直接丢弃
        threadPool.submit(new RunnableAsynTask(), "default", new DiscardFailHandler<Runnable>());

        // 队列满时，提交失败的任务信息会输出ERROR日志
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        threadPool.submit(new CallableAnsyTask(arr), "other");
    }

    @Override
    public void onClick(View v) {
        try {
            ThreadPool threadPool = APP.getTpm().getThreadPool();
            //executeRunnableAnsyTask(threadPool);
            executeCallableAnsyTask(threadPool);
            //parallelExecuteAnsyTask(threadPool);
            //handleSubmitFail(threadPool);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    class CallableAnsyTask implements Callable<Long> {

        private int[] _arr;

        public CallableAnsyTask(int[] arr) {
            _arr = arr;
        }

        @Override
        public Long call() throws Exception {
            long result = 0;
            for (int i = 0; i < _arr.length; i++) {
                result += _arr[i];
            }
            SystemClock.sleep(3000);
            return result;
        }
    }

    class RunnableAsynTask implements Runnable {

        // 耗时的操作（配置低一些的机器小心CPU 100％，反应慢）
        public void needSomeTime() {
            int len = 50000;
            String[] intArray = new String[len];
            Random random = new Random(len);

            // 初始化数组
            for (int i = 0; i < len; i++) {
                intArray[i] = String.valueOf(random.nextInt());
            }

            // 排序
            Arrays.sort(intArray);
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            needSomeTime();
            long endTime = System.currentTimeMillis();
            Log.e("LM" , "执行任务耗时：" + (endTime - startTime));
        }
    }

    @Override
    protected void onDestroy() {
        APP.destroy();
        super.onDestroy();
    }
}
