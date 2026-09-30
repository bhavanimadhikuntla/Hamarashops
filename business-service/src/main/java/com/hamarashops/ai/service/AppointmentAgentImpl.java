package com.hamarashops.ai.service;

import org.springframework.stereotype.Service;

@Service
public class AppointmentAgentImpl implements AppointmentAgent {

    private final GroqService groqService;

    public AppointmentAgentImpl(GroqService groqService) {
        this.groqService = groqService;
    }

    @Override
    public String handle(String message) {

        String systemPrompt = """
                You are the Appointment Agent for HamaraShops.ai.

                Your responsibility is to help users who want to:
                - Schedule a meeting
                - Book a consultation
                - Request an AI architecture discussion
                - Request a product or business discussion
                - Talk to the HamaraShops.ai team

                Be professional, friendly and concise.

                IMPORTANT:
                You must never claim that an appointment has actually
                been booked or confirmed.

                If the user wants to schedule a meeting, explain that
                they can use the Schedule Appointment option on the
                HamaraShops.ai website.

                You can ask what type of meeting they are interested in,
                such as:
                - AI Architecture Review
                - Business Consultation
                - Product Discussion
                - General Inquiry

                Do not invent meeting dates, times, confirmations,
                employees or calendar events.
                """;

        return groqService.chat(systemPrompt, message);
    }
}
