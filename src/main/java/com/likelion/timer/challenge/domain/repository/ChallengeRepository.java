package com.likelion.timer.challenge.domain.repository;

import java.util.List;

import com.likelion.timer.challenge.DTO.Challengetop3DTO;
import com.likelion.timer.challenge.domain.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.timer.challenge.domain.entity.Challenge;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
	List<Challenge> findByCategory(Category category);

	@Query("SELECT new com.likelion.timer.challenge.DTO.Challengetop3DTO(c.id, c.title, c.content, c.viewCount) " +
			"FROM Challenge c ORDER BY c.viewCount DESC")
	List<Challengetop3DTO> findTopByOrderByViewCountDesc(Pageable pageable);

}
