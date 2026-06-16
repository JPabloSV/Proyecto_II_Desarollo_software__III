package com.connextion.helpdesk.controller;

import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.enums.TicketStatus;
import com.connextion.helpdesk.service.TicketService;
import com.connextion.helpdesk.thread.TicketAssignmentPool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketAssignmentPool assignmentPool;

    public TicketController(TicketService ticketService, TicketAssignmentPool assignmentPool) {
        this.ticketService = ticketService;
        this.assignmentPool = assignmentPool;
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getTicketsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(ticketService.getTicketsByClientId(clientId));
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            Ticket created = ticketService.createTicket(ticket);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{ticketId}/assign/{technicianId}")
    public ResponseEntity<?> assignTechnician(@PathVariable Long ticketId,
            @PathVariable Long technicianId) {
        try {
            Future<String> future = assignmentPool.submitAssignment(ticketId, technicianId);

            String threadResult = future.get(5, TimeUnit.SECONDS);

            Ticket updated = ticketService.getTicketById(ticketId);
            return ResponseEntity.ok(Map.of(
                    "ticket", updated,
                    "threadResult", threadResult
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error during concurrent assignment: " + e.getMessage());
        }
    }

 
    @PutMapping("/{technicianId}/assign-batch")
    public ResponseEntity<?> assignBatch(@PathVariable Long technicianId,
            @RequestBody Long[] ticketIds) {
        assignmentPool.submitBatchAssignment(ticketIds, technicianId);
        return ResponseEntity.ok(Map.of(
                "message", ticketIds.length + " tickets submitted for concurrent assignment"
        ));
    }


    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long ticketId,
            @RequestParam String status) {
        try {
            TicketStatus newStatus = TicketStatus.fromString(status);
            Ticket updated = ticketService.updateTicketStatus(ticketId, newStatus);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido: " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
