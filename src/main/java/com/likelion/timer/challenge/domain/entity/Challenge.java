package com.likelion.timer.challenge.domain.entity;

import java.util.Date;

import com.likelion.timer.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "challenges")
@Getter
@Setter
public class Challenge {

	@Id
	@Column(name = "challenge_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false, length = 50)
	private String authMethod;

	@Column(columnDefinition = "TEXT")
	private String note;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Column(nullable = false)
	private int viewCount = 0;

	@Column(nullable = false)
	private int participantCount = 0;

	@Column(length = 255)
	private String imgUrl;

}