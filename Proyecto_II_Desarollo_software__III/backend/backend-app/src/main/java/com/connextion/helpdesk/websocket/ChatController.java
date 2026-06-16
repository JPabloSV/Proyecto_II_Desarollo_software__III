package com.connextion.helpdesk.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

   
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message) {
        logger.info("[Chat] Message from '{}' on ticket #{}: {}",
                message.getSender(), message.getTicketId(), message.getContent());

        messagingTemplate.convertAndSend(
                "/topic/ticket/" + message.getTicketId(),
                message
        );
    }

   
    @MessageMapping("/chat.join")
    public void joinRoom(@Payload ChatMessage message,
                         SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        headerAccessor.getSessionAttributes().put("ticketId", message.getTicketId());

        message.setType(ChatMessage.MessageType.JOIN);
        message.setContent(message.getSender() + " se unió al chat.");

        logger.info("[Chat] '{}' joined ticket #{} chat", message.getSender(), message.getTicketId());

        messagingTemplate.convertAndSend(
                "/topic/ticket/" + message.getTicketId(),
                message
        );
    }
}
