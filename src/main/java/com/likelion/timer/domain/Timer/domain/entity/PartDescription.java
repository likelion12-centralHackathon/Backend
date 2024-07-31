package com.likelion.timer.domain.Timer.domain.entity;

import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.likelion.timer.global.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class PartDescription extends BaseTime {

	@Id
	@Column(name = "part_description_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Builder
	public PartDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PartDescription partDescription))
			return false;

		return Objects.equals(this.id, partDescription.getId()) &&
			Objects.equals(this.description, partDescription.getDescription());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, description);
	}

}
