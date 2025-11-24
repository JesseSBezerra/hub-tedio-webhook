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
public class RabbitMessageDto {
    
    private String event;
    private Map<String, Object> object;
}
