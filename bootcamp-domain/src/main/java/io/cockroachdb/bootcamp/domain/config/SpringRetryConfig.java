package io.cockroachdb.bootcamp.domain.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Role;
import org.springframework.resilience.annotation.EnableResilientMethods;

import io.cockroachdb.bootcamp.common.aspect.AdvisorOrder;
import io.cockroachdb.bootcamp.common.retry.TransientExceptionClassifier;
import io.cockroachdb.bootcamp.common.retry.TransientExceptionRetryListener;

@Configuration
@EnableResilientMethods(proxyTargetClass = true, order = AdvisorOrder.TRANSACTION_RETRY_ADVISOR)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Profile("!aop-retry")
public class SpringRetryConfig {
    @Bean
    public TransientExceptionClassifier exceptionClassifier() {
        return new TransientExceptionClassifier();
    }

    @Bean
    public TransientExceptionRetryListener transientExceptionRetryListener() {
        return new TransientExceptionRetryListener();
    }
}
