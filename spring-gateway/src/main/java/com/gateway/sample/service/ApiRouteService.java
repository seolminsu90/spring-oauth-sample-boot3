package com.gateway.sample.service;

import com.gateway.sample.entity.ApiRoute;
import com.gateway.sample.model.CreateOrUpdateApiRouteRequest;
import com.gateway.sample.repository.ApiRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiRouteService {
    private final ApiRouteRepository apiRouteRepository;
    private final GatewayRouteService gatewayRouteService;

    public Flux<ApiRoute> findApiRoutes() {
        return apiRouteRepository.findAll();
    }

    public Mono<ApiRoute> findApiRoute(Long id) {
        return apiRouteRepository.findById(id);
    }

    public Mono<Void> createApiRoute(CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        ApiRoute route = ApiRoute.builder()
                .path(createOrUpdateApiRouteRequest.getPath())
                .uri(createOrUpdateApiRouteRequest.getUri())
                .method(createOrUpdateApiRouteRequest.getMethod())
                .build();

        return apiRouteRepository.save(route)
                .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
                .then();
    }

    public Mono<Void> updateApiRoute(Long id, CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        return findAndValidate(id).map(api -> {
                    api.setPath(createOrUpdateApiRouteRequest.getPath());
                    api.setUri(createOrUpdateApiRouteRequest.getUri());
                    api.setMethod(createOrUpdateApiRouteRequest.getMethod());
                    return api;
                })
                .flatMap(apiRouteRepository::save)
                .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
                .then();
    }

    public Mono<Void> deleteApiRoute(Long id) {
        return findAndValidate(id)
                .flatMap(apiRouteRepository::delete)
                .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
                .then();
    }

    private Mono<ApiRoute> findAndValidate(Long id) {
        return apiRouteRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("route is not found.")));
    }
}
