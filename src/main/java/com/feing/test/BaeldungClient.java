package com.feing.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "baeldungClient", url = "https://jsonplaceholder.typicode.com/")
public interface BaeldungClient {

    @GetMapping(value = "/posts")
    String getPosts();
}
