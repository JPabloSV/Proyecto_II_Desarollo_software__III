/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.connextion.helpdesk.controller;

import com.connextion.helpdesk.model.Comment;
import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.repository.CommentRepository;
import com.connextion.helpdesk.repository.TicketRepository;
import com.connextion.helpdesk.repository.UserRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author Kenneth
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*") // Ajusta según tu configuración
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addTechnicalNote(@RequestBody Map<String, Object> payload) {
        Long ticketId = Long.parseLong(payload.get("ticketId").toString());
        Long userId = Long.parseLong(payload.get("userId").toString());
        String text = payload.get("text").toString();

        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
            
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comment comment = new Comment();
        comment.setText(text);
        comment.setTicket(ticket);
        comment.setUser(user);
        
        commentRepository.save(comment);

        // Opcional: Si el flujo requiere cambiar el estado del ticket a EN_PROCESO
        // ticket.setStatus("EN_PROCESO");
        // ticketRepository.save(ticket);

        return ResponseEntity.ok().body(Map.of("message", "Nota técnica agregada con éxito"));
    }
}
