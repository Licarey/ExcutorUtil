package com.example.liming.excutor.exception;

import com.example.liming.excutor.task.Task;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class UnobservedErrorNotifier {
    private Task<?> task;

    public UnobservedErrorNotifier(Task<?> task) {
        this.task = task;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            Task faultedTask = this.task;
            if (faultedTask != null) {
                Task.UnobservedExceptionHandler ueh = Task.getUnobservedExceptionHandler();
                if (ueh != null) {
                    ueh.unobservedException(faultedTask, new UnobservedTaskException(faultedTask.getError()));
                }
            }
        } finally {
            super.finalize();
        }
    }

    public void setObserved() {
        task = null;
    }

}
