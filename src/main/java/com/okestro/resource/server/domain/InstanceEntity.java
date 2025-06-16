package com.okestro.resource.server.domain;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.model.instance.DeleteInstance;
import com.okestro.resource.server.domain.model.instance.NewInstance;
import com.okestro.resource.server.domain.model.instance.UpdatedInstance;
import com.okestro.resource.server.domain.support.InstanceAliasConverter;
import com.okestro.resource.server.domain.support.InstanceHostConverter;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Entity
@Table(
		name = "instances",
		uniqueConstraints = {@UniqueConstraint(name = "uq_instance_alias", columnNames = "alias")})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update  instances set deleted = true where id = ?")
public class InstanceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NonNull
	@Column(name = "name", nullable = false)
	private String name;

	@Builder.Default
	@Column(name = "description", nullable = false)
	private String description = "";

	@NonNull
	@Convert(converter = InstanceAliasConverter.class)
	@Column(name = "alias", nullable = false, unique = true)
	private InstanceAlias alias;

	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(name = "power_status", nullable = false)
	private PowerStatus powerStatus;

	@NonNull
	@Convert(converter = InstanceHostConverter.class)
	@Column(name = "host", nullable = false)
	private InstanceHost host;

	@NonNull
	@Column(name = "flavor_id", nullable = false)
	private Long flavorId;

	@NonNull @Embedded private ImageSource imageSource;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder.Default
	@Column(name = "deleted", nullable = false)
	private Boolean deleted = false;

	public static InstanceEntity createNew(NewInstance newInstance) {
		return InstanceEntity.builder()
				.name(newInstance.getName())
				.description(newInstance.getDescription())
				.alias(newInstance.getAlias())
				.powerStatus(PowerStatus.RUNNING)
				.host(newInstance.getHost())
				.flavorId(newInstance.getFlavorId())
				.imageSource(newInstance.getImageSource())
				.build();
	}

	public static InstanceEntity updateTo(UpdatedInstance instance) {
		return InstanceEntity.builder()
				.id(instance.getId())
				.name(instance.getName())
				.description(instance.getDescription())
				.alias(instance.getAlias())
				.powerStatus(instance.getPowerStatus())
				.host(instance.getHost())
				.flavorId(instance.getFlavorId())
				.imageSource(instance.getImageSource())
				.build();
	}

	public static InstanceEntity updateTo(DeleteInstance instance) {
		return InstanceEntity.builder()
				.id(instance.getId())
				.name(instance.getName())
				.description(instance.getDescription())
				.alias(instance.getAlias())
				.powerStatus(instance.getPowerStatus())
				.host(instance.getHost())
				.flavorId(instance.getFlavorId())
				.imageSource(instance.getImageSource())
				.deleted(instance.getIsDeleted())
				.build();
	}

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
		InstanceEntity instance = (InstanceEntity) o;
		return getId() != null && Objects.equals(getId(), instance.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
				: getClass().hashCode();
	}
}
