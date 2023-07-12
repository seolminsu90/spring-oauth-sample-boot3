package com.gateway.sample.service;

import com.gateway.sample.entity.ApiRoute;
import com.gateway.sample.repository.ApiRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ApiRouteService {
    private final ApiRouteRepository apiRouteRepository;

    public Flux<ApiRoute> findApiRoutes() {
        return apiRouteRepository.findAll();
    }
}
