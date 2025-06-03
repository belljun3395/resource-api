package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.util.Random;

public final class InstanceFixtures {
	private Long id;
	private String name;
	private String description;
	private InstanceAlias alias;
	private PowerStatus powerStatus;
	private InstanceHost host;
	private ImageSource imageSource;
	private Long flavorId;

	private InstanceFixtures() {}

	public static InstanceFixtures anInstance() {
		return new InstanceFixtures();
	}

	public InstanceFixtures withId(Long id) {
		this.id = id;
		return this;
	}

	public InstanceFixtures withName(String name) {
		this.name = name;
		return this;
	}

	public InstanceFixtures withDescription(String description) {
		this.description = description;
		return this;
	}

	public InstanceFixtures withAlias(InstanceAlias alias) {
		this.alias = alias;
		return this;
	}

	public InstanceFixtures withPowerStatus(PowerStatus powerStatus) {
		this.powerStatus = powerStatus;
		return this;
	}

	public InstanceFixtures withHost(InstanceHost host) {
		this.host = host;
		return this;
	}

	public InstanceFixtures withImageSource(ImageSource imageSource) {
		this.imageSource = imageSource;
		return this;
	}

	public InstanceFixtures withFlavorId(Long flavorId) {
		this.flavorId = flavorId;
		return this;
	}

	public Instance build() {
		return new Instance(name, description, alias, powerStatus, host, imageSource, flavorId, id);
	}

	public static InstanceFixtures giveMeOne() {
		int id = new Random().nextInt(100);
		return anInstance()
				.withId(1L)
				.withName("test-Instance-" + id)
				.withDescription("This is a test instance " + id)
				.withAlias(InstanceAlias.create("test" + id))
				.withPowerStatus(PowerStatus.RUNNING)
				.withHost(new InstanceHost("localhost"))
				.withImageSource(ImageSource.create(SourceType.IMAGE, (long) id))
				.withFlavorId(1L);
	}
}
