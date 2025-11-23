package com.tedioinfernal.tediohook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionWebhookEvent {

    private String event;
    private String instance;
    private List<ContactData> data;
    private String destination;
    private String date_time;
    private String sender;
    private String server_url;
    private String apikey;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactData {
        private String remoteJid;
        private String pushName;
        private String profilePicUrl;
        private String instanceId;
    }
}
