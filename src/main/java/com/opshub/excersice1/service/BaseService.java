package com.opshub.excersice1.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public abstract class BaseService<T> {

    protected final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    protected JiraService jiraService;

    @Getter @Setter
    protected String apiBaseUrl;
    protected String apiUsername;
    protected String apiPassword;

    public enum AuthType {
        BASIC,
        BEARER
    }

    protected abstract void init();

    // Generic createHeaders method to handle different types of authentication
    protected HttpHeaders createHeaders(AuthType authType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        switch (authType) {
            case BASIC:
                String basicAuth = apiUsername + ":" + apiPassword;
                byte[] encodedAuth = java.util.Base64.getEncoder().encode(basicAuth.getBytes());
                String authHeader = "Basic " + new String(encodedAuth);
                headers.set("Authorization", authHeader);
                break;

            case BEARER:
                // If you're using OAuth2 or a Bearer token for authentication
                headers.set("Authorization", "Bearer " + apiPassword);  // Assuming apiPassword is the Bearer token
                break;

            default:
                throw new IllegalArgumentException("Unsupported authentication type");
        }

        return headers;
    }

//    public ResponseEntity<String> createResource(String urlPath, T resource, AuthType authType) {
//        String url = apiBaseUrl + urlPath;
//        HttpEntity<T> request = new HttpEntity<>(resource, createHeaders(authType));
//        return restTemplate.postForEntity(url, request, String.class);
//    }

    public ResponseEntity<String> createResource(String urlPath, T resource, AuthType authType) {
        HttpEntity<T> request = new HttpEntity<>(resource, createHeaders(authType));
        return restTemplate.exchange(apiBaseUrl+urlPath, HttpMethod.POST, request, String.class);
    }

    public ResponseEntity<String> getSingleResource(String urlPath, AuthType authType) {
        String url = apiBaseUrl + urlPath;
        HttpEntity<String> request = new HttpEntity<>(createHeaders(authType));
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public ResponseEntity<String> getAllResource(String urlPath, AuthType authType) {
        String url = apiBaseUrl + urlPath;
        HttpEntity<String> request = new HttpEntity<>(createHeaders(authType));
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public ResponseEntity<String> updateResource(String urlPath, Map<String, Object> fields, AuthType authType) {
        String url = apiBaseUrl + urlPath;
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(fields, createHeaders(authType));
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }
}

