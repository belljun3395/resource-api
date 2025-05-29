package com.okestro.resource.server.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Entity
@Table(name = "instance_snapshots")
@EntityListeners(AuditingEntityListener.class)
public class InstanceSnapshotEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NonNull
	@Column(name = "instance_id", nullable = false)
	private Long instanceId;

	@NonNull
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass =
				o instanceof HibernateProxy
						? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
						: o.getClass();
		Class<?> thisEffectiveClass =
				this instanceof HibernateProxy
						? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
						: this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		InstanceSnapshotEntity that = (InstanceSnapshotEntity) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
				: getClass().hashCode();
	}
}
