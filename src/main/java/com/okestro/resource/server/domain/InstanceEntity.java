package com.okestro.resource.server.domain;

import com.okestro.resource.server.domain.enums.PowerStatus;
import com.okestro.resource.server.domain.support.InstanceAliasConverter;
import com.okestro.resource.server.domain.support.InstanceHostConverter;
import com.okestro.resource.server.domain.vo.ImageSource;
import com.okestro.resource.server.domain.vo.InstanceAlias;
import com.okestro.resource.server.domain.vo.InstanceHost;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Entity
@Table(
		name = "instances",
		uniqueConstraints = {
			@UniqueConstraint(name = "uq_instance_alias", columnNames = "alias"),
			@UniqueConstraint(
					name = "uq_source",
					columnNames = {"source_type", "source_target_id"})
		})
@EntityListeners(AuditingEntityListener.class)
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

	@Column(name = "updated_at", nullable = false, updatable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder.Default
	@Column(name = "deleted", nullable = false)
	private Boolean deleted = false;
}
