package com.opshub.excersice1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JiraIssue {

    private Fields fields;

    @Getter
    @Setter
    public static class Fields {
        private Project project;
        private String summary;
        private String description;
        private IssueType issuetype;
    }

    @Getter
    @Setter
    public static class Project {
        private String key;
    }

    @Getter
    @Setter
    public static class IssueType {
        private String name;
    }


}
