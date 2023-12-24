package com.example.ecommerce.service;

import com.example.ecommerce.response.ChatbotResponse;
import com.example.ecommerce.utils.Define;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ChatService {
    public String sendMessageToChatBot(String question){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Define.URL_CHAT)
                .queryParam("question", question);

        ResponseEntity<ChatbotResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), ChatbotResponse.class);
        ChatbotResponse response = responseEntity.getBody();
        return response.getResponse();
    }

    public void retrainChatbot(){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Define.URL_CHAT);
        ResponseEntity<ChatbotResponse> responseEntity = restTemplate.getForEntity(builder.toUriString(), ChatbotResponse.class);
    }
}
