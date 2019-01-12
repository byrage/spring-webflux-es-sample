package sample.webfluxes.api.functional.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sample.webfluxes.api.service.MasterService;

//@Component
@Slf4j
public class WebfluxHandler {

    MasterService elasticsearchService;

    public WebfluxHandler(MasterService elasticsearchService) {

        this.elasticsearchService = elasticsearchService;
    }

    public Mono<ServerResponse> getMappings(ServerRequest request) {

//        String index = request.pathVariable("index");
//        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
//                             .body(BodyInserters.fromObject(elasticsearchService.indexInfo(index).toString()));
        return null;
    }

    public Mono<ServerResponse> getDocs(ServerRequest request) {


//
//        String index = request.pathVariable("index");
//        String id = request.pathVariable("id");
//        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
//                             .body(BodyInserters.fromObject(elasticsearchService.docs(index, id)));
        return null;
    }

}
