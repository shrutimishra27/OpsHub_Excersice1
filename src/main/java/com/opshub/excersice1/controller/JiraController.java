package com.opshub.excersice1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opshub.excersice1.model.JiraIssue;
import com.opshub.excersice1.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jira")
public class JiraController {

    @Autowired
    private JiraService jiraService;

    @PostMapping("/issue")
    public ResponseEntity<String> createIssue(@RequestBody JiraIssue issue) {
        return jiraService.createIssue(issue);
    }

    @GetMapping("/issues")
    public ResponseEntity<String> getAllIssues(@RequestParam String jql) throws JsonProcessingException {
        return jiraService.getAllIssues(jql);
    }

    @GetMapping("/issue/{project}-{id}")
    public ResponseEntity<String> getIssue(@PathVariable String project, @PathVariable String id) throws JsonProcessingException {
        return jiraService.getIssue(project, id);
    }

    @PutMapping("/issue/{id}")
    public ResponseEntity<String> updateIssue(@PathVariable String project, @PathVariable String id, @RequestBody JiraIssue.Fields fields) {
        return jiraService.updateIssue(project, id, fields);
    }
}
