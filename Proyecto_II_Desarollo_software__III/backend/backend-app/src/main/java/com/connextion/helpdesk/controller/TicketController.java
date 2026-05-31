package com.connextion.helpdesk.controller;

import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // GET http://localhost:8080/api/tickets
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // POST http://localhost:8080/api/tickets
    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket created = ticketService.createTicket(ticket);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT http://localhost:8080/api/tickets/{ticketId}/assign/{technicianId}
    @PutMapping("/{ticketId}/assign/{technicianId}")
    public ResponseEntity<?> assignTechnician(@PathVariable Long ticketId, @PathVariable Long technicianId) {
        try {
            Ticket updated = ticketService.assignTechnician(ticketId, technicianId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PATCH http://localhost:8080/api/tickets/{ticketId}/status?status=RESOLVED
    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long ticketId, @RequestParam String status) {
        try {
            Ticket updated = ticketService.updateTicketStatus(ticketId, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}