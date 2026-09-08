package com.feing.test.github.controller;

import com.feing.test.github.dto.RepositorySummaryResponse;
import com.feing.test.github.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RepositoryController {
    private final RepositoryService repositoryService;

    @GetMapping("/repositories/{owner}/{repositoryName}")
    public RepositorySummaryResponse getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryService.getRepositorySummary(owner, repositoryName);
    }

    @GetMapping("/local/repositories/{owner}/{repositoryName}")
    public RepositorySummaryResponse getLocalRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryService.getLocalRepository(owner, repositoryName);
    }

    @PostMapping("/repositories/{owner}/{repositoryName}")
    public RepositorySummaryResponse create(@PathVariable String owner, @PathVariable String repositoryName) {
        return repositoryService.createRepository(owner, repositoryName);
    }
}
