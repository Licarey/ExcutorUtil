package com.example.liming.excutor;

import com.example.liming.excutor.task.Task;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public interface Continuation<TTaskResult, TContinuationResult> {
    TContinuationResult then(Task<TTaskResult> task) throws Exception;
}
