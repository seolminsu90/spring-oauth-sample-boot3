package com.gateway.sample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayRouteService {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void refreshRoutes() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        log.info("Gateway Route is Refreshed !");

        // route refresh event 발행
        // CachingRouteLocator.onApplicationEvent 참조. (event가 수신되면, 내부의 ConcurrentHashMap을 갱신한다)
    }
}
