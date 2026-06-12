package com.connextion.helpdesk.service;

import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.model.enums.TicketStatus;
import com.connextion.helpdesk.model.enums.UserRole;
import com.connextion.helpdesk.repository.TicketRepository;
import com.connextion.helpdesk.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        // CORREGIDO: Uso del Enum estructural TicketStatus
        ticket.setStatus(TicketStatus.ABIERTO);
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


        String role = technician.getRole();
        if (!UserRole.TECHNICIAN.name().equals(role) && !UserRole.SUPERVISOR.name().equals(role)) {
            throw new RuntimeException("Assigned user must be a TECHNICIAN or a SUPERVISOR");
        }

        ticket.setTechnician(technician);
        ticket.setStatus(TicketStatus.EN_PROCESO);
        return ticketRepository.save(ticket);
    }


   public Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus) {
    Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
    ticket.setStatus(newStatus);
    ticketRepository.save(ticket);
    ticketRepository.flush(); 
    return ticket;
}
}