package com.okestro.resource.server.domain.repository;

import com.okestro.resource.server.domain.InstanceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstanceCustomRepository {
	Page<InstanceEntity> search(Pageable pageable);
}
