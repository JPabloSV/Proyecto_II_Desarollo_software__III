package com.connextion.helpdesk.thread;

import com.connextion.helpdesk.model.Ticket;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.model.enums.TicketStatus;
import com.connextion.helpdesk.repository.TicketRepository;
import com.connextion.helpdesk.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TicketAssignmentTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TicketAssignmentTask.class);

    private final Long ticketId;
    private final Long technicianId;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private volatile boolean completed = false;
    private volatile String result = "";

    public TicketAssignmentTask(Long ticketId, Long technicianId,
                                TicketRepository ticketRepository,
                                UserRepository userRepository) {
        this.ticketId = ticketId;
        this.technicianId = technicianId;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        logger.info("[Thread: {}] Starting assignment of ticket #{} to technician #{}",
                threadName, ticketId, technicianId);

        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket #" + ticketId + " not found"));

            User technician = userRepository.findById(technicianId)
                    .orElseThrow(() -> new RuntimeException("Technician #" + technicianId + " not found"));

            ticket.setTechnician(technician);
            ticket.setStatus(TicketStatus.ASIGNADO);
            ticketRepository.save(ticket);

            result = "Ticket #" + ticketId + " assigned to " + technician.getName()
                    + " by thread " + threadName;
            completed = true;

            logger.info("[Thread: {}] Assignment completed — {}", threadName, result);

        } catch (Exception e) {
            result = "Error assigning ticket #" + ticketId + ": " + e.getMessage();
            logger.error("[Thread: {}] Assignment failed — {}", threadName, e.getMessage());
        }
    }

    public boolean isCompleted() { return completed; }
    public String getResult() { return result; }
}
