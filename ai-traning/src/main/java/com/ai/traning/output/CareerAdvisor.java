package com.ai.traning.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CareerAdvisor {

    private final ChatClient chatClient;

    public CareerAdvisor(ChatClient.Builder chatClient) {
    this.chatClient = chatClient.build();
    }

    @GetMapping("/career/structured")
    public CareerPlan structuredAdvice(
            @RequestParam String skill,
            @RequestParam String interest){
        return chatClient.prompt()
                .user(u -> {
                    u.text("Based on a person skilled in {skill} and interested in {interest}, suggest 2 suitable career paths with required skills and resources.");
                    u.param("skill", skill);
                    u.param("interest", interest);
                })
                .call()
                .entity(CareerPlan.class);
    }
}
