package com.example.home.api.user;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class groupApi1 {
    private final String API_PATH = "group1";

    @GetMapping(value=API_PATH)
    @ResponseBody
    public String userRefer(@RequestBody String request , HttpServletResponse response) throws Exception {

        return "hoge test";
    }
}