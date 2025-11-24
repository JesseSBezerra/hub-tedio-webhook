package com.tedioinfernal.tediohook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tedioinfernal.tediohook.dto.MessageEvent;
import com.tedioinfernal.tediohook.dto.RabbitMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final ObjectMapper objectMapper;
    private final MessageContentExtractor contentExtractor;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public void processMessagesUpsert(Map<String, Object> payload) {
        try {
            MessageEvent event = objectMapper.convertValue(payload, MessageEvent.class);
            MessageEvent.MessageData data = event.getData();
            
            if (data == null) {
                log.warn("Mensagem sem dados");
                return;
            }
            
            logMessageInfo(data);
            publishToRabbitMQ(payload);
            
        } catch (Exception e) {
            log.error("Erro ao processar messages.upsert", e);
        }
    }

    private void publishToRabbitMQ(Map<String, Object> payload) {
        try {
            RabbitMessageDto message = RabbitMessageDto.builder()
                    .event("MESSAGE-RECEIVED")
                    .object(payload)
                    .build();
            
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
            log.info("Mensagem publicada no RabbitMQ com sucesso");
            
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem no RabbitMQ", e);
        }
    }

    private void logMessageInfo(MessageEvent.MessageData data) {
        String direction = data.getKey().getFromMe() ? "ENVIADA" : "RECEBIDA";
        String chatType = identifyChatType(data.getKey().getRemoteJid());
        String contact = data.getKey().getRemoteJid();
        
        LocalDateTime messageTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(data.getMessageTimestamp()),
            ZoneId.systemDefault()
        );
        
        log.info("Mensagem {} - [{}] {}", direction, chatType, contact);
        log.info("  De: {}", data.getPushName() != null ? data.getPushName() : "[sem nome]");
        log.info("  Tipo: {}", data.getMessageType());
        log.info("  Status: {}", data.getStatus());
        log.info("  Horário: {}", messageTime);
        log.info("  Source: {}", data.getSource());
        
        if (data.getMessage() != null) {
            String messageContent = contentExtractor.extract(data.getMessage(), data.getMessageType());
            if (messageContent != null) {
                log.info("  Conteúdo: {}", messageContent);
            }
        }
    }

    private String identifyChatType(String remoteJid) {
        if (remoteJid.endsWith("@g.us")) {
            return "Grupo";
        } else if (remoteJid.endsWith("@lid")) {
            return "Canal/Lista";
        } else {
            return "Privado";
        }
    }
}
