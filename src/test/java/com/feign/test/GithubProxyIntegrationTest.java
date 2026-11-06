package com.feign.test;

import com.feign.test.github.repository.RepositoryJpaRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.spring.EnableWireMock;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWireMock
public class GithubProxyIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RepositoryJpaRepository repositoryJpaRepository;

    @AfterEach
    void after() {
        WireMock.reset();
        repositoryJpaRepository.deleteAll();
    }

    @Test
    void getRepository_dataCorrect_repositoryReturned() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/repos/testOwner/testRepo200"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-response.json")));

        mockMvc.perform(get("/repositories/testOwner/testRepo200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("testOwner/testRepo200"))
                .andExpect(jsonPath("$.description").value("test description"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/..."))
                .andExpect(jsonPath("$.stars").value(999))
                .andExpect(jsonPath("$.createdAt").value("2026-09-20T20:00:00"));
    }

    @Test
    void getRepository_repositoryNotFound_fallbackReturned() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/repos/testOwner/testRepo503"))
                .willReturn(aResponse().withStatus(503)));

        mockMvc.perform(get("/repositories/testOwner/testRepo503"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("testOwner/testRepo503 (FALLBACK)"))
                .andExpect(jsonPath("$.description").value("Data temporarily unavailable due to an external service failure."))
                .andExpect(jsonPath("$.cloneUrl").value(""))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.cratedAt").doesNotExist());
    }

    @Test
    void create_dataCorrect_repositoryCreated() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/repos/testOwner/testRepo200"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-response.json")));

        mockMvc.perform(post("/repositories/testOwner/testRepo200"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("testOwner/testRepo200"))
                .andExpect(jsonPath("$.description").value("test description"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/..."))
                .andExpect(jsonPath("$.stars").value(999))
                .andExpect(jsonPath("$.createdAt").value("2026-09-20T20:00:00"));
    }

    @Test
    void create_repositoryNotFound_fallbackReturned() throws Exception {
        stubFor(WireMock.post(urlEqualTo("/repos/testOwner/testRepo503"))
                .willReturn(aResponse().withStatus(503)));

        mockMvc.perform(post("/repositories/testOwner/testRepo503"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("testOwner/testRepo503 (FALLBACK)"))
                .andExpect(jsonPath("$.description").value("Data temporarily unavailable due to an external service failure."))
                .andExpect(jsonPath("$.cloneUrl").value(""))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.cratedAt").doesNotExist());
    }

    @Test
    void delete_dataCorrect_repositoryDeleted() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/repos/testOwner/testRepo200"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-response.json")));

        mockMvc.perform(delete("/repositories/testOwner/testRepo200"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_githubReturns503() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/repos/testOwner/testRepo503"))
                .willReturn(aResponse().withStatus(503)));

        mockMvc.perform(delete("/repositories/testOwner/testRepo503"))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_dataCorrect_repositoryUpdated() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/repos/testOwner/testRepo200"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-response.json")));

        mockMvc.perform(put("/repositories/testOwner/testRepo200"))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_githubReturns503() throws Exception {
        stubFor(WireMock.get(urlEqualTo("/repos/testOwner/testRepo503"))
                .willReturn(aResponse().withStatus(503)));

        mockMvc.perform(put("/repositories/testOwner/testRepo503"))
                .andExpect(status().isNoContent());
    }
}
