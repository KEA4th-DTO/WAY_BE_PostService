package com.dto.way.post.repository;

import com.dto.way.post.domain.Daily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<Daily, Long> {
}
