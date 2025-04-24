package com.bdserver.impactassist.model;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponseData {
    private String context_message;
    private List<Integer> parts_id;
}
