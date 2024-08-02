package com.likelion.timer.domain.User.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface T_UserRepository extends JpaRepository<T_User, Long> {
}
