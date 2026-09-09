package com.feign.test.github.service;

import com.feign.test.github.dto.RepositoryDto;
import com.feign.test.github.exception.LocalRepositoryNotFoundException;
import com.feign.test.github.client.GithubClient;
import com.feign.test.github.dto.github.GithubApiResponse;
import com.feign.test.github.mapper.RepositoryMapper;
import com.feign.test.github.model.Repository;
import com.feign.test.github.repository.RepositoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {
    private final GithubClient githubClient;
    private final RepositoryJpaRepository repositoryJpaRepository;
    private final RepositoryMapper githubRepositoryMapper;

    public RepositoryDto getRepository(String owner, String repositoryName) {
        log.info("Fetching repository with name : {}, and owner: {}", repositoryName, owner);
        return githubRepositoryMapper.toDto(githubClient.getRepository(owner, repositoryName));
    }

    @Transactional(readOnly = true)
    public RepositoryDto getLocalRepository(String owner, String repositoryName) {
        String fullName = getFullName(owner, repositoryName);
        log.info("Fetching repository with full name {}", fullName);
        Repository repository = repositoryJpaRepository.findByFullName(fullName)
                .orElseThrow(() -> new LocalRepositoryNotFoundException(String.format("Repository with full name %s was not found", fullName)));
        log.info("Repository fetched successfully");
        return githubRepositoryMapper.toDto(repository);
    }

    @Transactional
    public RepositoryDto createRepository(String owner, String repositoryName) {
        log.info("Process of saving repository [{}] in local repo started", repositoryName);
        GithubApiResponse githubApiResponse = githubClient.getRepository(owner, repositoryName);
        Repository repository = githubRepositoryMapper.toEntity(githubApiResponse);
        Repository saved = repositoryJpaRepository.save(repository);
        log.info("Process of saving repository completed successfully");
        return githubRepositoryMapper.toDto(saved);
    }

    public void updateRepository(String owner, String repositoryName) {
        log.info("Process of updating repository [{}] in local repo started", repositoryName);
        GithubApiResponse githubApiResponse = githubClient.getRepository(owner, repositoryName);
        Repository repository = githubRepositoryMapper.toEntity(githubApiResponse);
        repositoryJpaRepository.save(repository);
        log.info("Process of updating repository completed successfully");
    }

    public void deleteRepository(String owner, String repositoryName) {
        log.info("Process of deleting repository [{}] in local repo started", repositoryName);
        GithubApiResponse githubApiResponse = githubClient.getRepository(owner, repositoryName);
        Repository repository = githubRepositoryMapper.toEntity(githubApiResponse);
        repositoryJpaRepository.delete(repository);
        log.info("Process of deleting repository completed successfully");
    }

    private String getFullName(String owner, String repositoryName) {
        return owner + "/" + repositoryName;
    }
}
