package com.hamarashops.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GroqService {

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${GROQ_API_KEY:}")
    private String apiKey;

    public String chat(String systemPrompt, String userMessage) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GROQ_API_KEY is not configured. "
                            + "Please create business-service/.env "
                            + "using .env.example."
            );
        }

        String escapedSystemPrompt =
                escapeJson(systemPrompt);

        String escapedUserMessage =
                escapeJson(userMessage);

        String json = """
                {
                  "model": "openai/gpt-oss-120b",
                  "messages": [
                    {
                      "role": "system",
                      "content": "%s"
                    },
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ],
                  "temperature": 0.3
                }
                """.formatted(
                escapedSystemPrompt,
                escapedUserMessage
        );

        RequestBody body =
                RequestBody.create(json, JSON);

        Request request =
                new Request.Builder()
                        .url(GROQ_URL)
                        .post(body)
                        .addHeader(
                                "Authorization",
                                "Bearer " + apiKey
                        )
                        .addHeader(
                                "Content-Type",
                                "application/json"
                        )
                        .build();

        try (Response response =
                     client.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                String errorBody =
                        response.body() != null
                                ? response.body().string()
                                : "No response body";

                throw new RuntimeException(
                        "Groq API error: "
                                + response.code()
                                + " - "
                                + errorBody
                );
            }

            if (response.body() == null) {
                throw new RuntimeException(
                        "Groq API returned an empty response."
                );
            }

            String responseBody =
                    response.body().string();

            return extractContent(responseBody);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to connect to Groq API.",
                    e
            );
        }
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    private String extractContent(String json) {

        try {

            JsonNode root =
                    objectMapper.readTree(json);

            JsonNode content =
                    root.path("choices")
                            .path(0)
                            .path("message")
                            .path("content");

            if (content.isMissingNode()
                    || content.isNull()) {

                throw new RuntimeException(
                        "Could not read the content "
                                + "from Groq response."
                );
            }

            return content.asText();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to parse Groq API response.",
                    e
            );
        }
    }
}