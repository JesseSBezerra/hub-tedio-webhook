package com.tedioinfernal.tediohook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tedioinfernal.tediohook.dto.EvolutionWebhookEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ObjectMapper objectMapper;

    public void processContactsUpsert(Map<String, Object> payload) {
        try {
            EvolutionWebhookEvent event = objectMapper.convertValue(payload, EvolutionWebhookEvent.class);
            log.info("Contatos recebidos: {} contatos", event.getData() != null ? event.getData().size() : 0);
            
            if (event.getData() != null && !event.getData().isEmpty()) {
                log.info("Primeiros 5 contatos:");
                event.getData().stream()
                    .limit(5)
                    .forEach(contact -> log.info("  - {} ({})", contact.getPushName(), contact.getRemoteJid()));
            }
        } catch (Exception e) {
            log.error("Erro ao processar contacts.upsert", e);
        }
    }

    public void processContactsUpdate(Map<String, Object> payload) {
        try {
            Object dataObj = payload.get("data");
            List<EvolutionWebhookEvent.ContactData> contacts = extractContacts(dataObj);
            
            log.info("Contatos atualizados: {} contato(s)/grupo(s)", contacts.size());
            
            if (!contacts.isEmpty()) {
                log.info("Atualizações recebidas:");
                contacts.forEach(contact -> {
                    String type = identifyContactType(contact.getRemoteJid());
                    String picStatus = contact.getProfilePicUrl() != null ? "com foto" : "sem foto";
                    String name = getContactName(contact.getPushName());
                    log.info("  - [{}] {} ({}) - {}", type, name, contact.getRemoteJid(), picStatus);
                });
            }
        } catch (Exception e) {
            log.error("Erro ao processar contacts.update", e);
        }
    }

    private List<EvolutionWebhookEvent.ContactData> extractContacts(Object dataObj) {
        List<EvolutionWebhookEvent.ContactData> contacts = new ArrayList<>();
        
        if (dataObj instanceof List) {
            EvolutionWebhookEvent event = objectMapper.convertValue(
                Map.of("data", dataObj), 
                EvolutionWebhookEvent.class
            );
            contacts = event.getData();
        } else if (dataObj instanceof Map) {
            EvolutionWebhookEvent.ContactData contact = objectMapper.convertValue(
                dataObj, 
                EvolutionWebhookEvent.ContactData.class
            );
            contacts.add(contact);
        }
        
        return contacts;
    }

    private String identifyContactType(String remoteJid) {
        if (remoteJid.endsWith("@g.us")) {
            return "Grupo";
        } else if (remoteJid.endsWith("@lid")) {
            return "Canal/Lista";
        } else {
            return "Contato";
        }
    }

    private String getContactName(String pushName) {
        return (pushName != null && !pushName.isEmpty()) ? pushName : "[sem nome]";
    }
}
