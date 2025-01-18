package com.opshub.excersice1.controller;

import com.opshub.excersice1.model.JiraIssue;
import com.opshub.excersice1.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> getAllIssues(@RequestParam String jql) {
        return jiraService.getAllIssues(jql);
    }

    @GetMapping("/issue/{id}")
    public ResponseEntity<String> getIssue(@PathVariable String id) {
        return jiraService.getIssue(id);
    }

    @PutMapping("/issue/{id}")
    public ResponseEntity<String> updateIssue(@PathVariable String id, @RequestBody JiraIssue.Fields fields) {
        return jiraService.updateIssue(id, fields);
    }
}
