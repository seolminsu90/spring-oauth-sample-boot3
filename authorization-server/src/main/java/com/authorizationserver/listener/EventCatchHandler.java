package com.authorizationserver.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Component;

@Component
public class EventCatchHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EventListener
    public void eventHandler(AbstractAuthenticationEvent event) {
        logger.info("이벤트 발행 {}", event.toString());
    }
}
