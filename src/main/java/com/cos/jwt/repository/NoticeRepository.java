package com.cos.jwt.repository;

import com.cos.jwt.model.Notice;
import com.cos.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Optional<Notice> findById(Long id);


}
