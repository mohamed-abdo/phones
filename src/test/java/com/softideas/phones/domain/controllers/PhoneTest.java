package com.softideas.phones.domain.controllers;

import com.softideas.phones.domain.models.UploadStats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class PhoneTest {

    final String CONTENT_TYPE = "multipart/form-data; boundary=200MB";

    @LocalServerPort
    int randomPortNumber;


    @Value("classpath:${file.import-test-data}")
    private Resource resource;


    @Test
    void test_ping() throws URISyntaxException {
        final var baseUrl = String.format("http://localhost:%d/phones/ping", randomPortNumber);
        final URI uri = new URI(baseUrl);
        final var restTemplate = new RestTemplate();
        var response = restTemplate.getForEntity(uri, String.class);
        Assertions.assertSame(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void test_importFile() throws URISyntaxException, IOException {
        final var baseUrl = String.format("http://localhost:%d/phones/import", randomPortNumber);
        final URI uri = new URI(baseUrl);
        final var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.add("content-type", CONTENT_TYPE);
        var body = new LinkedMultiValueMap<String, Resource>();
        body.add("file", resource);
        var httpBody = new HttpEntity<>(body, headers);
        var response = restTemplate.postForEntity(uri, httpBody, UploadStats.class);
        var expectedFunc = Stream.of(HttpStatus.CREATED, HttpStatus.OK);
        Assertions.assertTrue(expectedFunc.anyMatch(s -> s.equals(response.getStatusCode())));
    }

    @Test
    void test_getFileStats() throws URISyntaxException {
        final var baseUrl = String.format("http://localhost:%d/phones/file/%s", randomPortNumber, UUID.randomUUID());
        final URI uri = new URI(baseUrl);
        final var restTemplate = new RestTemplate();
        try {
            restTemplate.getForEntity(uri, UploadStatusResource.class);
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            Assertions.assertSame(HttpStatus.NOT_FOUND, ex.getStatusCode());

        }
    }

    @Test
    void test_ValidateNumber() throws URISyntaxException {
        final var baseUrl = String.format("http://localhost:%d/phones/validate/%s", randomPortNumber, "750023242");
        final URI uri = new URI(baseUrl);
        final var restTemplate = new RestTemplate();
        var response = restTemplate.getForEntity(uri, PhoneNumberResource.class);
        Assertions.assertSame(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void test_importFile_no_file() throws URISyntaxException, IOException {
        final var baseUrl = String.format("http://localhost:%d/phones/import", randomPortNumber);
        final URI uri = new URI(baseUrl);
        final var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.add("content-type", CONTENT_TYPE);
        var body = new LinkedMultiValueMap<String, Resource>();
        var httpBody = new HttpEntity<>(body, headers);
        try {
            restTemplate.postForEntity(uri, httpBody, UploadStats.class);
            Assertions.fail();
        } catch (HttpClientErrorException ex) {
            Assertions.assertSame(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        }

    }
}