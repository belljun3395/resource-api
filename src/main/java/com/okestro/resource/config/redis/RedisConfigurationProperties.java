package com.okestro.resource.config.redis;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RedisConfigurationProperties {
	private int maxRedirects;
	private String password;
	private String connectIp;
	private List<String> nodes;
}
