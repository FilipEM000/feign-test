package com.feing.test.github;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repositories")
public class GithubController {
    private final GithubClient githubClient;

    @GetMapping("/{owner}/{repositoryName}")
    public RepositorySummaryResponse getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        return githubClient.getRepositorySummary(owner, repositoryName);
    }
}
