package com.okestro.resource.server.domain.vo;

public final class InstanceAliasFixtures {
	private String keyOrAlias;

	private InstanceAliasFixtures() {}

	public static InstanceAliasFixtures anInstanceAlias() {
		return new InstanceAliasFixtures();
	}

	public InstanceAliasFixtures withKeyOrAlias(String keyOrAlias) {
		this.keyOrAlias = keyOrAlias;
		return this;
	}

	public InstanceAlias build() {
		if (keyOrAlias != null && keyOrAlias.contains("_")) {
			return InstanceAlias.exist(keyOrAlias);
		}
		return InstanceAlias.create(keyOrAlias != null ? keyOrAlias : "test");
	}

	public static InstanceAliasFixtures giveMeOne() {
		return anInstanceAlias().withKeyOrAlias("test");
	}
}
