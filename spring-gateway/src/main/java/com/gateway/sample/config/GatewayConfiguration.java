package com.gateway.sample.config;

import com.gateway.sample.router.ApiRouterRocator;
import com.gateway.sample.service.ApiRouteService;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {
    @Bean
    public RouteLocator routeLocator(ApiRouteService apiRouteService,
                                     RouteLocatorBuilder routeLocatorBuilder) {
        return new ApiRouterRocator(apiRouteService, routeLocatorBuilder);
    }
}
