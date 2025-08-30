package com.ai.traning.tools.countryInfo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryInfoController {
    private final ChatClient chatClient;
    private final CountryInfoTools  countryInfoTools;
    public CountryInfoController(ChatClient.Builder builder, CountryInfoTools countryInfoTools) {
        this.chatClient = builder.build();
        this.countryInfoTools = countryInfoTools;
    }

    @GetMapping("/countryInfo")
    public String countryInfo(@RequestParam String countryName) {
        return chatClient.prompt()
                .tools(countryInfoTools)
                .user(countryName)
                .call()
                .content();
    }
}
