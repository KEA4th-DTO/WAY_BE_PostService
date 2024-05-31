package com.dto.way.post.global.utils;

import com.dto.way.post.domain.common.Uuid;
import com.dto.way.post.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@RequiredArgsConstructor
public class UuidCreator {

    private final UuidRepository uuidRepository;

    public String createUuid() {
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder()
                .uuid(uuid)
                .build());
        return savedUuid.getUuid();
    }
}
