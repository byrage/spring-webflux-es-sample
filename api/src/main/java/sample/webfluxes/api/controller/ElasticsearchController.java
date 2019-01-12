package sample.webfluxes.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sample.webfluxes.api.service.MasterService;
import sample.webfluxes.fullindexer.service.FullIndexService;

@Slf4j
@RestController
public class ElasticsearchController {

    private final MasterService masterService;
    private final FullIndexService fullIndexService;

    public ElasticsearchController(MasterService masterService, FullIndexService fullIndexService) {

        this.masterService = masterService;
        this.fullIndexService = fullIndexService;
    }

    @GetMapping("/{index}")
    public Mono<String> indexMapping(@PathVariable String index) {

        return masterService.indexInfo(index);
    }

    @PostMapping("/{index}")
    public Mono<String> createIndex(@PathVariable String index) {

        return masterService.createIndex(index);
    }

    @GetMapping("/{index}/{id}")
    public Mono<String> docs(@PathVariable String index, @PathVariable String id) {

        return masterService.docs(index, id);
    }

    @PostMapping("/{index}/{id}")
    public Mono<String> index(@PathVariable String index, @PathVariable String id) {

       return fullIndexService.shopFullIndex(index, id);
    }

    @GetMapping("/test")
    public Mono<String> test() {

        return Mono.just("webfluxTest");
    }
}
