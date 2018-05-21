package com.example.liming.excutor.exception;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class ExecutorException extends RuntimeException {
    public ExecutorException(Exception e) {
        super("An exception was thrown by an Executor", e);
    }
}
