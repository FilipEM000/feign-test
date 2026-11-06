package com.feign.test.github.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record GithubApiResponse(
        @JsonProperty("full_name") String fullName,
        String description,
        @JsonProperty("clone_url") String cloneUrl,
        @JsonProperty("stargazers_count") Integer stars,
        @JsonProperty("created_at") LocalDateTime createdAt
) {
}
