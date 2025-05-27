package com.okestro.resource.server.support.json;

import lombok.Getter;

@Getter
public abstract class ServerJson {
	private final String json;

	public ServerJson(String json) {
		this.json = json;
	}
}
