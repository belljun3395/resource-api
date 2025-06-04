package com.okestro.resource.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RateLimitConfig {

	@Bean
	public LettuceBasedProxyManager<String> proxyManager(RedisConnectionFactory factory) {
		if (factory instanceof LettuceConnectionFactory lettuceFactory) {
			RedisClusterClient client = (RedisClusterClient) lettuceFactory.getRequiredNativeClient();
			StatefulRedisClusterConnection<String, byte[]> connection =
					client.connect(RedisCodec.of(new StringCodec(), new ByteArrayCodec()));
			return Bucket4jLettuce.casBasedBuilder(connection)
					.expirationAfterWrite(
							ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(
									Duration.ofSeconds(10)))
					.build();
		}
		throw new IllegalArgumentException(
				"Unsupported RedisConnectionFactory type: " + factory.getClass());
	}
}
