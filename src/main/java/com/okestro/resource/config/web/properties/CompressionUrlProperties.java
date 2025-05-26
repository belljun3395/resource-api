package com.okestro.resource.config.web.properties;

import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class CompressionUrlProperties {

	private boolean enabled = false;
	private List<String> patterns = Collections.emptyList();
	private int minResponseSize = 1024;
	private List<String> mimeTypes = List.of("application/json", "application/*+json");
}
