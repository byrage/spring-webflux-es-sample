package sample.webfluxes;

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
    public void getIndexMapping_success() {

        webTestClient.get().uri("/{index}", "shop")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(String.class);

    }

    @Test
    public void getIndexMapping_fail() {

        webTestClient.get().uri("/{index}", "no_index")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .is4xxClientError();

    }

    @Test
    public void getDocs() {

        webTestClient.get().uri("/{index}/{id}", "shop", "1")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(String.class)
                     .isEqualTo("{\"name\":\"test\",\"like\":\"spring\"}");
    }

    @Test
    public void createIndex() {

        webTestClient.post().uri("/{index}", "shop232311")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();

    }

    @Test
    public void createDocument() {

        webTestClient.post().uri("/{index}/{id}", "shop", "1234")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();

    }
}