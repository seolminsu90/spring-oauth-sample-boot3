package com.gateway.sample.repository;


import com.gateway.sample.entity.ApiRoute;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ApiRouteRepository extends ReactiveCrudRepository<ApiRoute, Long> {

}
