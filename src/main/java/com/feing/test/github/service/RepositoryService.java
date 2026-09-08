package com.feing.test.github.service;

import com.feing.test.github.exception.LocalRepositoryNotFoundException;
import com.feing.test.github.client.GithubClient;
import com.feing.test.github.dto.RepositorySummaryResponse;
import com.feing.test.github.dto.github.GithubApiResponse;
import com.feing.test.github.mapper.RepositoryMapper;
import com.feing.test.github.model.Repository;
import com.feing.test.github.repository.RepositoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryService {
    private final GithubClient githubClient;
    private final RepositoryJpaRepository repositoryJpaRepository;
    private final RepositoryMapper githubRepositoryMapper;

    public RepositorySummaryResponse getRepositorySummary(String owner, String repositoryName) {
        return githubRepositoryMapper.toResponseFromGithubResponse(githubClient.getRepositorySummary(owner, repositoryName));
    }

    public RepositorySummaryResponse getLocalRepository(String owner, String repositoryName) {
        String fullName = owner + "/" + repositoryName;
        Repository repository = repositoryJpaRepository.findByFullName(fullName)
                .orElseThrow(() -> new LocalRepositoryNotFoundException(String.format("Repository with full name %s was not found", fullName)));
        return githubRepositoryMapper.toResponse(repository);
    }

    public RepositorySummaryResponse createRepository(String owner, String repositoryName) {
        GithubApiResponse githubApiResponse = githubClient.getRepositorySummary(owner, repositoryName);
        Repository repository = githubRepositoryMapper.toEntityFromGithubResponse(githubApiResponse);
        Repository saved = repositoryJpaRepository.save(repository);
        return githubRepositoryMapper.toResponse(saved);
    }
}
