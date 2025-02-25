package com.opshub.excersice1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opshub.excersice1.model.JiraIssue;
import com.opshub.excersice1.service.JiraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;

import java.util.Scanner;


@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.opshub.excersice1")
public class Excersice1Application implements CommandLineRunner {

	@Autowired
	private JiraService jiraService;
	Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =  SpringApplication.run(Excersice1Application.class, args);
		log.info("Stopping the server...");
		ctx.close();
	}

	@Override
	public void run(String... args) throws Exception {
		boolean loopControl = true;
		while (loopControl) {
			try {
				showMenu();

				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
					case 1: createIssue(); break;
					case 2: getAllIssues(); break;
					case 3: getIssueById(); break;
					case 4: updateIssue(); break;
					case 5:
						loopControl = false; System.out.println("Exiting the program..."); break;
					default: System.out.println("Invalid choice. Please try again.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Option choice. Try again.");
			}
		}
	}

	private void createIssue() {
		JiraIssue issue = jiraService.createJiraIssueFromInput();

		jiraService.createIssue(issue);
		System.out.println("Issue created successfully.");
	}

	private void getAllIssues() throws JsonProcessingException {
		System.out.print("Enter the Project Key: ");
		String projectKey = scanner.nextLine();
		System.out.println("Fetching all issues...");
		jiraService.getAllIssues(projectKey);
	}

	private void getIssueById() throws JsonProcessingException {
		System.out.print("Enter the Project Key: ");
		String projectKey = scanner.nextLine();
		System.out.print("Enter Issue ID: ");
		String issueId = scanner.nextLine();
		jiraService.getIssue(projectKey, issueId);
	}

	private void updateIssue() {
		System.out.print("Enter the Project Key: ");
		String projectKey = scanner.nextLine();
		System.out.print("Enter Issue ID: ");
		String issueId = scanner.nextLine();
		System.out.print("Enter Updated Summary: ");
		String summary = scanner.nextLine();
		System.out.print("Enter Updated Description: ");
		String description = scanner.nextLine();

		JiraIssue.Fields fields = new JiraIssue.Fields();
		fields.setSummary(summary);
		fields.setDescription(description);

		jiraService.updateIssue(projectKey, issueId, fields);
		System.out.println("Issue updated successfully.");
	}

	private void showMenu(){
		System.out.println("\nSelect an option:");
		System.out.println("1. Create Issue");
		System.out.println("2. Get All Issues");
		System.out.println("3. Get Issue by ID");
		System.out.println("4. Update Issue");
		System.out.println("5. Exit");
	}
}
//main file bhot choti
//lombok
//logger comment
//json - connector independt