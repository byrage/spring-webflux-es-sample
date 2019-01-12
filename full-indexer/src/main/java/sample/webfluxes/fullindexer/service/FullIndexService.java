package sample.webfluxes.fullindexer.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.*;
import org.elasticsearch.action.index.*;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sample.webfluxes.core.dto.ShopDto;

import java.io.IOException;

@Slf4j
@Service
public class FullIndexService {

    private final RestHighLevelClient client;

    public FullIndexService(RestHighLevelClient client) {

        this.client = client;
    }

    public Mono<String> shopFullIndex(String index, String id) {

        final String type = "_doc";
        XContentBuilder shopXContent = null;
        try {
            shopXContent = ShopDto.dummy(id).xContent();
            log.info("shopXContent={}", shopXContent);
        } catch (IOException e) {
            log.error("xContentBuilder error.", e);
        }

        IndexRequest request = Requests.indexRequest(index)
                                       .type(type)
                                       .id(id)
                                       .source(shopXContent)
                                       .opType(DocWriteRequest.OpType.INDEX)
                                       .timeout(TimeValue.timeValueSeconds(3));

        return Mono.create(
                sink -> client.indexAsync(
                        request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {

                            @Override
                            public void onResponse(IndexResponse response) {

                                ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
                                if (shardInfo.getFailed() > 0) {
                                    for (ReplicationResponse.ShardInfo.Failure failure :
                                            shardInfo.getFailures()) {
                                        log.error("index failed. failure={}", failure);
                                    }
                                }

                                log.info("index request completed. result={}, full response={}", response.getResult(), response);
                                sink.success(response.toString());
                            }

                            @Override
                            public void onFailure(Exception e) {

                                log.info("index request failed.", e);
                                sink.error(e);
                            }
                        }
                )
        );
    }
}
