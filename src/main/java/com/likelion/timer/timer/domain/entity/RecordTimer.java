package com.likelion.timer.timer.domain.entity;

import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.likelion.timer.global.entity.BaseTime;
import com.likelion.timer.timer.model.RecordTimerStateTypeEnum;
import com.likelion.timer.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class RecordTimer extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true)
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Timer timer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true)
	private Part part;

	private Long developTime;

	private Long stretchingTime;

	@Enumerated(EnumType.STRING)
	private RecordTimerStateTypeEnum timerStateType;

	@Builder
	public RecordTimer(@NotNull User user, Timer timer,
		Part part, Long developTime, Long stretchingTime,
		RecordTimerStateTypeEnum timerStateType) {
		this.user = user;
		this.timer = timer;
		this.developTime = developTime;
		this.stretchingTime = stretchingTime;
		this.timerStateType = timerStateType;
		this.part = part;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RecordTimer recordTimer))
			return false;

		return Objects.equals(this.id, recordTimer.getId()) &&
			Objects.equals(this.user, recordTimer.getUser()) &&
			Objects.equals(this.part, recordTimer.getPart()) &&
			Objects.equals(this.getRegTime(), recordTimer.getRegTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, user, part, getRegTime());
	}
}