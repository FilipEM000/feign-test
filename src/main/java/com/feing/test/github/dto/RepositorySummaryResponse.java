package com.feing.test.github.dto;

public record RepositorySummaryResponse(
        String fullName,
        String description,
        String cloneUrl,
        Integer stars,
        String createdAt
) {
}
