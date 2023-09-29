package com.example.homeworkspringboot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    private static final GenericContainer<?> devapp = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);
    private static final GenericContainer<?> prodapp = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    public void devContextLoads() {
        int devPort = devapp.getMappedPort(8080);

        ResponseEntity<String> devappEntity = restTemplate.getForEntity("http://localhost:" + devPort + "/profile",
                String.class);

        Assertions.assertEquals(devappEntity.getBody(), "Current profile is dev");
    }

    @Test
    public void prodContextLoads() {
        int prodPort = prodapp.getMappedPort(8081);

        ResponseEntity<String> prodappEntity = restTemplate.getForEntity("http://localhost:" + prodPort + "/profile",
                String.class);

        Assertions.assertEquals(prodappEntity.getBody(), "Current profile is production");
    }

}