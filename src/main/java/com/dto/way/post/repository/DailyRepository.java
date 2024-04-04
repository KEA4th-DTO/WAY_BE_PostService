package com.dto.way.post.repository;

import com.dto.way.post.domain.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DailyRepository extends JpaRepository<Daily, Long> {

    Optional<Daily> findById(Long postId);
}
