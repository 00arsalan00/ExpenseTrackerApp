package com.app.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternalController {

    @Hidden
    @GetMapping("/internal/health-check")
    public String internalCheck() {
        return "OK";
    }
}