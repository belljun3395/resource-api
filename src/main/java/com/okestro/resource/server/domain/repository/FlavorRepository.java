package com.okestro.resource.server.domain.repository;

import com.okestro.resource.server.domain.FlavorEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlavorRepository extends JpaRepository<FlavorEntity, Long> {
	List<FlavorEntity> findAllByIdIn(List<Long> ids);
}
