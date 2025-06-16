package com.okestro.resource.server.application.service.power;

import com.okestro.resource.server.controller.request.InstancePowerStatusAction;
import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import com.okestro.resource.server.domain.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RebootInstancePowerActionService extends InstancePowerActionService {
	private final InstanceRepository instanceRepository;

	@Override
	protected PowerStatus getCurrentPowerStatus(Long instanceId) {
		return instanceRepository
				.findById(instanceId)
				.orElseThrow(
						() -> new IllegalArgumentException("Instance not found with id: " + instanceId))
				.getPowerStatus();
	}

	@Override
	protected UpdatedInstance updateInstance(Long instanceId, PowerStatus powerStatus) {
		Instance instance =
				Instance.from(
						instanceRepository
								.findById(instanceId)
								.orElseThrow(
										() ->
												new IllegalArgumentException("Instance not found with id: " + instanceId)));
		UpdatedInstance shutdownInstance =
				UpdatedInstance.from(instanceRepository.save(InstanceEntity.updateTo(instance.shutdown())));
		// TODO: do something for reboot
		UpdatedInstance startedInstance = shutdownInstance.start();
		return UpdatedInstance.from(instanceRepository.save(InstanceEntity.updateTo(startedInstance)));
	}

	@Override
	public InstancePowerStatusAction supportAction() {
		return InstancePowerStatusAction.REBOOT;
	}
}
