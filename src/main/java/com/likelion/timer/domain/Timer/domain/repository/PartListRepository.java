package com.likelion.timer.domain.Timer.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.likelion.timer.domain.Timer.domain.entity.Part;
import com.likelion.timer.domain.Timer.domain.entity.PartList;

@Repository
public interface PartListRepository extends JpaRepository<PartList, Long> {

	@Query(
		"SELECT pl FROM PartList pl JOIN pl.parts p "
			+ "WHERE p IN :parts GROUP BY pl "
			+ "HAVING COUNT(p) = :size")
	Optional<PartList> findByParts(@Param("parts") List<Part> parts, @Param("size") long size);

}