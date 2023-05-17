package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.permissions.PermissionCheck;
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
    @PermissionCheck(hasAny = {UserType.ADMIN})
    void cleanup() {
        dataGenerationService.cleanup();
    }

    @PostMapping("/batch")
    @PermissionCheck(hasAny = {UserType.ADMIN})
    void generateBatch() {
        dataGenerationService.batch();
    }

    @PostMapping("/millions")
    @PermissionCheck(hasAny = {UserType.ADMIN})
    void generateMillions() {
        dataGenerationService.millions();
    }

}
