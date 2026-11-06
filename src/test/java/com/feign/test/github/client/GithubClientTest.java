package com.feign.test.github.client;

import com.feign.test.github.dto.github.GithubApiResponse;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableWireMock
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;

    @AfterEach
    void after() {
        WireMock.reset();
    }

    @Test
    public void getRepository_returns503_shouldRetryAndFallback() {
        stubFor(get(urlEqualTo("/repos/testOwner/testRepo503"))
                .willReturn(aResponse().withStatus(503)));

        GithubApiResponse response = githubClient.getRepository("testOwner", "testRepo503");

        assertAll(
                () -> assertEquals("testOwner/testRepo503 (FALLBACK)", response.fullName()),
                () -> assertEquals("Data temporarily unavailable due to an external service failure.", response.description()),
                () -> assertEquals("", response.cloneUrl()),
                () -> assertEquals(0, response.stars()),
                () -> assertNull(response.createdAt())
        );
        verify(6, getRequestedFor(urlEqualTo("/repos/testOwner/testRepo503")));
    }

    @Test
    public void getRepository_returns500_shouldFallbackImmediatelyWithoutRetry() {
        stubFor(get(urlEqualTo("/repos/testOwner/testRepo500"))
                .willReturn(aResponse().withStatus(500)));

        GithubApiResponse response = githubClient.getRepository("testOwner", "testRepo500");

        assertAll(
                () -> assertEquals("testOwner/testRepo500 (FALLBACK)", response.fullName()),
                () -> assertEquals("Data temporarily unavailable due to an external service failure.", response.description()),
                () -> assertEquals("", response.cloneUrl()),
                () -> assertEquals(0, response.stars()),
                () -> assertNull(response.createdAt())
        );
        verify(1, getRequestedFor(urlEqualTo("/repos/testOwner/testRepo500")));
    }

    @Test
    public void getRepository_returns200_shouldReturnRealData() {
        stubFor(get(urlEqualTo("/repos/testOwner/testRepo200"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github-response.json")));

        GithubApiResponse response = githubClient.getRepository("testOwner", "testRepo200");

        assertAll(
                () -> assertEquals("testOwner/testRepo200", response.fullName()),
                () -> assertEquals("test description", response.description()),
                () -> assertEquals("https://github.com/...", response.cloneUrl()),
                () -> assertEquals(999, response.stars()),
                () -> assertEquals(LocalDateTime.of(2026, 9, 20, 20, 0, 0), response.createdAt())
        );
        verify(1, getRequestedFor(urlEqualTo("/repos/testOwner/testRepo200")));
    }
}
