package com.likelion.timer.timer.domain.entity;

import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.likelion.timer.global.entity.BaseTime;
import com.likelion.timer.timer.model.TimerStateTypeEnum;
import com.likelion.timer.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Timer extends BaseTime {
	@Id
	@Column(name = "timer_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private String name;

	private Long cycle;

	private Boolean isPermanent;

	private Boolean isSettingByUser;

	@Enumerated(EnumType.STRING)
	private TimerStateTypeEnum timerState;

	@ManyToMany
	private List<PartList> partLists;

	private String deviceToken;

	@Builder
	public Timer(@NotNull User user, String deviceToken, String name, Long cycle, Boolean isPermanent,
		Boolean isSettingByUser,
		List<PartList> partLists) {
		this.user = user;
		this.name = name;
		this.cycle = cycle;
		this.deviceToken = deviceToken;
		this.timerState = TimerStateTypeEnum.TIMER_NOT_RUN;
		this.isPermanent = isPermanent;
		this.isSettingByUser = isSettingByUser;
		this.partLists = partLists;
	}

	public void changedTimerState(TimerStateTypeEnum timerState) {
		this.timerState = timerState;
	}

	public void updateTimer(String name, Long cycle, Boolean isSettingByUser, List<PartList> partLists) {
		this.name = name;
		this.cycle = cycle;
		this.isSettingByUser = isSettingByUser;
		this.partLists = partLists;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Timer timer))
			return false;

		return Objects.equals(this.id, timer.getId()) &&
			Objects.equals(this.user, timer.getUser());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, user);
	}

}
