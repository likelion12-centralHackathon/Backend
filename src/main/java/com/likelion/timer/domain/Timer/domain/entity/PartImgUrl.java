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
public class PartImgUrl extends BaseTime {

	@Id
	@Column(name = "part_img_url_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imageUrl;

	@Builder
	public PartImgUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PartImgUrl partImgUrl))
			return false;

		return Objects.equals(this.id, partImgUrl.getId()) &&
			Objects.equals(this.imageUrl, partImgUrl.getImageUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, imageUrl);
	}

}
