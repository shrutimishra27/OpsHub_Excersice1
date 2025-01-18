package com.opshub.excersice1.service;

import com.opshub.excersice1.model.JiraIssue;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class JiraService {


    @Value("${jira.api.baseurl}")
    private String jiraBaseUrl;

    @Value("${jira.api.username}")
    private String jiraUsername;

    @Value("${jira.api.password}")
    private String jiraPassword;

    @Value("${jira.api.projectKey}")
    private String jiraProjectKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders createHeaders() {
        String auth = jiraUsername + ":" + jiraPassword;
        byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", authHeader);
        return headers;
    }

    public ResponseEntity<String> createIssue(JiraIssue issue) {
        String url = jiraBaseUrl + "/issue";
        HttpEntity<JiraIssue> request = new HttpEntity<>(issue, createHeaders());
        return restTemplate.postForEntity(url, request, String.class);
    }

    public ResponseEntity<String> getAllIssues(String jql) {
        String url = jiraBaseUrl + "/search?jql=" + jql;
        HttpEntity<String> request = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public ResponseEntity<String> getIssue(String issueId) {
        String url = jiraBaseUrl + "/issue/" + jiraProjectKey+ "-" + issueId;
        HttpEntity<String> request = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public ResponseEntity<String> updateIssue(String issueId, JiraIssue.Fields fields) {
        String url = jiraBaseUrl + "/issue/" + jiraProjectKey + "-" + issueId;

        // Create a map to wrap the fields in the required structure
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fields", fields); // Ensure this matches the expected structure by Jira API

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }

}
