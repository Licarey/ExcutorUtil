package com.example.liming.excutor.token;

import java.io.Closeable;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class CancellationTokenRegistration implements Closeable {
    private final Object lock = new Object();
    private CancellationTokenSource tokenSource;
    private Runnable action;
    private boolean closed;

    /* package */ CancellationTokenRegistration(CancellationTokenSource tokenSource, Runnable action) {
        this.tokenSource = tokenSource;
        this.action = action;
    }

    /**
     * Unregisters the callback runnable from the cancellation token.
     */
    @Override
    public void close() {
        synchronized (lock) {
            if (closed) {
                return;
            }

            closed = true;
            tokenSource.unregister(this);
            tokenSource = null;
            action = null;
        }
    }

    /* package */ void runAction() {
        synchronized (lock) {
            throwIfClosed();
            action.run();
            close();
        }
    }

    private void throwIfClosed() {
        if (closed) {
            throw new IllegalStateException("Object already closed");
        }
    }
}
