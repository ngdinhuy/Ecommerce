package com.example.ecommerce.config;

import com.example.ecommerce.service.ChatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ScheduledTask {
    @Autowired
    private ChatService chatService;
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleTaskWithCronExpression() {
        try {
            chatService.retrainChatbot();
            log.info("Success");
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
