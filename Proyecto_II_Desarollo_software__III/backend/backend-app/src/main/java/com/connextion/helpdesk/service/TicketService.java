package com.connextion.helpdesk.service;

import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.repository.TicketRepository;
import com.connextion.helpdesk.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus("OPEN");
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket assignTechnician(Long ticketId, Long technicianId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        if (!"TECHNICIAN".equals(technician.getRole()) && !"ADMIN".equals(technician.getRole())) {
            throw new RuntimeException("Assigned user must be a TECHNICIAN or an ADMIN");
        }

        ticket.setTechnician(technician);
        ticket.setStatus("IN_PROGRESS");

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId, String newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(newStatus.toUpperCase());
        return ticketRepository.save(ticket);
    }
}
