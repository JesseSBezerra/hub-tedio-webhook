package com.tedioinfernal.tediohook.controller;

import com.tedioinfernal.tediohook.service.ContactService;
import com.tedioinfernal.tediohook.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final ContactService contactService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> receiveWebhook(@RequestBody Map<String, Object> payload) {
        log.info("=== Webhook recebido ===");
        log.info("Timestamp: {}", LocalDateTime.now());
        
        String eventType = (String) payload.get("event");
        String instance = (String) payload.get("instance");
        
        log.info("Event Type: {}", eventType);
        log.info("Instance: {}", instance);
        
        // Processa eventos específicos
        switch (eventType) {
            case "contacts.upsert" -> contactService.processContactsUpsert(payload);
            case "contacts.update" -> contactService.processContactsUpdate(payload);
            case "messages.upsert" -> messageService.processMessagesUpsert(payload);
            default -> log.info("Evento não processado: {}. Payload: {}", eventType, payload);
        }
        
        log.info("========================");

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Webhook recebido com sucesso",
                "event", eventType != null ? eventType : "unknown",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

}
