package com.dto.way.post.repository;

import com.dto.way.post.domain.Report;
import com.dto.way.post.domain.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatus(ReportStatus status);

}
