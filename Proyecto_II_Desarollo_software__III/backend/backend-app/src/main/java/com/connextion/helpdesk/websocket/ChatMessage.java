package com.connextion.helpdesk.websocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ChatMessage {

    public enum MessageType {
        CHAT,       
        JOIN,      
        LEAVE       
    }

    private MessageType type;
    private String content;
    private String sender;
    private Long ticketId;
    private String timestamp;

    public ChatMessage() {
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public ChatMessage(MessageType type, String content, String sender, Long ticketId) {
        this();
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.ticketId = ticketId;
    }

    // Getters and setters
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
