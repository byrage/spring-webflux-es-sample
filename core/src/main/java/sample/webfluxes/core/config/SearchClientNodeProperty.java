package sample.webfluxes.core.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchClientNodeProperty {

    private List<String> hostNames;
    private int port;
    private int connectTimeoutMills;
    private int socketTimeoutMills;
    private int connectionRequestTimeoutMills;
}
