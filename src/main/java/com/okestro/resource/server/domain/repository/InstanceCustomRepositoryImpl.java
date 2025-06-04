package com.okestro.resource.server.domain.repository;

import com.okestro.resource.server.domain.InstanceEntity;
import com.okestro.resource.server.domain.QInstanceEntity;
import com.okestro.resource.support.data.repository.BaseQueryDslRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class InstanceCustomRepositoryImpl extends BaseQueryDslRepository
		implements InstanceCustomRepository {
	QInstanceEntity qInstance = QInstanceEntity.instanceEntity;

	public InstanceCustomRepositoryImpl() {
		super(InstanceEntity.class);
	}

	@Override
	public Page<InstanceEntity> search(Pageable pageable) {
		long offset = pageable.getOffset();
		int pageSize = pageable.getPageSize();
		List<Long> ids =
				getQuerydsl()
						.createQuery()
						.select(qInstance.id)
						.from(qInstance)
						.offset(offset)
						.limit(pageSize)
						.orderBy(qInstance.id.desc())
						.fetch();

		List<InstanceEntity> instances =
				getQuerydsl()
						.createQuery()
						.select(qInstance)
						.from(qInstance)
						.where(qInstance.id.in(ids))
						.orderBy(qInstance.id.desc())
						.fetch();

		Long totalCount =
				Objects.requireNonNullElse(
						getQuerydsl().createQuery().select(qInstance.count()).from(qInstance).fetchOne(), 0L);

		return new PageImpl<>(instances, pageable, totalCount);
	}
}
