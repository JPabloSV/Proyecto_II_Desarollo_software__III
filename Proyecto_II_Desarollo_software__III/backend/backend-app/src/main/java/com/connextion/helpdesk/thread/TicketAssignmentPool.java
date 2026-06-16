package com.connextion.helpdesk.thread;

import com.connextion.helpdesk.repository.TicketRepository;
import com.connextion.helpdesk.repository.UserRepository;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class TicketAssignmentPool {

    private static final Logger logger = LoggerFactory.getLogger(TicketAssignmentPool.class);
    private static final int POOL_SIZE = 5;

    private final ExecutorService executorService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketAssignmentPool(TicketRepository ticketRepository,
                                UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.executorService = Executors.newFixedThreadPool(POOL_SIZE);
        logger.info("TicketAssignmentPool initialized with {} threads", POOL_SIZE);
    }

  
    public Future<String> submitAssignment(Long ticketId, Long technicianId) {
        logger.info("Submitting assignment — ticket #{} → technician #{}", ticketId, technicianId);

        return executorService.submit(() -> {
            TicketAssignmentTask task = new TicketAssignmentTask(
                    ticketId, technicianId, ticketRepository, userRepository);
            task.run();
            return task.getResult();
        });
    }

  
    public void submitBatchAssignment(Long[] ticketIds, Long technicianId) {
        logger.info("Submitting batch assignment of {} tickets to technician #{}",
                ticketIds.length, technicianId);

        for (Long ticketId : ticketIds) {
            executorService.submit(new TicketAssignmentTask(
                    ticketId, technicianId, ticketRepository, userRepository));
        }
    }

  
    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down TicketAssignmentPool...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
