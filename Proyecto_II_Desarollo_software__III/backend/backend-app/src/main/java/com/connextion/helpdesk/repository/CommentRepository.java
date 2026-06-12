package com.connextion.helpdesk.repository;

import com.connextion.helpdesk.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Buscar toda la bitácora ordenada cronológicamente de un ticket específico
    List<Comment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}