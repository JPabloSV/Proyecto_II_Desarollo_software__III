package com.connextion.helpdesk.controller;

import com.connextion.helpdesk.model.Comment;
import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.repository.CommentRepository;
import com.connextion.helpdesk.repository.TicketRepository;
import com.connextion.helpdesk.repository.UserRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired private CommentRepository commentRepository;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;


    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<?> getCommentsByTicket(@PathVariable Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
        return ResponseEntity.ok(comments);
    }

 
    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> payload) {
        Long ticketId = Long.parseLong(payload.get("ticketId").toString());
        Long userId   = Long.parseLong(payload.get("userId").toString());
        String text   = payload.get("text").toString();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comment comment = new Comment();
        comment.setText(text);
        comment.setTicket(ticket);
        comment.setUser(user);

        Comment saved = commentRepository.save(comment);

        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "text", saved.getText(),
                "timestamp", saved.getCreatedAt().toString(),
                "message", "Comentario agregado con éxito"
        ));
    }
}
