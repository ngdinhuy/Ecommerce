package com.example.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String senderName;
    private String targetUserName;
    private String message;
    private String room;
}
