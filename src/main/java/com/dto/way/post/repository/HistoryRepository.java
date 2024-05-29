package com.dto.way.post.repository;

import com.dto.way.post.domain.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    Optional<History> findById(Long Id);

    Long countByPostMemberId(Long memberId);

    Page<History> findByTitleContaining(PageRequest pageRequest, String title);

    Page<History> findByBodyContaining(PageRequest pageRequest, String body);

}
