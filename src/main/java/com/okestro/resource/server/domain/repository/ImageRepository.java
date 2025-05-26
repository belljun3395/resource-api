package com.okestro.resource.server.domain.repository;

import com.okestro.resource.server.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {}
