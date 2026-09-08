package com.feing.test.github.service;

import com.feing.test.github.client.GithubClient;
import com.feing.test.github.dto.RepositorySummaryResponse;
import com.feing.test.github.dto.github.GithubApiResponse;
import com.feing.test.github.exception.LocalRepositoryNotFoundException;
import com.feing.test.github.mapper.RepositoryMapper;
import com.feing.test.github.model.Repository;
import com.feing.test.github.repository.RepositoryJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoryServiceTest {
    RepositoryService repositoryService;
    GithubClient githubClient;
    RepositoryJpaRepository repositoryJpaRepository;
    RepositoryMapper repositoryMapper;

    @BeforeEach
    void setup() {
        this.githubClient = Mockito.mock(GithubClient.class);
        this.repositoryJpaRepository = Mockito.mock(RepositoryJpaRepository.class);
        this.repositoryMapper = Mappers.getMapper(RepositoryMapper.class);
        this.repositoryService = new RepositoryService(githubClient, repositoryJpaRepository, repositoryMapper);
    }

    @Test
    void getLocalRepository_dataCorrect_repositoryReturned() {
        Repository repository = new Repository(0L, "test_name", "test_description", "test_cloneurl", 0, "test_createdat");
        when(repositoryJpaRepository.findByFullName(any())).thenReturn(Optional.of(repository));

        RepositorySummaryResponse result = repositoryService.getLocalRepository("owner", "repositoryName");

        Assertions.assertAll(
                () -> Assertions.assertEquals("test_name", result.fullName()),
                () -> Assertions.assertEquals("test_description", result.description()),
                () -> Assertions.assertEquals("test_cloneurl", result.cloneUrl()),
                () -> Assertions.assertEquals(0, result.stars()),
                () -> Assertions.assertEquals("test_createdat", result.createdAt()),
                () -> verify(repositoryJpaRepository).findByFullName("owner/repositoryName")
        );
    }

    @Test
    void getLocalRepository_repositoryNotFound_exceptionThrown() {
        when(repositoryJpaRepository.findByFullName(any())).thenReturn(Optional.empty());

        Assertions.assertAll(
                () -> assertThatExceptionOfType(LocalRepositoryNotFoundException.class)
                        .isThrownBy(() -> repositoryService.getLocalRepository("repository", "not_found"))
                        .extracting(LocalRepositoryNotFoundException::getMessage)
                        .isEqualTo("Repository with full name repository/not_found was not found"),
                () -> verify(repositoryJpaRepository).findByFullName("repository/not_found")
        );
    }

    @Test
    void createRepository_dataCorrect_repositoryCreatedAndReturned() {
        GithubApiResponse githubResponse = new GithubApiResponse("test_name", "test_description", "test_cloneurl", 0, "test_createdat");
        when(githubClient.getRepositorySummary(any(), any())).thenReturn(githubResponse);
        Repository savedRepository = new Repository(1L, "test_name", "test_description", "test_cloneurl", 0, "test_createdat");
        when(repositoryJpaRepository.save(any(Repository.class))).thenReturn(savedRepository);

        RepositorySummaryResponse result = repositoryService.createRepository("owner", "repositoryName");

        Assertions.assertAll(
                () -> Assertions.assertEquals("test_name", result.fullName()),
                () -> Assertions.assertEquals("test_description", result.description()),
                () -> Assertions.assertEquals("test_cloneurl", result.cloneUrl()),
                () -> Assertions.assertEquals(0, result.stars()),
                () -> Assertions.assertEquals("test_createdat", result.createdAt()),
                () -> verify(githubClient).getRepositorySummary("owner", "repositoryName"),
                () -> verify(repositoryJpaRepository).save(any(Repository.class))
        );
    }
}
