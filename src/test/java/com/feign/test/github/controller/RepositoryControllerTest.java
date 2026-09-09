package com.feign.test.github.controller;

import com.feign.test.github.dto.RepositoryDto;
import com.feign.test.github.exception.LocalRepositoryNotFoundException;
import com.feign.test.github.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        RepositoryDto repositoryDto = new RepositoryDto("test_name", "test_description", "test_cloneUrl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(repositoryService.getRepository(any(), any())).thenReturn(repositoryDto);

        mockMvc.perform(get("/repositories/owner/repositoryName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("test_name"))
                .andExpect(jsonPath("$.description").value("test_description"))
                .andExpect(jsonPath("$.cloneUrl").value("test_cloneUrl"))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("2026-10-10T20:00:00"));
        verify(repositoryService).getRepository("owner", "repositoryName");
    }

    @Test
    void getLocalRepository_dataCorrect_repositoryReturned() throws Exception {
        RepositoryDto repositoryDto = new RepositoryDto("test_name", "test_description", "test_cloneUrl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(repositoryService.getLocalRepository(any(), any())).thenReturn(repositoryDto);

        mockMvc.perform(get("/local/repositories/owner/repositoryName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("test_name"))
                .andExpect(jsonPath("$.description").value("test_description"))
                .andExpect(jsonPath("$.cloneUrl").value("test_cloneUrl"))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("2026-10-10T20:00:00"));
        verify(repositoryService).getLocalRepository("owner", "repositoryName");
    }

    @Test
    void getLocalRepository_notFound_returnsError() throws Exception {
        when(repositoryService.getLocalRepository("owner", "repositoryName"))
                .thenThrow(new LocalRepositoryNotFoundException("Repository not found"));

        mockMvc.perform(get("/local/repositories/owner/repositoryName"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Repository not found"));
    }

    @Test
    void create_dataCorrect_repositoryReturned() throws Exception {
        RepositoryDto repositoryDto = new RepositoryDto("test_name", "test_description", "test_cloneUrl", 0, LocalDateTime.of(2026, 10, 10, 20, 0));
        when(repositoryService.createRepository(any(), any())).thenReturn(repositoryDto);

        mockMvc.perform(post("/repositories/owner/repositoryName"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("test_name"))
                .andExpect(jsonPath("$.description").value("test_description"))
                .andExpect(jsonPath("$.cloneUrl").value("test_cloneUrl"))
                .andExpect(jsonPath("$.stars").value(0))
                .andExpect(jsonPath("$.createdAt").value("2026-10-10T20:00:00"));
        verify(repositoryService).createRepository("owner", "repositoryName");
    }

    @Test
    void update_dataCorrect_repositoryUpdated() throws Exception {
        mockMvc.perform(put("/repositories/owner/repositoryName"))
                .andExpect(status().isNoContent());
        verify(repositoryService).updateRepository("owner", "repositoryName");
    }

    @Test
    void delete_dataCorrect_repositoryDeleted() throws Exception {
        mockMvc.perform(delete("/repositories/owner/repositoryName"))
                .andExpect(status().isNoContent());
        verify(repositoryService).deleteRepository("owner", "repositoryName");
    }
}