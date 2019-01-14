package sample.webfluxes.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "elasticsearch.client.search")
public class SearchClientNodeProperty {

    private List<String> hostNames;
    private int port;
    private int connectTimeoutMills;
    private int socketTimeoutMills;
    private int connectionRequestTimeoutMills;
}
