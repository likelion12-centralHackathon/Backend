package com.likelion.timer.domain.Timer.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.timer.domain.Timer.domain.entity.Part;
import com.likelion.timer.domain.model.PartTypeEnum;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

	Optional<Part> findByPartType(PartTypeEnum partType);

}
