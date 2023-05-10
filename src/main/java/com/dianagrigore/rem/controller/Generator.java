package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.service.DataGenerationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/generate")
public class Generator {

    private final DataGenerationService dataGenerationService;

    public Generator(DataGenerationService dataGenerationService) {
        this.dataGenerationService = dataGenerationService;
    }

    @PostMapping("/cleanup")
    void cleanup() {
        dataGenerationService.cleanup();
    }

    @PostMapping("/batch")
    void generateBatch() {
        dataGenerationService.batch();
    }

    @PostMapping("/millions")
    void generateMillions() {
        dataGenerationService.millions();
    }

}
