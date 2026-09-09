package com.feign.test.github.client;

import com.feign.test.github.dto.github.GithubApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GithubClient {

    @GetMapping("/repos/{owner}/{repositoryName}")
    GithubApiResponse getRepository(@PathVariable("owner") String owner, @PathVariable("repositoryName") String repositoryName);
}
