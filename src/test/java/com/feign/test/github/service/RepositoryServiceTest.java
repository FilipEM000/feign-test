package com.feign.test.github.service;

import com.feign.test.github.client.GithubClient;
import com.feign.test.github.dto.RepositoryDto;
import com.feign.test.github.dto.github.GithubApiResponse;
import com.feign.test.github.exception.LocalRepositoryNotFoundException;
import com.feign.test.github.mapper.RepositoryMapper;
import com.feign.test.github.model.Repository;
import com.feign.test.github.repository.RepositoryJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
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
    void getRepository_dataCorrect_repositoryReturned() {
        GithubApiResponse githubResponse = new GithubApiResponse("test_name", "test_description", "test_cloneurl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(githubClient.getRepository(any(), any())).thenReturn(githubResponse);

        RepositoryDto result = repositoryService.getRepository("owner", "repositoryName");

        Assertions.assertAll(
                () -> Assertions.assertEquals("test_name", result.fullName()),
                () -> Assertions.assertEquals("test_description", result.description()),
                () -> Assertions.assertEquals("test_cloneurl", result.cloneUrl()),
                () -> Assertions.assertEquals(0, result.stars()),
                () -> Assertions.assertEquals(LocalDateTime.of(2026, 10, 10, 20, 0), result.createdAt())
        );
    }

    @Test
    void getLocalRepository_dataCorrect_repositoryReturned() {
        Repository repository = new Repository(0L, "test_name", "test_description", "test_cloneurl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(repositoryJpaRepository.findByFullName(any())).thenReturn(Optional.of(repository));

        RepositoryDto result = repositoryService.getLocalRepository("owner", "repositoryName");

        Assertions.assertAll(
                () -> Assertions.assertEquals("test_name", result.fullName()),
                () -> Assertions.assertEquals("test_description", result.description()),
                () -> Assertions.assertEquals("test_cloneurl", result.cloneUrl()),
                () -> Assertions.assertEquals(0, result.stars()),
                () -> Assertions.assertEquals(LocalDateTime.of(2026, 10, 10, 20, 0), result.createdAt())
        );
        verify(repositoryJpaRepository).findByFullName("owner/repositoryName");
    }

    @Test
    void getLocalRepository_repositoryNotFound_exceptionThrown() {
        when(repositoryJpaRepository.findByFullName(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(LocalRepositoryNotFoundException.class)
                .isThrownBy(() -> repositoryService.getLocalRepository("repository", "not_found"))
                .extracting(LocalRepositoryNotFoundException::getMessage)
                .isEqualTo("Repository with full name repository/not_found was not found");

        verify(repositoryJpaRepository).findByFullName("repository/not_found");
    }

    @Test
    void createRepository_dataCorrect_repositoryCreatedAndReturned() {
        GithubApiResponse githubResponse = new GithubApiResponse("test_name", "test_description", "test_cloneurl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(githubClient.getRepository(any(), any())).thenReturn(githubResponse);
        Repository savedRepository = new Repository(1L, "test_name", "test_description", "test_cloneurl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(repositoryJpaRepository.save(any(Repository.class))).thenReturn(savedRepository);

        RepositoryDto result = repositoryService.createRepository("owner", "repositoryName");

        Assertions.assertAll(
                () -> Assertions.assertEquals("test_name", result.fullName()),
                () -> Assertions.assertEquals("test_description", result.description()),
                () -> Assertions.assertEquals("test_cloneurl", result.cloneUrl()),
                () -> Assertions.assertEquals(0, result.stars()),
                () -> Assertions.assertEquals(LocalDateTime.of(2026, 10, 10, 20, 0), result.createdAt()),
                () -> verify(githubClient).getRepository("owner", "repositoryName")
        );
        verify(repositoryJpaRepository).save(any(Repository.class));
    }

    @Test
    void updateRepository_dataCorrect_repositoryUpdated() {
        GithubApiResponse githubResponse = new GithubApiResponse("test_name", "test_description", "test_cloneurl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(githubClient.getRepository(any(), any())).thenReturn(githubResponse);

        repositoryService.updateRepository("owner", "repositoryName");

        verify(repositoryJpaRepository).save(any(Repository.class));
    }

    @Test
    void deleteRepository_dataCorrect_repositoryDeleted() {
        GithubApiResponse githubResponse = new GithubApiResponse("test_name", "test_description", "test_cloneurl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(githubClient.getRepository(any(), any())).thenReturn(githubResponse);

        repositoryService.deleteRepository("owner", "repositoryName");

        verify(repositoryJpaRepository).delete(any(Repository.class));
    }
}
