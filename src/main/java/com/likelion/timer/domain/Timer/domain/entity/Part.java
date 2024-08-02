package com.likelion.timer.domain.Timer.domain.entity;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.likelion.timer.domain.model.PartTypeEnum;
import com.likelion.timer.global.entity.BaseTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Part extends BaseTime {

	@Id
	@Column(name = "part_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private PartTypeEnum partType;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PartImgUrl> imgUrls;

	@ManyToMany(mappedBy = "parts")
	private List<PartList> partLists;

	@Builder
	public Part(PartTypeEnum partType, List<PartImgUrl> imgUrls) {
		this.partType = partType;
		this.imgUrls = imgUrls;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Part part))
			return false;
		return Objects.equals(this.id, part.getId()) && Objects.equals(this.partType, part.getPartType());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, partType);
	}

}
