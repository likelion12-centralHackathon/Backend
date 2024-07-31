package com.likelion.timer.domain.Timer.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.timer.domain.Timer.domain.entity.Part;
import com.likelion.timer.domain.Timer.domain.entity.PartList;

@Repository
public interface PartListRepository extends JpaRepository<PartList, Long> {

	Optional<PartList> findByParts(List<Part> parts);
}
