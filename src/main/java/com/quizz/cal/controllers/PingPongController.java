package com.quizz.cal.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/ping")
public class PingPongController {
    @GetMapping
    @Operation(summary = "só um ping e pong",responses = {
        @ApiResponse(description = "Mensagem pong", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> ping() {
        //System.out.println("ping pong chamado");
        return ResponseEntity.status(200).body("pong");
    }
}
