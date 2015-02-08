package com.violation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.util.PropertiesUtil;
import com.util.ReviewRecord;
import org.sonar.wsclient.SonarClient;
import org.sonar.wsclient.SonarClient.Builder;
import org.sonar.wsclient.connectors.ConnectionException;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueClient;
import org.sonar.wsclient.issue.IssueQuery;
import org.sonar.wsclient.issue.Issues;
import org.sonar.wsclient.services.TimeMachine;

public class SonarWebServiceAPI {

	private static List<TimeMachine> voilation;
	static String url = "http://localhost:9000";
	static String login = "admin";
	static String password = "Release10";

	static {

		File directory = new File("");
		String currentDirectoryPath = directory.getAbsolutePath();

		File workSpaceFolder = new File(currentDirectoryPath);
		String propertiesFile = workSpaceFolder.getAbsolutePath()
		+ "//sonar-project.properties";

		if (null != VoilationAssignmentDriver.getPathToProperties()
				&& !".".equalsIgnoreCase(VoilationAssignmentDriver
						.getPathToProperties())) {
			propertiesFile = VoilationAssignmentDriver.getPathToProperties()
			+ "/sonar-project.properties";
		}

		InputStream inputStream;
		try {
			inputStream = new FileInputStream(propertiesFile);

			// Load properties file
			PropertiesUtil properties = new PropertiesUtil();

			properties.load(inputStream);

			// String runManual =
			// properties.get("configuration.runmode.manual");
			url = properties.get("configuration.url");
			login = properties.get("configuration.userName");

			password = properties.get("configuration.password");

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<Issue> fetchAllVoilations(
			List<ReviewRecord> projectKeyList, Date _enteredDateLocal,boolean unassignMode) {

		List<Issue> issueListFinal = new ArrayList<Issue>();

		for (ReviewRecord projectKey : projectKeyList) {

			System.out.println("Fetching Voilations for Project Key -->> "
					+ projectKey.getProject_key());

			Builder builder = SonarClient.builder();

			builder.url(url);
			builder.login(login);
			builder.password(password);
			SonarClient sonarClient = builder.build();

			IssueClient issueClient = sonarClient.issueClient();

			IssueQuery issueQuery = IssueQuery.create();
			if (unassignMode) {
				issueQuery.assigned(false);

			}else{
				
				issueQuery.assigned(true);
			}
			issueQuery.severities("BLOCKER", "CRITICAL", "MAJOR");
			issueQuery.resolved(false);
			issueQuery.createdAfter(VoilationAssignmentDriver.enteredDate);
			issueQuery.components(projectKey.getProject_key());

			Issues issues = issueClient.find(issueQuery);

			List<Issue> issueList = issues.list();
			issueListFinal.addAll(issueList);

			System.out.println("Total Voilation found are  -->> "
					+ (null != issueList ? issueList.size() : 0));

		}

		return issueListFinal;
	}

	/**
	 * @param _createReviewListLocal
	 * @param _userNameLocal
	 */
	public static void createNewReviews(Issue _createReviewListLocal,
			String _userNameLocal) {

		System.out.println("Creating new Reviews in Open Status..... ");
		Builder builder = SonarClient.builder();

		builder.url(url);
		builder.login(login);
		builder.password(password);
		SonarClient sonarClient = builder.build();

		IssueClient issueClient = sonarClient.issueClient();

		try {
			issueClient.assign(_createReviewListLocal.key(), _userNameLocal);

			issueClient.addComment(_createReviewListLocal.key(),
			"Please Fix This");

			System.out.println("Review Created with Follwing Details...");

			System.out.println(_createReviewListLocal.message() + "--"
					+ _createReviewListLocal.assignee() + "--"
					+ _createReviewListLocal.status() + "--"
					+ _createReviewListLocal.severity());
		} catch (ConnectionException connection) {

			System.out
			.println("Exception Occured while assining voilation Id --> "
					+ _createReviewListLocal.message());

		}

	}
}
