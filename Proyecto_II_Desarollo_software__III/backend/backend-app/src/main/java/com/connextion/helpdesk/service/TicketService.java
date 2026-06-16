package com.connextion.helpdesk.service;

import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.enums.TicketStatus;
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
        ticket.setStatus(TicketStatus.ABIERTO);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket #" + id + " not found"));
    }

    public List<Ticket> getTicketsByClientId(Long clientId) {
        return ticketRepository.findByClientId(clientId);
    }

    public Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (newStatus == TicketStatus.RESUELTO && ticket.getStatus() != TicketStatus.EN_PROCESO) {
            throw new RuntimeException(
                    "Solo se puede resolver una solicitud que esté En Proceso. Estado actual: "
                    + ticket.getStatus());
        }

        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);
        ticketRepository.flush();
        return ticket;
    }
}
