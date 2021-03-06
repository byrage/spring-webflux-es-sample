package sample.webfluxes.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticsearchConfig {

    final private SearchClientNodeProperty searchClientNodeProperty;

    public ElasticsearchConfig(SearchClientNodeProperty searchClientNodeProperty) {

        this.searchClientNodeProperty = searchClientNodeProperty;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(SearchClientNodeProperty property) {

        log.debug("host={}", property.getHostNames());
        HttpHost[] httpHosts = property.getHostNames().stream().map(host -> new HttpHost(host, property.getPort())).toArray(HttpHost[]::new);

        return new RestHighLevelClient(RestClient.builder(httpHosts)
                                                 .setRequestConfigCallback(requestConfigBuilder -> {
                                                     requestConfigBuilder.setConnectTimeout(property.getConnectTimeoutMills());
                                                     requestConfigBuilder.setConnectionRequestTimeout(property.getConnectionRequestTimeoutMills());
                                                     requestConfigBuilder.setSocketTimeout(property.getSocketTimeoutMills());
                                                     return requestConfigBuilder;
                                                 }));
    }
}
