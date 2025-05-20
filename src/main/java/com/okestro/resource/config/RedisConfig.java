package com.okestro.resource.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okestro.resource.config.redis.RedisConfigurationProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DnsResolvers;
import io.lettuce.core.resource.MappingSocketAddressResolver;
import java.time.Duration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableAutoConfiguration(
		exclude = {RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class})
public class RedisConfig {
	@Bean
	@ConfigurationProperties(prefix = "spring.data.redis.cluster")
	public RedisConfigurationProperties getRedisConfigurationProperties() {
		return new RedisConfigurationProperties();
	}

	@Bean
	public RedisSerializer<Object> redisSerializer(ObjectMapper objectMapper) {
		return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory(RedisConfigurationProperties rcp) {
		RedisClusterConfiguration redisClusterConfiguration =
				new RedisClusterConfiguration(rcp.getNodes());
		redisClusterConfiguration.setPassword(rcp.getPassword());
		redisClusterConfiguration.setMaxRedirects(rcp.getMaxRedirects());

		SocketOptions socketOptions =
				SocketOptions.builder().connectTimeout(Duration.ofMillis(100L)).keepAlive(true).build();

		ClusterTopologyRefreshOptions clusterTopologyRefreshOptions =
				ClusterTopologyRefreshOptions.builder()
						.enableAllAdaptiveRefreshTriggers()
						.enablePeriodicRefresh(Duration.ofHours(1L))
						.build();

		ClientOptions clientOptions =
				ClusterClientOptions.builder()
						.topologyRefreshOptions(clusterTopologyRefreshOptions)
						.socketOptions(socketOptions)
						.build();

		MappingSocketAddressResolver resolver =
				MappingSocketAddressResolver.create(
						DnsResolvers.UNRESOLVED,
						hostAndPort -> HostAndPort.of(rcp.getConnectIp(), hostAndPort.getPort()));
		ClientResources clientResources =
				ClientResources.builder().socketAddressResolver(resolver).build();

		LettuceClientConfiguration clientConfiguration =
				LettuceClientConfiguration.builder()
						.clientOptions(clientOptions)
						.clientResources(clientResources)
						.readFrom(ReadFrom.REPLICA_PREFERRED)
						.build();
		return new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(
			RedisConnectionFactory factory, RedisSerializer<Object> serializer) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(serializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(serializer);
		return template;
	}
}
