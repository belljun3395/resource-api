package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.util.Objects;
import lombok.*;

@Getter
public class NewInstance extends BaseInstance {
	private static final String ALIAS_PREFIX = "instance";

	private NewInstance(
			String name,
			String description,
			InstanceAlias alias,
			PowerStatus powerStatus,
			InstanceHost host,
			ImageSource imageSource,
			Long flavorId) {
		super(name, description, alias, powerStatus, host, imageSource, flavorId);
	}

	public static NewInstance create(
			String name,
			String description,
			String host,
			String sourceType,
			Long sourceId,
			Long flavorId) {
		description = Objects.requireNonNullElse(description, "");
		InstanceAlias instanceAlias = InstanceAlias.create(ALIAS_PREFIX);
		InstanceHost instanceHost = new InstanceHost(host);
		SourceType imageSourceType = SourceType.valueOf(sourceType.toUpperCase());
		ImageSource imageSource = ImageSource.create(imageSourceType, sourceId);
		return new NewInstance(
				name, description, instanceAlias, PowerStatus.RUNNING, instanceHost, imageSource, flavorId);
	}
}
