package com.connextion.helpdesk.repository;

import com.connextion.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatus(String status);


    List<Ticket> findByClientId(Long clientId);


    List<Ticket> findByTechnicianId(Long technicianId);
}