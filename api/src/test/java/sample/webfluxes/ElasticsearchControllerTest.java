package sample.webfluxes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElasticsearchControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String index = "shop";

    @Before
    public void setUp() {

        webTestClient.delete().uri("/{index}", index).exchange()
        .expectStatus().isOk();

    }

    @Test
    public void simpleTest() {

        webTestClient.get().uri("/test")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(String.class)
                     .isEqualTo("webfluxTest");

    }

    @Test
    public void createAndIndex() {

        // create index
        webTestClient.post().uri("/{index}", index)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();

        // get index info
        webTestClient.get().uri("/{index}", index)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(String.class);

        // index docs 1
        webTestClient.post().uri("/{index}/delta", index)
                     .contentType(MediaType.APPLICATION_JSON)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();

        // get docs
        webTestClient.get().uri("/{index}/{id}", index, "1")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(String.class)
                     .isEqualTo("{\"campaignId\":\"1\",\"shopNumber\":\"117722\",\"use\":true,\"block\":true,\"delete\":true,\"categoryCode\":\"1\",\"address\":\"강남역\",\"region3Code\":\"11680640\",\"realLocation\":{\"lat\":37.49799082,\"lon\":127.02779625},\"favoriteCount\":456,\"viewCount\":111,\"starAvgScore\":4.99,\"modificationDate\":\"2019-01-13T23:41:31.808\"}");

    }
}