package sample.webfluxes.fullindexer.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.*;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sample.webfluxes.core.entity.Shop;
import sample.webfluxes.core.exception.BulkFailedException;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

@Slf4j
@Service
public class FullIndexService {

    private final RestHighLevelClient client;

    public FullIndexService(RestHighLevelClient client) {

        this.client = client;
    }

    public Mono<String> index(String index, int start, int end) {

        final String type = "_doc";

        BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);

        return Mono.create(sink -> {

            BulkProcessor.Listener listener = new BulkProcessor.Listener() {

                @Override
                public void beforeBulk(long executionId, BulkRequest request) {

                    int numberOfActions = request.numberOfActions();
                    log.debug("Executing bulk [{}] with {} requests", executionId, numberOfActions);
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                    if (response.hasFailures()) {
                        log.warn("Bulk [{}] executed with failures", executionId);
                        sink.error(new BulkFailedException());
                    } else {
                        log.info("Bulk [{}] completed in {} milliseconds", executionId, response.getTook().getMillis());
                        sink.success(response.toString());

                    }
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

                    log.error("Failed to execute bulk", failure);
                    sink.error(failure);
                }
            };

            BulkProcessor bulkProcessor = BulkProcessor.builder(bulkConsumer, listener)
                                                       .setBulkActions(50)
                                                       .setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB))
                                                       .setConcurrentRequests(0)
                                                       .setFlushInterval(TimeValue.timeValueSeconds(10L))
                                                       .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3))
                                                       .build();

            IntStream.range(start, end + 1).forEach(
                    num -> {
                        try {
                            Shop shop = Shop.dummy(String.valueOf(num));
                            bulkProcessor.add(new IndexRequest(index, type, shop.getCampaignId()).source(shop.source()));
                        } catch (IOException e) {
                            log.error("xContent build error. id={}", num, e);
                        }
                    });
        });
    }

    public Mono<String> indexWithBulkRequest(String index, int start, int end) {

        final String type = "_doc";

        BulkRequest bulkRequest = Requests.bulkRequest()
                                          .timeout(TimeValue.timeValueSeconds(30))
                                          .waitForActiveShards(ActiveShardCount.DEFAULT)
                                          .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        IntStream.range(start, end + 1).forEach(
                num -> bulkRequest.add(createIndexRequest(index, type, String.valueOf(num)))
        );

        ActionRequestValidationException exception = bulkRequest.validate();
        if (Objects.nonNull(exception)) {
            log.error("bulk exception", exception);
        }

        return Mono.create(sink -> client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {

            @Override
            public void onResponse(BulkResponse bulkItemResponses) {

                if (bulkItemResponses.hasFailures()) {
                    log.error("bulkItemResponse has failures. message={}", bulkItemResponses.buildFailureMessage());
                    sink.error(new BulkFailedException());
                }
                log.info("index request completed. took={}, full response={}", bulkItemResponses.getTook(), bulkItemResponses.getItems());
                sink.success(bulkItemResponses.toString());
            }

            @Override
            public void onFailure(Exception e) {

                log.info("index request failed.", e);
                sink.error(e);
            }
        }));
    }

    private IndexRequest createIndexRequest(String index, String type, String id) {

        XContentBuilder source;
        try {
            source = Shop.dummy(id).source();
        } catch (IOException e) {
            log.error("xContent build error.", e);
            return null;
        }

        return Requests.indexRequest(index)
                       .type(type)
                       .id(id)
                       .source(source)
                       .opType(DocWriteRequest.OpType.INDEX) // CREATE OR UPDATE
                       .timeout(TimeValue.timeValueSeconds(3));

    }

}
