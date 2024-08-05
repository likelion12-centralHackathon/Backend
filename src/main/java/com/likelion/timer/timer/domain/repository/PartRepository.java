package com.likelion.timer.timer.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.timer.timer.domain.entity.Part;
import com.likelion.timer.timer.model.PartTypeEnum;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

	Optional<Part> findByPartType(PartTypeEnum partTypeEnum);
}
