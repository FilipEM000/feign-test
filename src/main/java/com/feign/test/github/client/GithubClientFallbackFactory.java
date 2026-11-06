package com.feign.test.github.client;

import com.feign.test.github.dto.github.GithubApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubClientFallbackFactory implements FallbackFactory<GithubClient> {

    @Override
    public GithubClient create(Throwable cause) {
        log.error("An exception occurred when calling the UserSessionClient", cause);
        return new GithubClient() {
            @Override
            public GithubApiResponse getRepository(String owner, String repositoryName) {
                return new GithubApiResponse(
                        owner + "/" + repositoryName + " (FALLBACK)",
                        "Data temporarily unavailable due to an external service failure.",
                        "", 0, null
                );
            }
        };
    }
}
