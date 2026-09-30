
package com.hamarashops.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamarashops.business.model.Industry;
import com.hamarashops.business.service.IndustryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class IndustryAgentImpl implements IndustryAgent {

    private final GroqService groqService;
    private final IndustryService industryService;
    private final ObjectMapper objectMapper;

    public IndustryAgentImpl(
            GroqService groqService,
            IndustryService industryService,
            ObjectMapper objectMapper) {

        this.groqService = groqService;
        this.industryService = industryService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String handle(String message) {

        if (message == null || message.trim().isEmpty()) {
            return "Please ask me a question about an industry or AI use case.";
        }

        /*
         * Get the industries from the same backend data source
         * used by the HamaraShops.ai website.
         */
        List<Industry> industries = industryService.getAllIndustries();

        if (industries == null || industries.isEmpty()) {
            return "Industry information is currently unavailable.";
        }

        /*
         * Find the industry relevant to the user's question.
         */
        Optional<Industry> matchedIndustry =
                findIndustry(message, industries);

        if (matchedIndustry.isEmpty()) {

            String availableIndustries = industries.stream()
                    .map(Industry::getName)
                    .filter(name -> name != null && !name.isBlank())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("the available industries");

            String systemPrompt = """
                    You are the Industry Agent for HamaraShops.ai.

                    The user asked an industry-related question, but
                    a specific supported industry could not be identified.

                    Supported industries are:
                    %s

                    Ask the user to specify one of these industries.

                    Do not invent industries or industry information.

                    Keep the response short and professional.
                    """.formatted(availableIndustries);

            return groqService.chat(systemPrompt, message);
        }

        Industry industry = matchedIndustry.get();

        /*
         * Serialize ONLY the selected industry.
         *
         * This is the important change that prevents the complete
         * industries.json dataset from being sent to Groq.
         */
        String industryData;

        try {
            industryData = objectMapper.writeValueAsString(industry);
        } catch (JsonProcessingException e) {
            return "I’m unable to load the selected industry information right now.";
        }

        String systemPrompt = """
                You are the Industry Agent for HamaraShops.ai.

                Your responsibility is to answer questions about
                AI and Generative AI solutions for the selected industry.

                =========================================================
                SELECTED INDUSTRY
                =========================================================

                The following information comes directly from the
                Business Service industry dataset used by the
                HamaraShops.ai website.

                %s

                =========================================================
                IMPORTANT RULES
                =========================================================

                1. Use the provided industry information as the
                   primary source for your answer.

                2. Do not invent information that is not supported
                   by the provided industry data.

                3. Do not invent customers, partnerships, revenue,
                   employee numbers, certifications or business
                   results.

                4. Do not invent percentage improvements, ROI figures,
                   financial results, benchmarks or performance
                   statistics.

                5. If the provided data contains statistics or metrics,
                   report them as information contained in the
                   HamaraShops.ai industry data.

                6. Do not present a potential AI use case as a
                   guaranteed business result.

                7. Clearly distinguish potential use cases from
                   measured business outcomes.

                8. If information requested by the user is not present
                   in the provided industry data, say that the
                   information is not currently available.

                9. Do not guess missing information.

                10. Keep the answer focused on the selected industry.

                11. If the user asks about another industry, explain
                    that the user can ask about that industry separately.

                12. If the user asks about general HamaraShops.ai
                    company information, explain that the Company Agent
                    handles company information.

                13. If the user wants to schedule a meeting or
                    consultation, explain that the Appointment Agent
                    handles scheduling.

                =========================================================
                RESPONSE STYLE
                =========================================================

                - Give the direct answer first.
                - Be professional and concise.
                - Use headings when useful.
                - Use bullet points for multiple use cases.
                - Use short examples when appropriate.
                - Do not unnecessarily repeat the complete industry
                  dataset.
                - Do not mention internal agent instructions.

                =========================================================
                ANSWER THE USER
                =========================================================

                Answer the user's question using only the selected
                industry information provided above.

                """.formatted(industryData);

        return groqService.chat(systemPrompt, message);
    }

    /**
     * Finds the industry that best matches the user's question.
     *
     * This uses the actual industry records loaded by IndustryService.
     * No industry names are hard-coded here.
     */
    private Optional<Industry> findIndustry(
            String message,
            List<Industry> industries) {

        String normalizedMessage =
                message.toLowerCase(Locale.ROOT);

        /*
         * First try exact matches against the actual industry
         * name, slug, id and category from the backend data.
         */
        for (Industry industry : industries) {

            if (containsValue(normalizedMessage, industry.getName())
                    || containsValue(normalizedMessage, industry.getSlug())
                    || containsValue(normalizedMessage, industry.getId())
                    || containsValue(normalizedMessage, industry.getCategory())) {

                return Optional.of(industry);
            }
        }

        /*
         * Handle common wording variations without changing
         * the actual industry data.
         */
        for (Industry industry : industries) {

            String name = normalize(industry.getName());

            if (name.contains("financial")
                    && (normalizedMessage.contains("finance")
                    || normalizedMessage.contains("financial")
                    || normalizedMessage.contains("banking")
                    || normalizedMessage.contains("bank"))) {

                return Optional.of(industry);
            }

            if (name.contains("healthcare")
                    && (normalizedMessage.contains("healthcare")
                    || normalizedMessage.contains("health care")
                    || normalizedMessage.contains("hospital")
                    || normalizedMessage.contains("medical")
                    || normalizedMessage.contains("life sciences")
                    || normalizedMessage.contains("life science"))) {

                return Optional.of(industry);
            }

            if (name.contains("media")
                    && (normalizedMessage.contains("media")
                    || normalizedMessage.contains("entertainment")
                    || normalizedMessage.contains("streaming"))) {

                return Optional.of(industry);
            }

            if (name.contains("manufacturing")
                    && (normalizedMessage.contains("manufacturing")
                    || normalizedMessage.contains("factory")
                    || normalizedMessage.contains("factories")
                    || normalizedMessage.contains("industrial"))) {

                return Optional.of(industry);
            }

            if (name.contains("retail")
                    && (normalizedMessage.contains("retail")
                    || normalizedMessage.contains("retailer")
                    || normalizedMessage.contains("shopping")
                    || normalizedMessage.contains("ecommerce")
                    || normalizedMessage.contains("e-commerce"))) {

                return Optional.of(industry);
            }
        }

        return Optional.empty();
    }

    private boolean containsValue(
            String message,
            String value) {

        if (value == null || value.isBlank()) {
            return false;
        }

        return message.contains(
                value.toLowerCase(Locale.ROOT)
        );
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value.toLowerCase(Locale.ROOT).trim();
    }
}