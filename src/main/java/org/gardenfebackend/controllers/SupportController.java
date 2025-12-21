package org.gardenfebackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.requests.SupportMessageRequest;
import org.gardenfebackend.services.SupportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @PostMapping
    public ResponseEntity<Void> sendSupportMessage(@RequestBody @Valid SupportMessageRequest request) {
        supportService.sendSupportMessage(request);
        return ResponseEntity.ok().build();
    }
}
