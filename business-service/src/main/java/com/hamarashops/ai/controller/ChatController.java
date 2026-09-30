package com.hamarashops.ai.controller;

import com.hamarashops.ai.model.ChatRequest;
import com.hamarashops.ai.model.ChatResponse;
import com.hamarashops.ai.service.ChatOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")

public class ChatController {

    private final ChatOrchestrator chatOrchestrator;

    public ChatController(ChatOrchestrator chatOrchestrator) {
        this.chatOrchestrator = chatOrchestrator;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request) {

        if (request == null
                || request.getMessage() == null
                || request.getMessage().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ChatResponse(
                            "System",
                            "Please enter a message."
                    ));
        }

        ChatResponse response =
                chatOrchestrator.processMessage(request.getMessage());

        return ResponseEntity.ok(response);
    }
}
