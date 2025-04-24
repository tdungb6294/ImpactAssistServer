package com.bdserver.impactassist.model;

import lombok.Data;

@Data
public class OpenAiChatResponseChoice {
    private Integer index;
    private OpenAIChatResponseMessageDAO message;
    private String logprobs;
    private String finish_reason;
}
