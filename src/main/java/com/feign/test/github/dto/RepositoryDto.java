package com.feign.test.github.dto;

import java.time.LocalDateTime;

public record RepositoryDto(
        String fullName,
        String description,
        String cloneUrl,
        Integer stars,
        LocalDateTime createdAt
) {
}
