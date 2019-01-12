package sample.webfluxes.api.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.*;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.client.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.IOException;

@Slf4j
@Service
public class ReadService {

    public final static String DEFAULT_MAPPING_TYPE = "_doc";

    private RestHighLevelClient client;

    public ReadService(RestHighLevelClient client) {

        this.client = client;
    }

    public Mono<String> indexMapping(String indexName) {

        return Mono.create(
                sink -> client.indices().getMappingAsync(new GetMappingsRequest().indices(indexName), RequestOptions.DEFAULT, new ActionListener<GetMappingsResponse>() {

            @Override
            public void onResponse(GetMappingsResponse response) {
                log.info("success. full={}, response={}", response.toString(), response);
                sink.success(response.toString());
            }

            @Override
            public void onFailure(Exception e) {
                log.error("fail.", e);
                sink.error(e);
            }
        }));
    }

    public Mono<String> docs(String index, String id) {

        GetRequest getRequest = new GetRequest().index(index).type(DEFAULT_MAPPING_TYPE).id(id);
        RequestOptions requestOptions = RequestOptions.DEFAULT;

        return Mono.create(sink -> client.getAsync(getRequest, requestOptions, new ActionListener<GetResponse>() {

            @Override
            public void onResponse(GetResponse documentFields) {
                log.info("success. response={}", documentFields.getSourceAsString());
                sink.success(documentFields.getSourceAsString());

            }

            @Override
            public void onFailure(Exception e) {
                log.error("fail.", e);
                sink.error(e);
            }
        }));
    }
}
