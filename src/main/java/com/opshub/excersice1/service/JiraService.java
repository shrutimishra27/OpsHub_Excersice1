package com.opshub.excersice1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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

    Scanner scanner = new Scanner(System.in);

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

    public ResponseEntity<String> getAllIssues(String jiraProjectKey) throws JsonProcessingException {
        String url = jiraBaseUrl + "/search?jql=project=" + jiraProjectKey;
        HttpEntity<String> request = new HttpEntity<>(createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try{
            JsonNode responseNode = mapper.readTree(response.getBody());
            JsonNode issuesNode = responseNode.get("issues");
            if (issuesNode != null && issuesNode.isArray()) {
                int counter = 1;
                for (JsonNode issue : issuesNode) {
                    System.out.println("Issue #"+counter);
                    printIssueFromJson(issue);
                    counter++;
                }
            } else {
                System.out.println("Issues field is not an array or not found.");
                return null;
            }
        }
        catch (JsonProcessingException e){
            System.out.println("Unknown Json Processing error occured!");
        }

        return response;
    }

    public ResponseEntity<String> getIssue(String projectKey, String issueId) throws JsonProcessingException {
        String url = jiraBaseUrl + "/issue/" + projectKey+ "-" + issueId;
        HttpEntity<String> request = new HttpEntity<>(createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode responseNode = mapper.readTree(response.getBody());
            if (responseNode == null) {
                System.out.println("No issue related to the specified key was found!");
                return null;
            }
            printIssueFromJson(responseNode);

//            JsonNode keyNode = responseNode.get("key");
//            JsonNode issueTypeNode = responseNode.get("fields").get("issuetype").get("name");
//            JsonNode summaryNode = responseNode.get("fields").get("summary");
//            JsonNode descriptionNode = responseNode.get("fields").get("description");
//
//            System.out.println("\tIssue Key: " + (keyNode != null ? keyNode.asText() : "N/A") + "\t");
//            System.out.println("\tIssue Type: " + (issueTypeNode != null ? issueTypeNode.asText() : "N/A"));
//            System.out.println("\tIssue Summary: " + (summaryNode != null ? summaryNode.asText() : "N/A"));
//            System.out.println("\tIssue Description: " + (descriptionNode != null ? descriptionNode.asText() : "N/A"));
        } catch (JsonProcessingException e){
            System.out.println("Unknown Json Processing error occured!");
        }
        return response;
    }

    public ResponseEntity<String> updateIssue(String projectKey, String issueId, JiraIssue.Fields fields) {
        String url = jiraBaseUrl + "/issue/" + projectKey + "-" + issueId;

        // Create a map to wrap the fields in the required structure
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fields", fields);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, createHeaders());
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }

    public void printIssueFromJson(JsonNode issue){
        JsonNode keyNode = issue.get("key");
        JsonNode issueTypeNode = issue.get("fields").get("issuetype").get("name");
        JsonNode summaryNode = issue.get("fields").get("summary");
        JsonNode descriptionNode = issue.get("fields").get("description");

        System.out.println("\tIssue Key: " + (keyNode != null ? keyNode.asText() : "N/A") + "\t");
        System.out.println("\tIssue Type: " + (issueTypeNode != null ? issueTypeNode.asText() : "N/A"));
        System.out.println("\tIssue Summary: " + (summaryNode != null ? summaryNode.asText() : "N/A"));
        System.out.println("\tIssue Description: " + (descriptionNode != null ? descriptionNode.asText() : "N/A"));
    }

    public JiraIssue createJiraIssueFromInput(){
        System.out.println("Enter Project Key:");
        String projectKey = scanner.nextLine();
        System.out.println("Enter Summary:");
        String summary = scanner.nextLine();
        System.out.println("Enter Description:");
        String description = scanner.nextLine();
        System.out.println("Enter Issue Type:");
        String issueType = scanner.nextLine();

        JiraIssue issue = new JiraIssue();
        JiraIssue.Fields fields = new JiraIssue.Fields();
        JiraIssue.Project project = new JiraIssue.Project();
        JiraIssue.IssueType issueTypeName = new JiraIssue.IssueType();

        project.setKey(projectKey);
        fields.setProject(project);
        issueTypeName.setName(issueType);
        fields.setIssuetype(issueTypeName);
        fields.setSummary(summary);
        fields.setDescription(description);
        issue.setFields(fields);
        return issue;
    }
}
