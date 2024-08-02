package com.likelion.timer.user.model;

import com.likelion.timer.domain.Timer.domain.entity.Part;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BodyPartSatisfaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Part part;

	private int satisfactionScore; // 부위에 대한 만족도 점수
	private String comment;

	public BodyPartSatisfaction(Long id, Part part, int satisfactionScore, String comment) {
		this.id = id;
		this.part = part;
		this.satisfactionScore = satisfactionScore;
		this.comment = comment;
	}
}




