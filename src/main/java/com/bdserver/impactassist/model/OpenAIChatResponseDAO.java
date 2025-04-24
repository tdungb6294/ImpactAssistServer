package com.bdserver.impactassist.model;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIChatResponseDAO {
    private String id;
    private String object;
    private Long created;
    private List<OpenAiChatResponseChoice> choices;
    private OpenAIChatResponseUsageDAO usage;
    private String service_tier;
    private String system_fingerprint;
}
