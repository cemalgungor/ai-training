package com.ai.traning.multimodal.audio;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AudioGeneration {

     private final OpenAiAudioSpeechModel speechModel;

     public AudioGeneration(OpenAiAudioSpeechModel speechModel) {
         this.speechModel = speechModel;
     }

     @GetMapping("/speak")
     public ResponseEntity<byte[]> generateSpeech(
             @RequestParam(defaultValue = "Can you speech about software developer's future") String text){
     var options = OpenAiAudioSpeechOptions.builder()
             .model("tts-1-hd")
             .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
             .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
             .speed(1.0f)
             .build();
     SpeechPrompt speechPrompt =  new SpeechPrompt(text,options);
     SpeechResponse speechResponse = speechModel.call(speechPrompt);

     byte[] audioBytes = speechResponse.getResult().getOutput();

     return   ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\speech.mp3\"")
             .body(audioBytes);

     }


}
