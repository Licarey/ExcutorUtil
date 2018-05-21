package com.example.liming.excutor.token;

import java.util.Locale;
import java.util.concurrent.CancellationException;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class CancellationToken {
    private final CancellationTokenSource tokenSource;

    /* package */ CancellationToken(CancellationTokenSource tokenSource) {
        this.tokenSource = tokenSource;
    }

    /**
     * @return {@code true} if the cancellation was requested from the source, {@code false} otherwise.
     */
    public boolean isCancellationRequested() {
        return tokenSource.isCancellationRequested();
    }

    /**
     * Registers a runnable that will be called when this CancellationToken is canceled.
     * If this token is already in the canceled state, the runnable will be run immediately and synchronously.
     * @param action the runnable to be run when the token is cancelled.
     * @return a {@link CancellationTokenRegistration} instance that can be used to unregister
     * the action.
     */
    public CancellationTokenRegistration register(Runnable action) {
        return tokenSource.register(action);
    }

    /**
     * @throws CancellationException if this token has had cancellation requested.
     * May be used to stop execution of a thread or runnable.
     */
    public void throwIfCancellationRequested() throws CancellationException {
        tokenSource.throwIfCancellationRequested();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s@%s[cancellationRequested=%s]",
                getClass().getName(),
                Integer.toHexString(hashCode()),
                Boolean.toString(tokenSource.isCancellationRequested()));
    }
}
