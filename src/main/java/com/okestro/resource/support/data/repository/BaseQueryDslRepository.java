package com.okestro.resource.support.data.repository;

import com.okestro.resource.config.JpaDataSourceConfig;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Objects;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public abstract class BaseQueryDslRepository extends QuerydslRepositorySupport {

	public BaseQueryDslRepository(Class<?> domainClass) {
		super(domainClass);
	}

	@Nonnull
	@Override
	public Querydsl getQuerydsl() {
		return Objects.requireNonNull(super.getQuerydsl());
	}

	@Nonnull
	@Override
	public EntityManager getEntityManager() {
		return Objects.requireNonNull(super.getEntityManager());
	}

	@Override
	@PersistenceContext(unitName = JpaDataSourceConfig.PERSIST_UNIT)
	public void setEntityManager(@Nonnull EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}
}
