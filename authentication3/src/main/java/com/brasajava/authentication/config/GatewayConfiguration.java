package com.brasajava.authentication.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfiguration {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route("message_route", r -> r.path("/message/**").uri("http://localhost:8081"))
        .route(
            "person_route",
            r -> r.path("/person/**").and().method(HttpMethod.POST).uri("http://localhost:8082"))
        .route(
            "person_host_route",
            r -> r.path("/person/**").and().host("myhost").uri("http://localhost:8082"))
        .route(
            "person_host_add_header",
            r ->
                r.path("/person/**")
                    .and()
                    .host("needHeader")
                    .filters(
                        f -> {
                          f.addRequestHeader("X-TestHeader", "rewrite_request");
                          f.addRequestParameter("foo", "bar");
                          return f;
                        })
                    .uri("http://localhost:8082"))
        .route(
            "person_host_add_params",
            r ->
                r.path("/person/**")
                    .and()
                    .host("needParams")
                    .filters(
                        f -> {
                          f.addRequestParameter("foo", "fooRequest");
                          f.addRequestParameter("bar", "barRequest");
                          return f;
                        })
                    .uri("http://localhost:8082"))
        .build();
  }
}
