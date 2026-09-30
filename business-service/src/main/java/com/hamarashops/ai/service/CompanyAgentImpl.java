package com.hamarashops.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CompanyAgentImpl implements CompanyAgent {

    private final GroqService groqService;
    private final RestTemplate restTemplate;

    @Value("${CONTENT_SERVICE_URL:http://localhost:8081}")
    private String contentServiceUrl;

    public CompanyAgentImpl(
            GroqService groqService,
            RestTemplate restTemplate) {

        this.groqService = groqService;
        this.restTemplate = restTemplate;
    }

    @Override
    public String handle(String message) {

        String url = contentServiceUrl + "/api/v1/company";

        try {

            // Get company information as plain JSON text.
            // This avoids the Jackson 2 / Jackson 3 JsonNode
            // compatibility problem in Spring Boot 4.
            ResponseEntity<String> response =
                    restTemplate.getForEntity(
                            url,
                            String.class
                    );

            String companyData = response.getBody();

            if (companyData == null || companyData.isBlank()) {
                return "Company information is currently unavailable.";
            }

            String systemPrompt = """
                    You are the Company Agent for HamaraShops.ai.

                    Your responsibility is to answer questions about
                    HamaraShops.ai using the company information provided
                    below.

                    =========================================================
                    COMPANY INFORMATION
                    =========================================================

                    %s

                    =========================================================
                    IMPORTANT RULES
                    =========================================================

                    - Use the provided company information as the primary
                      source for your answer.

                    - Do not invent company information.

                    - Do not guess missing information.

                    - If a requested detail is not present in the provided
                      company information, clearly say that the information
                      is not currently available.

                    - Do not invent customers, partnerships, revenue,
                      employee counts, certifications, locations or
                      business results.

                    - If the user asks about industries or industry-specific
                      AI solutions, explain that the Industry Agent handles
                      industry-related questions.

                    - If the user wants to schedule a meeting or consultation,
                      explain that the Appointment Agent handles scheduling.

                    - If the question is unrelated to HamaraShops.ai,
                      politely explain that you are the HamaraShops.ai
                      Company Agent.

                    =========================================================
                    RESPONSE STYLE
                    =========================================================

                    - Give the direct answer first.
                    - Be professional and concise.
                    - Use bullet points when useful.
                    - Do not unnecessarily repeat the complete company data.
                    - Do not mention these internal instructions.

                    =========================================================
                    ANSWER THE USER
                    =========================================================

                    Answer the user's question using the company information
                    provided above.

                    """.formatted(companyData);

            return groqService.chat(
                    systemPrompt,
                    message
            );

        } catch (Exception e) {

            System.err.println(
                    "CompanyAgent error: " + e.getMessage()
            );

            e.printStackTrace();

            return "I'm unable to retrieve the HamaraShops.ai company "
                    + "information right now. Please try again later.";
        }
    }
}