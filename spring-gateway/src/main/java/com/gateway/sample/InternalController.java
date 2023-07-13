package com.gateway.sample;

import com.gateway.sample.entity.ApiRoute;
import com.gateway.sample.model.ApiRouteResponse;
import com.gateway.sample.model.CreateOrUpdateApiRouteRequest;
import com.gateway.sample.service.ApiRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {
    private final ApiRouteService apiRouteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ApiRouteResponse>> findApiRoutes() {
        return apiRouteService.findApiRoutes()
                .map(this::convertApiRouteIntoApiRouteResponse)
                .collectList();
        // .subscribeOn(Schedulers.boundedElastic()); for Blocking I/O (default : parallel)
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiRouteResponse> findApiRoute(@PathVariable Long id) {
        return apiRouteService.findApiRoute(id)
                .map(this::convertApiRouteIntoApiRouteResponse);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<?> createApiRoute(@RequestBody @Validated CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        return apiRouteService.createApiRoute(createOrUpdateApiRouteRequest);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<?> updateApiRoute(@PathVariable Long id, @RequestBody @Validated CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        return apiRouteService.updateApiRoute(id, createOrUpdateApiRouteRequest);
    }

    @DeleteMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<?> deleteApiRoute(@PathVariable Long id) {
        return apiRouteService.deleteApiRoute(id);
    }

    private ApiRouteResponse convertApiRouteIntoApiRouteResponse(ApiRoute apiRoute) {
        return ApiRouteResponse.builder()
                .id(apiRoute.getId())
                .path(apiRoute.getPath())
                .method(apiRoute.getMethod())
                .uri(apiRoute.getUri())
                .build();
    }
}
