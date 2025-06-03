package com.okestro.resource.server.domain.model.flavor;

import com.okestro.resource.server.domain.FlavorEntity;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Flavor extends BaseFlavor {
	private final Long id;
	private final LocalDateTime createdAt;

	protected Flavor(
			String name,
			String description,
			Float vCpu,
			Float memory,
			Float rootDiskSize,
			Long id,
			LocalDateTime createdAt) {
		super(name, description, vCpu, memory, rootDiskSize);
		this.id = id;
		this.createdAt = createdAt;
	}

	public static Flavor from(FlavorEntity flavorEntity) {
		return new Flavor(
				flavorEntity.getName(),
				flavorEntity.getDescription(),
				flavorEntity.getVCpu(),
				flavorEntity.getMemory(),
				flavorEntity.getRootDiskSize(),
				flavorEntity.getId(),
				flavorEntity.getCreatedAt());
	}
}
