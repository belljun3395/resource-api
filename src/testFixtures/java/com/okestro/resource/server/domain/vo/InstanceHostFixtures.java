package com.okestro.resource.server.domain.vo;

public final class InstanceHostFixtures {
	private String value;

	private InstanceHostFixtures() {}

	public static InstanceHostFixtures anInstanceHost() {
		return new InstanceHostFixtures();
	}

	public InstanceHostFixtures withValue(String value) {
		this.value = value;
		return this;
	}

	public InstanceHost build() {
		return new InstanceHost(value != null ? value : "localhost");
	}

	public static InstanceHostFixtures giveMeOne() {
		return anInstanceHost().withValue("localhost");
	}
}
