package com.bdserver.impactassist.model;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIChatResponseMessageDAO {
    private String role;
    private String content;
    private String refusal;
    private List<String> annotations;
}
