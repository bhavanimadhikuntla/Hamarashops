package com.hamarashops.ai.service;

import com.hamarashops.ai.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatOrchestrator {

    private final CompanyAgent companyAgent;
    private final IndustryAgent industryAgent;
    private final AppointmentAgent appointmentAgent;

    public ChatOrchestrator(
            CompanyAgent companyAgent,
            IndustryAgent industryAgent,
            AppointmentAgent appointmentAgent) {

        this.companyAgent = companyAgent;
        this.industryAgent = industryAgent;
        this.appointmentAgent = appointmentAgent;
    }

    public ChatResponse processMessage(String message) {

        String lowerMessage = message.toLowerCase();

        if (containsAny(lowerMessage,
                "appointment",
                "schedule",
                "meeting",
                "book",
                "demo",
                "consultation")) {

            return new ChatResponse(
                    "Appointment Agent",
                    appointmentAgent.handle(message)
            );
        }

        if (containsAny(lowerMessage,
                "retail",
                "healthcare",
                "financial",
                "finance",
                "manufacturing",
                "industry",
                "industries")) {

            return new ChatResponse(
                    "Industry Agent",
                    industryAgent.handle(message)
            );
        }

        return new ChatResponse(
                "Company Agent",
                companyAgent.handle(message)
        );
    }

    private boolean containsAny(String message, String... keywords) {

        for (String keyword : keywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
