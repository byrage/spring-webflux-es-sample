package sample.webfluxes.api.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sample.webfluxes.core.entity.Shop;

import java.io.IOException;

@Slf4j
@Service
public class MasterService {

    public final static String DEFAULT_MAPPING_TYPE = "_doc";

    private RestHighLevelClient client;

    public MasterService(RestHighLevelClient client) {

        this.client = client;
    }

    public Mono<String> indexInfo(String indexName) {

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

    public Mono<String> createIndex(String indexName) {

        String scheme;
        try {
            scheme = Shop.scheme();
        } catch (IOException e) {
            log.error("fetching scheme failed", e);
            return Mono.error(e);
        }

        CreateIndexRequest request = new CreateIndexRequest(indexName)
                .settings(Settings.builder()
                                  .put("index.number_of_shards", 1)
                                  .put("index.number_of_replicas", 0))
                .mapping("_doc", scheme, XContentType.JSON);

        return Mono.create(sink -> client.indices().createAsync(request, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {

            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {

                log.info("success. response={}", createIndexResponse);
                sink.success(createIndexResponse.toString());

            }

            @Override
            public void onFailure(Exception e) {

                log.error("fail.", e);
                sink.error(e);
            }
        }));
    }

    public Mono<String> getDocs(String index, String id) {

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
