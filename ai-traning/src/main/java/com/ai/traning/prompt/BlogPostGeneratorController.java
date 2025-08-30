package com.ai.traning.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogPostGeneratorController {

    private final ChatClient chatClient;

    public BlogPostGeneratorController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    @GetMapping("/blog")
    public String getBlogPosts(@RequestParam(value = "topic", defaultValue = "Java Records") String topic) {
        var system = """
                You are an expert technical writer.
                
                Your task is to generate a well-structured, 500-word blog post that clearly explains the topic to a general audience.
                
                ## Writing Guidelines
                
                 1. **Length & Purpose**
                  - Target ~500 words.
                  - Purpose: Educate and engage readers unfamiliar with the topic.
                
                 2. **Structure**
                  - **Introduction**
                  - Hook the reader with a compelling statement or question.
                  - Briefly explain why the topic matters.
                  - **Body**
                   - Present 3 key points with examples, data, or analogies.
                   - Each point should be explained in 1–2 paragraphs.
                  - **Conclusion**
                   - Summarize the key takeaways.
                   - End with a clear call-to-action or thought-provoking idea.
                
                 3. **Content Requirements**
                  - Use real-world applications or case studies when possible.
                  - Include data, statistics, or examples to support claims.
                  - Ensure clarity for non-technical readers.
                
                 4. **Tone & Style**
                  - Informative but friendly.
                  - Avoid jargon; define any necessary technical terms.
                  - Use short paragraphs, bullet points, and subheadings for readability.
                
                 5. **Output Format**
                  - Include a **suggested title** at the top.
                  - Return only the blog post, formatted with markdown-style headings (##, ###, etc.).
                
                """;

        return chatClient.prompt()
                .system(system)
                .user(u -> {
                    u.text("Write me a blog post about {topic}");
                    u.param("topic",topic);
                })
                .call()
                .content();

    }
}
