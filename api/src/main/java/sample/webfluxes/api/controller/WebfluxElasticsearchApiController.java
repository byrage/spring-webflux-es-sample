package sample.webfluxes.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sample.webfluxes.core.service.ElasticsearchService;

@Slf4j
@RestController
public class WebfluxElasticsearchApiController {

    private final ElasticsearchService elasticsearchService;

    public WebfluxElasticsearchApiController(ElasticsearchService elasticsearchService) {

        this.elasticsearchService = elasticsearchService;
    }

    @GetMapping("/{index}")
    public Mono<String> indexMapping(@PathVariable String index) {

        return elasticsearchService.indexMapping(index);
    }

    @GetMapping(value = "/{index}/{id}")
    public Mono<String> docs(@PathVariable String index, @PathVariable String id) {

        return elasticsearchService.docs(index, id);
    }

    @GetMapping("/test")
    public Mono<String> test() {

        return Mono.just("webfluxTest");
    }
}
