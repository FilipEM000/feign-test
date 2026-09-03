package com.feing.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/baeldung")
public class BaeldungController {
    private final BaeldungClient baeldungClient;

    @GetMapping
    public String get() {
        return baeldungClient.getPosts();
    }
}
