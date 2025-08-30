package com.ai.traning.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }
    @GetMapping("/chat")
    public String chat(){
        return chatClient.prompt()
                .user("Tell me something about Spring ai")
                .call()
                .content();
    }
    @GetMapping("/stream")
    public Flux<String> stream(){
        return chatClient.prompt()
                .user("I want to be software engineer, can you give me 5 best software languages")
                .stream()
                .content();
    }
    @GetMapping("/information")
    public ChatResponse information(){
        return chatClient.prompt()
                .user("Give me some information about Java 24")
                .system("You are a helpful assistant and Java expert.")
                .call()
                .chatResponse();
    }
}
