/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.connextion.helpdesk.dto;

/**
 *
 * @author Kenneth
 */
public class CommentRequestDTO {
    private Long userId;
    private String text;


    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
