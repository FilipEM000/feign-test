package com.feing.test.github.controller;

import com.feing.test.github.dto.RepositorySummaryResponse;
import com.feing.test.github.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RepositoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RepositoryService repositoryService;

    @Test
    void getRepository_dataCorrect_repositoryReturned() throws Exception {
        RepositorySummaryResponse repositorySummaryResponse = new RepositorySummaryResponse("test_name", "test_description", "test_cloneUrl", 0, "test_createdAt");
        when(repositoryService.getRepositorySummary(any(), any())).thenReturn(repositorySummaryResponse);

        mockMvc.perform(get("/repositories/owner/repositoryName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("test_name"))
                .andExpect(jsonPath("$.description").value("test_description"))
                .andExpect(jsonPath("$.cloneUrl").value("test_cloneUrl"))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("test_createdAt"));
        verify(repositoryService).getRepositorySummary("owner", "repositoryName");
    }

    @Test
    void getLocalRepository_dataCorrect_repositoryReturned() throws Exception {
        RepositorySummaryResponse repositorySummaryResponse = new RepositorySummaryResponse("test_name", "test_description", "test_cloneUrl", 0, "test_createdAt");
        when(repositoryService.getLocalRepository(any(), any())).thenReturn(repositorySummaryResponse);

        mockMvc.perform(get("/local/repositories/owner/repositoryName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("test_name"))
                .andExpect(jsonPath("$.description").value("test_description"))
                .andExpect(jsonPath("$.cloneUrl").value("test_cloneUrl"))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("test_createdAt"));
        verify(repositoryService).getLocalRepository("owner", "repositoryName");
    }

    @Test
    void create_dataCorrect_repositoryReturned() throws Exception {
        RepositorySummaryResponse repositorySummaryResponse = new RepositorySummaryResponse("test_name", "test_description", "test_cloneUrl", 0, "test_createdAt");
        when(repositoryService.createRepository(any(), any())).thenReturn(repositorySummaryResponse);

        mockMvc.perform(post("/repositories/owner/repositoryName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("test_name"))
                .andExpect(jsonPath("$.description").value("test_description"))
                .andExpect(jsonPath("$.cloneUrl").value("test_cloneUrl"))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("test_createdAt"));
        verify(repositoryService).createRepository("owner", "repositoryName");
    }
}