package io.cockroachdb.bootcamp.common.retry;

import java.util.concurrent.atomic.AtomicInteger;

import org.jspecify.annotations.Nullable;
import org.springframework.core.retry.RetryListener;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.Retryable;

public class TransientExceptionRetryListener implements RetryListener {
    private final AtomicInteger error = new AtomicInteger();

    private final AtomicInteger success = new AtomicInteger();

    public void clear() {
        error.set(0);
        success.set(0);
    }

    public int getError() {
        return error.get();
    }

    public int getSuccess() {
        return success.get();
    }

    @Override
    public void onRetrySuccess(RetryPolicy retryPolicy, Retryable<?> retryable, @Nullable Object result) {
        success.incrementAndGet();
    }

    @Override
    public void onRetryFailure(RetryPolicy retryPolicy, Retryable<?> retryable, Throwable throwable) {
        error.incrementAndGet();
    }
}
