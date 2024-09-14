package com.likelion.timer.challenge.DTO;

import java.util.Date;

import com.likelion.timer.challenge.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
public class ChallengeOverviewDto {
	private Long id;
	private String title;
	private String imgUrl;
	private int participantCount;
	private Date endDate;
	private String authMethod;
	private String note;
	private Category category;
	private int viewCount;

	public ChallengeOverviewDto(Long id, String title, String imgUrl, int participantCount, Date endDate, String authMethod, String note, Category category, int viewCount) {
		this.id = id;
		this.title = title;
		this.imgUrl = imgUrl;
		this.participantCount = participantCount;
		this.endDate = endDate;
		this.authMethod = authMethod;
		this.note = note;
		this.category = category;
		this.viewCount = viewCount;
	}
}
