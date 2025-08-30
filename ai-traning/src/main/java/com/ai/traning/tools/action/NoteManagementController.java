package com.ai.traning.tools.action;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteManagementController {

    private final ChatClient chatClient;
    private final NoteManagementTools noteManagementTools;

    public NoteManagementController(ChatClient.Builder builder, NoteManagementTools noteManagementTools) {
        this.chatClient = builder.build();
        this.noteManagementTools = noteManagementTools;
    }

    @GetMapping("/tasks")
    public String createNote(@RequestParam String message){
        return chatClient.prompt()
                .tools(noteManagementTools)
                .user(message)
                .call()
                .content();
    }
}
