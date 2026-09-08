package com.feing.test.github.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
    @Id
    @GeneratedValue
    private Long id;
    private String fullName;
    private String description;
    private String cloneUrl;
    private Integer stars;
    private String createdAt;
}
