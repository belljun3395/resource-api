package com.okestro.resource.server.domain.model.instance;

import static org.junit.jupiter.api.Assertions.*;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.enums.SourceType;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class InstanceTest {

	@Test
	void start_instance() {
		LocalDateTime createdDate = LocalDateTime.now().minusMinutes(10);
		Instance instance = InstanceFixtures.giveMeOne()
				.build();

		UpdatedInstance updatedInstance = instance.start();

		assertNotNull(updatedInstance);
		assertSame(PowerStatus.RUNNING, updatedInstance.getPowerStatus());
	}

	@Test
	void stop_instance() {
		Instance instance = InstanceFixtures.giveMeOne().build();

		UpdatedInstance updatedInstance = instance.shutdown();

		assertNotNull(updatedInstance);
		assertSame(PowerStatus.SHUTDOWN, updatedInstance.getPowerStatus());
	}

	@Test
	void pause_instance() {
		Instance instance = InstanceFixtures.giveMeOne().build();

		UpdatedInstance updatedInstance = instance.pause();

		assertNotNull(updatedInstance);
		assertSame(PowerStatus.PAUSED, updatedInstance.getPowerStatus());
	}

	@Test
	void shutdown_instance() {
		Instance instance = InstanceFixtures.giveMeOne().build();

		UpdatedInstance updatedInstance = instance.shutdown();

		assertNotNull(updatedInstance);
		assertSame(PowerStatus.SHUTDOWN, updatedInstance.getPowerStatus());
	}
}
