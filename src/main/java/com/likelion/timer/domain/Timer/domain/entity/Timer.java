package com.likelion.timer.domain.Timer.domain.entity;

import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.likelion.timer.domain.User.domain.T_User;
import com.likelion.timer.domain.model.TimerStateTypeEnum;
import com.likelion.timer.global.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private T_User user;

	private Float cycle;

	private Boolean isPermanent;

	private Boolean isSettingByUser;

	@Enumerated(EnumType.STRING)
	private TimerStateTypeEnum timerState;

	@OneToMany(fetch = FetchType.EAGER)
	private List<PartList> partLists;

	@Builder
	public Timer(@NotNull T_User user, Float cycle, Boolean isPermanent, Boolean isSettingByUser,
		List<PartList> partLists) {
		this.user = user;
		this.cycle = cycle;
		this.timerState = TimerStateTypeEnum.TIMER_NOT_START;
		this.isPermanent = isPermanent;
		this.isSettingByUser = isSettingByUser;
		this.partLists = partLists;
	}

	public void changedTimerState(TimerStateTypeEnum timerState) {
		this.timerState = timerState;
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
