package sample.webfluxes.api.functional.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.*;

//@Configuration
public class WebfluxRouter {

    @Bean
    public RouterFunction<ServerResponse> route(WebfluxHandler handler) {

        return RouterFunctions.route(RequestPredicates.GET("/{index}/mapping"), handler::getMappings)
                              .andRoute(RequestPredicates.GET("/{index}/{id}"), handler::getDocs);
    }
}
