package com.feing.test.github.mapper;

import com.feing.test.github.dto.RepositorySummaryResponse;
import com.feing.test.github.dto.github.GithubApiResponse;
import com.feing.test.github.model.Repository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    RepositorySummaryResponse toResponse(Repository repository);

    RepositorySummaryResponse toResponseFromGithubResponse(GithubApiResponse githubApiResponse);

    @Mapping(target = "id", ignore = true)
    Repository toEntityFromGithubResponse(GithubApiResponse githubApiResponse);
}
