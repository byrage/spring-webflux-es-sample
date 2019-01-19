package sample.webfluxes.api.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sample.webfluxes.api.dto.SearchParam;
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

    public Mono<String> search(String index, SearchParam searchParam) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(searchParam.getPage().getOffset());
        searchSourceBuilder.size(searchParam.getPage().getSize());

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("regionCode", searchParam.getRegionCode()));
        boolQueryBuilder.filter(QueryBuilders.termQuery("categoryCode", searchParam.getCategoryCode()));
        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.sort(SortBuilders.fieldSort("campaignId").order(SortOrder.DESC));

        SearchRequest searchRequest = new SearchRequest().indices(index)
                                                         .searchType(SearchType.DEFAULT)
                                                         .source(searchSourceBuilder);

        return Mono.create(sink -> client.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {

            @Override
            public void onResponse(SearchResponse searchResponse) {

                log.info("success. response={}", searchResponse.toString());
                sink.success(searchResponse.toString());
            }

            @Override
            public void onFailure(Exception e) {

                log.error("fail.", e);
                sink.error(e);
            }
        }));
    }

    public Mono<String> delete(String index) {

        DeleteRequest request = new DeleteRequest(index);

        return Mono.create(sink -> {
            try {
                client.delete(request, RequestOptions.DEFAULT);
                sink.success();
            } catch (IOException e) {
                sink.error(e);
            }
        });
    }
}
