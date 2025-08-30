package com.ai.traning.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class MedicalAdvisorConfiguration {
    @Value("classpath:/data/models.json")
    private Resource models;
    @Value("medical-vector-store.json")
    private String vectorStoreName;

    @Bean
    SimpleVectorStore vectorStore(EmbeddingModel embeddingModel) throws IOException {
        var store = SimpleVectorStore.builder(embeddingModel).build();
        var file = getVectorStoreFile();
        if (file.exists()) {
            store.load(file);
        } else {
            TextReader reader = new TextReader(models);
            reader.getCustomMetadata().put("filename", "model.txt");
            List<Document> docs = new TokenTextSplitter().apply(reader.get());
            store.add(docs);
            store.save(file);
        }
        return store;
    }
    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }
}
