package com.bdserver.impactassist.model;

import lombok.Data;

@Data
public class OpenAIChatResponsePromptTokenDetails {
    private Long cached_tokens;
    private Long audio_tokens;
}
