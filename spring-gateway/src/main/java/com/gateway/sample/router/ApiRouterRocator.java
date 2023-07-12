package com.gateway.sample.router;

import com.gateway.sample.entity.ApiRoute;
import com.gateway.sample.service.ApiRouteService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ApiRouterRocator implements RouteLocator {

    private final ApiRouteService apiRouteService;
    private final RouteLocatorBuilder routeLocatorBuilder;

    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
        return apiRouteService.findApiRoutes()
                .map(apiRoute -> routesBuilder.route(
                                String.valueOf(apiRoute.getId()),
                                predicateSpec -> setPredicateSpec(apiRoute, predicateSpec)
                        )
                )
                .collectList()
                .flatMapMany(builders -> routesBuilder.build().getRoutes());
    }

    private Buildable<Route> setPredicateSpec(ApiRoute apiRoute, PredicateSpec predicateSpec) {
        BooleanSpec booleanSpec = predicateSpec.path(apiRoute.getPath());
        if (!StringUtils.isEmpty(apiRoute.getMethod())) {
            booleanSpec.and()
                    .method(apiRoute.getMethod());
        }
        return booleanSpec.uri(apiRoute.getUri());
    }
}
