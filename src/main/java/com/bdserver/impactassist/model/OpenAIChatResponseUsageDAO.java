package com.bdserver.impactassist.model;

import lombok.Data;

@Data
public class OpenAIChatResponseUsageDAO {
    private Long prompt_tokens;
    private Long completion_tokens;
    private Long total_tokens;
    private OpenAIChatResponsePromptTokenDetails prompt_tokens_details;
    private OpenAiChatResponseCompletionTokenDetails completion_tokens_details;
}
