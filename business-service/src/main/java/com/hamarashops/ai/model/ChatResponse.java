package com.hamarashops.ai.model;

public class ChatResponse {

    private String agent;
    private String message;

    public ChatResponse() {
    }

    public ChatResponse(String agent, String message) {
        this.agent = agent;
        this.message = message;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
