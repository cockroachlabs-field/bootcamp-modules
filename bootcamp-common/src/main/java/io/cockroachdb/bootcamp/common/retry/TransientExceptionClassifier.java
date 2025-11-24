package io.cockroachdb.bootcamp.common.retry;

import java.lang.reflect.Method;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.resilience.retry.MethodRetryPredicate;

public class TransientExceptionClassifier implements MethodRetryPredicate {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SERIALIZATION_FAILURE = "40001";

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean shouldRetry(Method method, Throwable ex) {
        if (!isEnabled() || ex == null) {
            return false;
        }
        Throwable throwable = NestedExceptionUtils.getMostSpecificCause(ex);
        if (throwable instanceof SQLException) {
            return shouldRetry((SQLException) throwable);
        }
        logger.warn("Non-transient exception {}", ex.getClass());
        return false;
    }

    public boolean shouldRetry(SQLException ex) {
        if (SERIALIZATION_FAILURE.equals(ex.getSQLState())) {
            logger.warn("Transient SQL exception detected : sql state [{}], message [{}]",
                    ex.getSQLState(), ex.toString());
            return true;
        }
        return false;
    }
}
