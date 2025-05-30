package com.okestro.resource.config;

import com.okestro.resource.config.async.ClonedTaskDecorator;
import com.okestro.resource.config.async.DefaultAsyncExceptionHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {
	public static final String DEFAULT_EXECUTOR = "threadPoolTaskExecutor";

	private final DefaultAsyncExceptionHandler defaultAsyncExceptionHandler;

	@Bean(name = DEFAULT_EXECUTOR)
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(50);
		taskExecutor.setQueueCapacity(100);
		taskExecutor.setThreadNamePrefix("Executor-");
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.setAwaitTerminationSeconds(20);
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setTaskDecorator(new ClonedTaskDecorator());
		return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return defaultAsyncExceptionHandler;
	}
}
