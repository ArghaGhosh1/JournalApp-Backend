package com.example.JournalApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthCheck {

    @GetMapping("/health_check")
    public String healthCheck(){
        return "ok";
    }
}
