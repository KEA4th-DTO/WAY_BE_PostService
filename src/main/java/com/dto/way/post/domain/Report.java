package com.dto.way.post.domain;


import com.dto.way.post.domain.common.BaseEntity;
import com.dto.way.post.domain.enums.ReportStatus;
import com.dto.way.post.domain.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(columnDefinition = "varchar(20) default 'PROCESS'")
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String memberEmail;

    private Long targetId;
}
