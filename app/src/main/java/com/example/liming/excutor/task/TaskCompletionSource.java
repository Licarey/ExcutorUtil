package com.example.liming.excutor.task;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class TaskCompletionSource<TResult> {

    private final Task<TResult> task;

    /**
     * Creates a TaskCompletionSource that orchestrates a Task. This allows the creator of a task to
     * be solely responsible for its completion.
     */
    public TaskCompletionSource() {
        task = new Task<>();
    }

    /**
     * @return the Task associated with this TaskCompletionSource.
     */
    public Task<TResult> getTask() {
        return task;
    }

    /**
     * Sets the cancelled flag on the Task if the Task hasn't already been completed.
     */
    public boolean trySetCancelled() {
        return task.trySetCancelled();
    }

    /**
     * Sets the result on the Task if the Task hasn't already been completed.
     */
    public boolean trySetResult(TResult result) {
        return task.trySetResult(result);
    }

    /**
     * Sets the error on the Task if the Task hasn't already been completed.
     */
    public boolean trySetError(Exception error) {
        return task.trySetError(error);
    }

    /**
     * Sets the cancelled flag on the task, throwing if the Task has already been completed.
     */
    public void setCancelled() {
        if (!trySetCancelled()) {
            throw new IllegalStateException("Cannot cancel a completed task.");
        }
    }

    /**
     * Sets the result of the Task, throwing if the Task has already been completed.
     */
    public void setResult(TResult result) {
        if (!trySetResult(result)) {
            throw new IllegalStateException("Cannot set the result of a completed task.");
        }
    }

    /**
     * Sets the error of the Task, throwing if the Task has already been completed.
     */
    public void setError(Exception error) {
        if (!trySetError(error)) {
            throw new IllegalStateException("Cannot set the error on a completed task.");
        }
    }
}