/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.connextion.helpdesk.service;

import com.connextion.helpdesk.model.Comment;
import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.repository.CommentRepository;
import com.connextion.helpdesk.repository.TicketRepository;
import com.connextion.helpdesk.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kenneth
 */
@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;

    @Transactional
    public Comment addComment(Long ticketId, Long userId, String text) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Comment comment = new Comment(text, ticket, user);
        return commentRepository.save(comment);
    }
}
