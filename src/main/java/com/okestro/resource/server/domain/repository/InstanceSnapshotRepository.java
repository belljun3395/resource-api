package com.okestro.resource.server.domain.repository;

import com.okestro.resource.server.domain.InstanceSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceSnapshotRepository extends JpaRepository<InstanceSnapshotEntity, Long> {}
