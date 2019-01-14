package sample.webfluxes.fullindexer.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sample.webfluxes.core.entity.Shop;
import sample.webfluxes.core.exception.BulkFailedException;

import java.io.IOException;
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

        return Mono.create(sink -> {

            BulkProcessor.Listener listener = new BulkProcessor.Listener() {

                @Override
                public void beforeBulk(long executionId, BulkRequest request) {

                    int numberOfActions = request.numberOfActions();
                    log.debug("Executing bulk [{}] with {} requests",
                              executionId, numberOfActions);
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                    if (response.hasFailures()) {
                        log.warn("Bulk [{}] executed with failures", executionId);
                        sink.error(new BulkFailedException());
                    } else {
                        log.info("Bulk [{}] completed in {} milliseconds",
                                 executionId, response.getTook().getMillis());
                        sink.success(response.toString());

                    }
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

                    log.error("Failed to execute bulk", failure);
                    sink.error(new BulkFailedException());
                }
            };

            BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer =
                    (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);
            BulkProcessor bulkProcessor = BulkProcessor.builder(bulkConsumer, listener)
                                                       .setBulkActions(500)
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

}
