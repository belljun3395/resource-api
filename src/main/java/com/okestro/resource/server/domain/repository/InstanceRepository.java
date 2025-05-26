package com.okestro.resource.server.domain.repository;

import com.okestro.resource.server.domain.InstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceRepository extends JpaRepository<InstanceEntity, Long> {}
