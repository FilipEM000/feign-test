package com.feign.test.github.controller;

import com.feign.test.github.dto.RepositoryDto;
import com.feign.test.github.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RepositoryController {
    private final RepositoryService repositoryService;

    @GetMapping("/repositories/{owner}/{repositoryName}")
    public RepositoryDto getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Getting repository {} for {}", repositoryName, owner);
        return repositoryService.getRepository(owner, repositoryName);
    }

    @GetMapping("/local/repositories/{owner}/{repositoryName}")
    public RepositoryDto getLocalRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Getting local repository {} for {}", repositoryName, owner);
        return repositoryService.getLocalRepository(owner, repositoryName);
    }

    @PostMapping("/repositories/{owner}/{repositoryName}")
    @ResponseStatus(HttpStatus.CREATED)
    public RepositoryDto create(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Saving repository {} in local repo", repositoryName);
        return repositoryService.createRepository(owner, repositoryName);
    }

    @PutMapping("/repositories/{owner}/{repositoryName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Updating repository {} in local repo", repositoryName);
        repositoryService.updateRepository(owner, repositoryName);
    }

    @DeleteMapping("/repositories/{owner}/{repositoryName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String owner, @PathVariable String repositoryName) {
        log.info("Deleting repository {} in local repo", repositoryName);
        repositoryService.deleteRepository(owner, repositoryName);
    }
}
