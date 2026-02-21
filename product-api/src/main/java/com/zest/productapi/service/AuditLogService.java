package com.zest.productapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditLogService {

    @Async("auditLogTaskExecutor")
    public void logProductAction(String action, Integer productId, String productName, String user) {
        try {
            // Simulate a time-consuming background task like sending an email or saving to
            // an external audit system
            Thread.sleep(2000);
            log.info("AUDIT LOG [ASYNC]: User '{}' performed action '{}' on Product ID: {} (Name: {})",
                    user, action, productId, productName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Audit log process interrupted", e);
        }
    }
}
