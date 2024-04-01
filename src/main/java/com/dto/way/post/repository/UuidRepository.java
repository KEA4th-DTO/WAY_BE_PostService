package com.dto.way.post.repository;

import com.dto.way.post.domain.common.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
