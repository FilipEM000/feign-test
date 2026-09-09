package com.feign.test.github.mapper;

import com.feign.test.github.dto.RepositoryDto;
import com.feign.test.github.dto.github.GithubApiResponse;
import com.feign.test.github.model.Repository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    RepositoryDto toDto(Repository repository);

    RepositoryDto toDto(GithubApiResponse githubApiResponse);

    @Mapping(target = "id", ignore = true)
    Repository toEntity(GithubApiResponse githubApiResponse);
}
