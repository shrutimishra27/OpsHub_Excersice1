package com.opshub.excersice1;

import com.opshub.excersice1.model.JiraIssue;
import com.opshub.excersice1.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;

import java.util.Scanner;


@SpringBootApplication
@ComponentScan(basePackages = "com.opshub.excersice1")
public class Excersice1Application implements CommandLineRunner {

	@Autowired
	private JiraService jiraService;

	public static void main(String[] args) {
		SpringApplication.run(Excersice1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\nSelect an option:");
			System.out.println("1. Create Issue");
			System.out.println("2. Get All Issues");
			System.out.println("3. Get Issue by ID");
			System.out.println("4. Update Issue");
			System.out.println("5. Exit");

			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
				case 1:
					createIssue(scanner);
					break;
				case 2:
					getAllIssues();
					break;
				case 3:
					getIssueById(scanner);
					break;
				case 4:
					updateIssue(scanner);
					break;
				case 5:
					System.exit(0);
				default:
					System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private void createIssue(Scanner scanner) {
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
		JiraIssue.IssueType type = new JiraIssue.IssueType();

		project.setKey(projectKey);
		fields.setProject(project);
		fields.setSummary(summary);
		fields.setDescription(description);
		type.setName(issueType);
		fields.setIssuetype(type);

		issue.setFields(fields);

		jiraService.createIssue(issue);
		System.out.println("Issue created successfully.");
	}

	private void getAllIssues() {
		System.out.println("Fetching all issues...");
		ResponseEntity<String> response = jiraService.getAllIssues("project=SP1");
		System.out.println(response.getBody());
	}

	private void getIssueById(Scanner scanner) {
		System.out.println("Enter Issue ID:");
		String issueId = scanner.nextLine();
		ResponseEntity<String> response = jiraService.getIssue(issueId);
		System.out.println(response.getBody());
	}

	private void updateIssue(Scanner scanner) {
		System.out.println("Enter Issue ID:");
		String issueId = scanner.nextLine();
		System.out.println("Enter Updated Summary:");
		String summary = scanner.nextLine();

		JiraIssue.Fields fields = new JiraIssue.Fields();
		fields.setSummary(summary);

		jiraService.updateIssue(issueId, fields);
		System.out.println("Issue updated successfully.");
	}
}
//main file bhot choti
//lombok
//logger comment
//json - connector independt