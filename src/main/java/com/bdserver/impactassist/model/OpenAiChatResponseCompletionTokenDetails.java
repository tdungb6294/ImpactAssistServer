package com.bdserver.impactassist.model;

import lombok.Data;

@Data
public class OpenAiChatResponseCompletionTokenDetails {
    private Long reasoning_tokens;
    private Long audio_tokens;
    private Long accepted_prediction_tokens;
    private Long rejected_prediction_tokens;
}
