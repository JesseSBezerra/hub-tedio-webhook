package com.tedioinfernal.tediohook.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageContentExtractor {

    @SuppressWarnings("unchecked")
    public String extract(Map<String, Object> message, String messageType) {
        if (message == null) return null;
        
        // Mensagem de texto simples
        if (message.containsKey("conversation")) {
            return (String) message.get("conversation");
        }
        
        // Mensagem de texto estendida
        if (message.containsKey("extendedTextMessage")) {
            Map<String, Object> extended = (Map<String, Object>) message.get("extendedTextMessage");
            return (String) extended.get("text");
        }
        
        // Mensagem de imagem
        if (message.containsKey("imageMessage")) {
            return extractImageInfo((Map<String, Object>) message.get("imageMessage"));
        }
        
        // Mensagem de vídeo
        if (message.containsKey("videoMessage")) {
            return extractVideoInfo((Map<String, Object>) message.get("videoMessage"));
        }
        
        // Mensagem de áudio
        if (message.containsKey("audioMessage")) {
            return extractAudioInfo((Map<String, Object>) message.get("audioMessage"));
        }
        
        // Mensagem de documento
        if (message.containsKey("documentMessage")) {
            return extractDocumentInfo((Map<String, Object>) message.get("documentMessage"));
        }
        
        // Mensagem de sticker
        if (message.containsKey("stickerMessage")) {
            return "[FIGURINHA]";
        }
        
        // Mensagem de localização
        if (message.containsKey("locationMessage")) {
            return extractLocationInfo((Map<String, Object>) message.get("locationMessage"));
        }
        
        // Mensagem de contato
        if (message.containsKey("contactMessage")) {
            return extractContactInfo((Map<String, Object>) message.get("contactMessage"));
        }
        
        // Outros tipos de mensagem
        return "[" + messageType + "]";
    }

    private String extractImageInfo(Map<String, Object> imageMsg) {
        String caption = (String) imageMsg.get("caption");
        String mimetype = (String) imageMsg.get("mimetype");
        Object width = imageMsg.get("width");
        Object height = imageMsg.get("height");
        
        StringBuilder info = new StringBuilder("[IMAGEM");
        if (mimetype != null) info.append(" - ").append(mimetype);
        if (width != null && height != null) info.append(" - ").append(width).append("x").append(height);
        info.append("]");
        if (caption != null && !caption.isEmpty()) {
            info.append(" Legenda: ").append(caption);
        }
        return info.toString();
    }

    private String extractVideoInfo(Map<String, Object> videoMsg) {
        String caption = (String) videoMsg.get("caption");
        return caption != null ? "[VÍDEO] Legenda: " + caption : "[VÍDEO]";
    }

    private String extractAudioInfo(Map<String, Object> audioMsg) {
        Object seconds = audioMsg.get("seconds");
        return seconds != null ? "[ÁUDIO - " + seconds + "s]" : "[ÁUDIO]";
    }

    private String extractDocumentInfo(Map<String, Object> docMsg) {
        String fileName = (String) docMsg.get("fileName");
        String mimetype = (String) docMsg.get("mimetype");
        return "[DOCUMENTO: " + (fileName != null ? fileName : mimetype) + "]";
    }

    private String extractLocationInfo(Map<String, Object> locMsg) {
        Object lat = locMsg.get("degreesLatitude");
        Object lon = locMsg.get("degreesLongitude");
        return "[LOCALIZAÇÃO: " + lat + ", " + lon + "]";
    }

    private String extractContactInfo(Map<String, Object> contactMsg) {
        String displayName = (String) contactMsg.get("displayName");
        return "[CONTATO: " + (displayName != null ? displayName : "sem nome") + "]";
    }
}
