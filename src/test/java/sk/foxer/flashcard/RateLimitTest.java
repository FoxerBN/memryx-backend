package sk.foxer.flashcard;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class RateLimitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void testRateLimit() {
        String url = "/api/auth/login";
        int rateLimitCount = 0;

        // Pošli 25 požiadaviek
        for (int i = 1; i <= 25; i++) {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url,
                    Map.of("username", "test", "password", "test"),
                    String.class
            );

            System.out.println("Request " + i + ": " + response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                rateLimitCount++;
            }
        }

        // Skontroluj, že aspoň niektoré požiadavky boli rate limited
        assertThat(rateLimitCount).isGreaterThan(0);
        System.out.println("Rate limited requests: " + rateLimitCount);
    }
}