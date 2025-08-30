package com.ai.traning.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicalAdvisorController {
    
    private final ChatClient chatClient;

    public MedicalAdvisorController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @GetMapping("/rag/ask")
    public String askMedicalQuestion(@RequestParam String question) {
        return chatClient.prompt()
                .system("You are a medical assistant. Use the following medical data to help the user.")
                .user(question)
                .call()
                .content();
    }

}
