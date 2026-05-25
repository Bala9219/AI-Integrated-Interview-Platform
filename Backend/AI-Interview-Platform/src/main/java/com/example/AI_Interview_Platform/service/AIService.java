package com.example.AI_Interview_Platform.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    @Value("${ai.gemini.api-key:}")
    private String apiKey;

    private final RestTemplate http = new RestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    @jakarta.annotation.PostConstruct
    void logKeyStatus(){
        if(apiKey==null || apiKey.isBlank()){
            System.out.println("[AIService] Gemini API key NOT configured — using fallback scoring.");
        }else {
            System.out.println("[AIService] Gemini API Key loaded (length = " +apiKey.length()+").");
        }
    }

    // AIEvaluation — a plain data holder for what AI sends back.
    @Data
    public static class AIEvaluation{
        public int score;
        public int fillerWords;
        public String relevance;
        public String technicalAccuracy;
        public List<String> strengths = new ArrayList<>();
        public List<String> weaknesses = new ArrayList<>();
        public List<String> recommendations = new ArrayList<>();
    }

    //Method #1 -> Score one Q/A pair using AI...
    public AIEvaluation evaluateWithAI(String question, String answer){
        if(apiKey==null || apiKey.isBlank()){
            return fallbackEvaluation();
        }
        try{
            String prompt = buildPrompt(question, answer);  // step 1: English instructions
            String aiText = callGemini(prompt);             // step 2: HTTP call
            return parseEvaluation(aiText);                 // step 3: text -> java object
        } catch (Exception e) {
            // Common reasons: no internet, invalid key, Gemini quota exceeded, weird response.
            System.err.println("[AIService] Failed: "+e.getMessage());
            return fallbackEvaluation();
        }
    }

    //Method #3 Batch Evaluation...
    public List<AIEvaluation> evaluateBatch(List<EvaluationService.QA> pairs){
        if (apiKey==null || apiKey.isBlank()){
            return new ArrayList<>();
        }
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("""
                    You are an expert technical interviewer.
                    
                    Evaluate each question-answer pair.
                    
                    Return ONLY valid JSON array.
                    
                    Example:
                    [
                      {
                        "score": 70,
                        "fillerWords": 0,
                        "relevance": "high",
                        "technicalAccuracy": "good",
                        "strengths": ["..."],
                        "weaknesses": ["..."],
                        "recommendations": ["..."]
                      }
                    ]
                    """);

            for (int i = 0; i < pairs.size(); i++) {
                EvaluationService.QA qa = pairs.get(i);

                prompt.append("\nQUESTION ").append(i + 1).append(":\n");
                prompt.append(qa.questionText).append("\n");

                prompt.append("\nANSWER ").append(i + 1).append(":\n");
                prompt.append(qa.answerText).append("\n");
            }

            String aiText = callGemini(prompt.toString());

            return parseBatchEvaluation(aiText);
        }catch (Exception e) {
            System.err.println("[AIService] Batch Evaluation Failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<AIEvaluation> parseBatchEvaluation(String aiText) {
        try {
            String clean = cleanJson(aiText);

            return mapper.readValue(
                    clean,
                    new TypeReference<List<AIEvaluation>>() {}
            );

        } catch (Exception e) {
            System.err.println("[AIService] Failed to parse batch JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Build the English instructions we send to AI for answer evaluation...
    private String buildPrompt(String question, String answer) {
        return """
                You are an expert technical interviewer.
                Evaluate the candidate's answer.
                Question:
                %s
                Answer:
                %s
                Evaluate based on:
                1. Relevance to the question
                2. Technical correctness
                3. Clarity
                4. Count filler words in the answer
                   (um, uh, er, like, you know, basically)
                Return ONLY valid JSON.
                Example:
                {
                  "score": 75,
                  "fillerWords": 2,
                  "relevance": "high",
                  "technicalAccuracy": "good",
                  "strengths": ["Clear explanation"],
                  "weaknesses": ["Needs examples"],
                  "recommendations": ["Explain time complexity"]
                }
                """.formatted(question, answer);
    }

    // parseEvaluation — turn the AI's JSON text into our AIEvaluation.
    private AIEvaluation parseEvaluation(String aiText) {
        try {
            String clean = cleanJson(aiText);
            return mapper.readValue(clean, AIEvaluation.class);

        } catch (Exception e) {
            System.err.println("[AIService] Failed to parse evaluation: " + e.getMessage());
            return fallbackEvaluation();
        }
    }

    //Method #2 ->  ask AI to invent N interview questions...
    public List<String> generateQuestions(String role, String experienceLevel, String difficulty, int count){
        if(apiKey==null || apiKey.isBlank()){
            return null;
        }
        try {
            String prompt = buildQuestionPrompt(role, experienceLevel, difficulty, count);
            String aiText = callGemini(prompt);
            return parseQuestionList(aiText);
        } catch (Exception e) {
            System.err.println("[AIService] Generating Questions Failed: "+e.getMessage());
            return null;
        }
    }

    //Build the English instructions we send to the AI for question generation...
    private String buildQuestionPrompt(String role, String experienceLevel, String difficulty, int count) {
        String safeRole = role==null || role.isBlank() ? "Software Engineer":role;
        String safeExp = experienceLevel==null || experienceLevel.isBlank() ? "Mid":experienceLevel;
        String safeDiff = difficulty==null || difficulty.isBlank() ? "Medium":difficulty;
        int safeCount = count<=0 ? 5:count;

        return """
                You are an expert technical interviewer.
                Generate exactly %d interview questions
                for a %s-level %s at %s difficulty.

                Rules:
                - Mix conceptual, practical, and scenario-based questions
                - Each question must be one sentence
                - No numbering
                - No markdown
                - Return ONLY valid JSON array

                Example:
                [
                  "What is polymorphism in Java?",
                  "Explain REST API design principles"
                ]
                """.formatted(safeCount, safeExp, safeRole, safeDiff);
    }

    //Pull a JSON array of strings out of the AI's reply eg: unwanted characters...
    private List<String> parseQuestionList(String aiText) {
        try {
            String clean = cleanJson(aiText);

            return mapper.readValue(clean, new TypeReference<List<String>>() {});

        } catch (Exception e) {
            System.err.println("[AIService] Failed to parse questions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /* Build the JSON body by hand. We must escape the prompt because the
       prompt itself may contain quotes/newlines that would BREAK the JSON.
       User Prompt
           ↓
    Create JSON Request Body
           ↓
    Send HTTP POST Request to Gemini
           ↓
    Receive JSON Response
           ↓
    Extract "text" field using Regex
           ↓
    Convert escaped JSON text to normal Java string
           ↓
    Return AI Response  */
    private String callGemini(String prompt) throws Exception {
        String requestBody = mapper.writeValueAsString(new GeminiRequest(prompt));

        // Tell Gemini we're sending JSON...
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> req = new HttpEntity<>(requestBody, headers);

        // POST to: https://...generateContent?key=YOUR_KEY
        ResponseEntity<String> resp = http.exchange(GEMINI_URL + apiKey, HttpMethod.POST, req, String.class);

        JsonNode root = mapper.readTree(resp.getBody());
        JsonNode textNode = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");

        if (textNode.isMissingNode()) {
            throw new RuntimeException("No text found in Gemini response");
        }
        return textNode.asText();
    }


    private String cleanJson(String text) {
        if (text == null) {
            return "";
        }
        String clean = text.trim();
        // Remove mark down code fences
        if (clean.startsWith("```")) {
            clean = clean.replaceFirst("^```(?:json)?", "")
                    .replaceFirst("```$", "")
                    .trim();
        }
        return clean;
    }

    private AIEvaluation fallbackEvaluation() {
        AIEvaluation e = new AIEvaluation();

        e.setScore(50);
        e.setFillerWords(0);
        e.setRelevance("medium");
        e.setTechnicalAccuracy("average");
        e.getStrengths().add("Evaluation unavailable");
        e.getWeaknesses().add("AI service unavailable");
        e.getRecommendations().add("Try again later");

        return e;
    }

    @Data
    static class GeminiRequest {
        private List<Content> contents;

        public GeminiRequest(String prompt) {
            this.contents = List.of(new Content(prompt));
        }
    }

    @Data
    static class Content {
        private List<Part> parts;

        public Content(String text) {
            this.parts = List.of(new Part(text));
        }
    }

    @Data
    static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }
    }
}
