package com.feign.test.github.repository;

import com.feign.test.github.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryJpaRepository extends JpaRepository<Repository, Long> {
    Optional<Repository> findByFullName(String fullName);
}
