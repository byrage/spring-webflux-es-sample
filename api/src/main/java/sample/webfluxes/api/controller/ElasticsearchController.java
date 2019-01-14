package sample.webfluxes.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sample.webfluxes.api.service.MasterService;
import sample.webfluxes.deltaindexer.service.DeltaIndexService;
import sample.webfluxes.fullindexer.service.FullIndexService;

@Slf4j
@RestController
public class ElasticsearchController {

    private final MasterService masterService;
    private final FullIndexService fullIndexService;
    private final DeltaIndexService deltaIndexService;

    public ElasticsearchController(MasterService masterService, FullIndexService fullIndexService, DeltaIndexService deltaIndexService) {

        this.masterService = masterService;
        this.fullIndexService = fullIndexService;
        this.deltaIndexService = deltaIndexService;
    }

    @GetMapping("/{index}")
    public Mono<String> getIndexMapping(@PathVariable String index) {

        return masterService.indexInfo(index);
    }

    @PostMapping("/{index}")
    public Mono<String> createIndex(@PathVariable String index) {

        return masterService.createIndex(index);
    }

    @GetMapping("/{index}/{id}")
    public Mono<String> getDocs(@PathVariable String index, @PathVariable String id) {

        return masterService.getDocs(index, id);
    }

    @PostMapping("/{index}/full")
    public Mono<String> fullIndex(@PathVariable String index, @RequestBody String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        String startId = jsonObject.getString("startId");
        String endId = jsonObject.getString("endId");

        int start = startId == null ? 0 : Integer.parseInt(startId);
        int end = endId == null ? 10000 : Integer.parseInt(endId);

        return fullIndexService.index(index, start, end);
    }

    @PostMapping("/{index}/delta")
    public Mono<String> deltaIndex(@PathVariable String index, @RequestBody String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        String id = jsonObject.getString("id");

        return deltaIndexService.index(index, id);
    }

/*    @PostMapping("/{index}/search")
    public Mono<String> search(@PathVariable String index, @RequestBody SearchDto searchDto) {

        return masterService.search(index, searchDto);
    }*/


    @GetMapping("/test")
    public Mono<String> test() {

        return Mono.just("webfluxTest");
    }
}
