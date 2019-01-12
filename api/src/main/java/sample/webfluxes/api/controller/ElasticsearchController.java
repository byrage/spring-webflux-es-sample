package sample.webfluxes.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sample.webfluxes.api.service.ReadService;
import sample.webfluxes.fullindexer.service.FullIndexService;

@Slf4j
@RestController
public class ElasticsearchController {

    private final ReadService readService;
    private final FullIndexService fullIndexService;

    public ElasticsearchController(ReadService readService, FullIndexService fullIndexService) {

        this.readService = readService;
        this.fullIndexService = fullIndexService;
    }

    @GetMapping("/{index}")
    public Mono<String> indexMapping(@PathVariable String index) {

        return readService.indexMapping(index);
    }

    @GetMapping("/{index}/{id}")
    public Mono<String> docs(@PathVariable String index, @PathVariable String id) {

        return readService.docs(index, id);
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
