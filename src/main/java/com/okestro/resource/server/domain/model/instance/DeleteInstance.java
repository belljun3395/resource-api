package com.okestro.resource.server.domain.model.instance;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class DeleteInstance extends UpdatedInstance {
	private Boolean isDeleted = true;

	protected DeleteInstance(
			String name,
			String description,
			InstanceAlias alias,
			PowerStatus powerStatus,
			InstanceHost host,
			ImageSource imageSource,
			Long flavorId,
			Long id) {
		super(name, description, alias, powerStatus, host, imageSource, flavorId, id);
	}

	public static DeleteInstance of(ShutDownInstance instance) {
		return new DeleteInstance(
				instance.getName(),
				instance.getDescription(),
				instance.getAlias(),
				PowerStatus.SHUTDOWN,
				instance.getHost(),
				instance.getImageSource(),
				instance.getFlavorId(),
				instance.getId());
	}

	public void failToDelete() {
		this.isDeleted = false;
	}
}
