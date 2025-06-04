package com.okestro.resource.support.web.support;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRateLimiter {
	private static final int CAPACITY = 10;
	private static final int REFILL_AMOUNT = 10;
	private static final Duration REFILL_DURATION = Duration.ofSeconds(10);

	private final LettuceBasedProxyManager<String> proxyManager;

	private Bucket resolveBucket(String apiKey) {
		BucketConfiguration configuration = createBucketConfiguration();
		return proxyManager.builder().build(apiKey, () -> configuration);
	}

	private BucketConfiguration createBucketConfiguration() {
		return BucketConfiguration.builder()
				.addLimit(
						Bandwidth.builder()
								.capacity(CAPACITY)
								.refillGreedy(REFILL_AMOUNT, REFILL_DURATION)
								.build())
				.build();
	}

	public boolean tryConsume(String apiKey) {
		Bucket bucket = resolveBucket(apiKey);
		boolean consumed = bucket.tryConsume(1);
		log.info("API Key: {}, Consumed: {}, Time: {}", apiKey, consumed, LocalDateTime.now());
		return consumed;
	}
}
