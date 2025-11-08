// src/main/java/com/verifi/controller/AutoResolveController.java
package com.verifi.controller;

import com.verifi.service.AutoResolveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autoresolve")
public class AutoResolveController {
    private final AutoResolveService autoResolveService;

    public AutoResolveController(AutoResolveService autoResolveService) {
        this.autoResolveService = autoResolveService;
    }

    @PostMapping("/run")
    public ResponseEntity<String> runNow() {
        autoResolveService.runOnce();
        return ResponseEntity.ok("Auto-resolve run triggered");
    }
}
