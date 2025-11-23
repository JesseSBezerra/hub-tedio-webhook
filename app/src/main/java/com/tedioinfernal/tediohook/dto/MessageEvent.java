package com.tedioinfernal.tediohook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent {

    private String event;
    private String instance;
    private MessageData data;
    private String destination;
    private String date_time;
    private String sender;
    private String server_url;
    private String apikey;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageData {
        private MessageKey key;
        private String pushName;
        private String status;
        private Map<String, Object> message;
        private String messageType;
        private Long messageTimestamp;
        private String instanceId;
        private String source;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageKey {
        private String remoteJid;
        private Boolean fromMe;
        private String id;
    }
}
