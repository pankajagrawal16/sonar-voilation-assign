package com.violation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.util.ExcelGenerator;
import com.util.PropertiesUtil;
import com.util.ResultSetMapper;
import com.util.ReviewRecord;
import com.util.SetUpDataBase;
import org.sonar.wsclient.issue.Issue;

public class VoilationAssignmentDriver {

	private static String readString = null;

	static List<ReviewRecord> pojoList = null;

	static List<Issue> voilationDetails = null;

	// static List<ReviewCreateQuery> createReviewList = null;

	static List<Issue> voilationDetailsConsolidated = new ArrayList<Issue>();

	static List<ReviewRecord> notFoundReviewList = new ArrayList<ReviewRecord>();

	static Map<String, Issue> notFoundUserIssue = new HashMap<String, Issue>();

	static Map<String, List> useridMap = new HashMap<String, List>();

	private static String pathToProperties = ".";

	static String projectAbsoluteName = null;
	public static Date enteredDate = null;

	private static HashMap<String, Integer> optionMap = new HashMap<String, Integer>();
	static {

		optionMap.put("1", 1);
		optionMap.put("2", 2);
		optionMap.put("3", 3);
		optionMap.put("4", 4);

	}

	public static void main(String[] args) throws Exception {

		if (null != args && args.length == 1) {

			setPathToProperties(args[0]);
		}
		if (validateRunMode()) {

			fetchVoilations();
			assignVoilations();
			exportAsDataAsExcel();

		} else {

			System.out.println("WELCOME TO SONAR VOILATION ASSIGNMENT UTILTIY");

			do {

				readString = null;

				pojoList = null;

				voilationDetails = null;

				// createReviewList = null;

				voilationDetailsConsolidated = new ArrayList<Issue>();

				notFoundReviewList = new ArrayList<ReviewRecord>();

				useridMap = new HashMap<String, List>();

				projectAbsoluteName = null;

				enteredDate = null;

				System.out.println("1. Export Voilation as Excel");
				System.out.println("2. Assign Voilations");
				System.out.println("3. Assign Voilations and Export as Excel");
				System.out.println("4. Exit");

				String input = readInput();
				int value = 99;
				if (null != optionMap.get(input)) {
					value = optionMap.get(input);
				}

				switch (value) {
				case 1: {

					if (promptForListOfProjects()) {
						fetchVoilations();
						exportAsDataAsExcel();
					}

					break;

				}
				case 2: {
					if (promptForListOfProjects()) {
						getDateWhenReportIsToBeGenerated();
						fetchVoilations();
						assignVoilations();
					}
					break;
				}
				case 3: {

					if (promptForListOfProjects()) {
						getDateWhenReportIsToBeGenerated();
						fetchVoilations();
						assignVoilations();
						exportAsDataAsExcel();
					}
					break;

				}
				case 4: {
					System.out
					.println("THANKS FOR USING THE APP. PLEASE CONTACT 388524 IN CASE OF ANY CONCERN!!!!!!");
					System.exit(0);
				}
				default: {
					System.out
					.println("Invalid Entry. Please Enter a Valid Option ..");
				}
				}
			} while (true);

		}
	}

	/**
	 * @throws java.io.IOException
	 * @throws java.text.ParseException
	 * 
	 */
	private static boolean validateRunMode() throws IOException, ParseException {

		File directory = new File("");
		String currentDirectoryPath = directory.getAbsolutePath();

		File workSpaceFolder = new File(currentDirectoryPath);
		String propertiesFile = workSpaceFolder.getAbsolutePath()
		+ "/sonar-project.properties";

		if (null != getPathToProperties()
				&& !".".equalsIgnoreCase(getPathToProperties())) {
			propertiesFile = getPathToProperties()
			+ "/sonar-project.properties";
		}

		InputStream inputStream = new FileInputStream(propertiesFile);

		// Load properties file
		PropertiesUtil properties = new PropertiesUtil();

		properties.load(inputStream);

		boolean runModeAutomated = false;
		String runAutomated = properties.get("configuration.runmode.automated");
		// String runManual = properties.get("configuration.runmode.manual");
		String assignDate = properties
		.get("configuration.violation.assignDate");
		String projects = properties
		.get("configuration.violation.projectDetails");

		System.out.println("Checking Configuration......");

		if ("true".equals(runAutomated)) {

			String date = null != assignDate
			&& !"NA".equalsIgnoreCase(assignDate) ? assignDate : null;

			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);

			enteredDate = null == date ? cal.getTime() : formatter
					.parse(assignDate);

			projectAbsoluteName = projects;

			System.out
			.println("Assignments will be done in automated Mode with following settings: ");

			System.out.println("Assign voilation created after date --> "
					+ enteredDate);

			System.out.println("Assign Violation for Project -->" + projects);

			runModeAutomated = true;

			OutputStream output = new FileOutputStream(new File(propertiesFile));
			if (null != enteredDate) {
				properties.setProperty(
						"configuration.violation.previousAssignDate",
						enteredDate.toString());
				properties.store(output, null);
			}
			inputStream.close();
			output.flush();
			output.close();

		}

		return runModeAutomated;

	}

	/**
	 * 
	 */
	private static void getDateWhenReportIsToBeGenerated() {

		System.out
		.println("Please enter the date after which violation needs to be reported(dd/mm/yyyy). To ByPass press Continue(C)");
		System.out
		.println("------------------------------------------------------------------------------------------------------------");

		String option = readInput();
		long optionValue = 99;
		boolean validInput = false;

		do {
			try {

				if (!"C".equalsIgnoreCase(option)) {

					SimpleDateFormat formatter = new SimpleDateFormat(
					"ddMMyyyy");

					Date date = formatter.parse(option);
					System.out.println(date);
					enteredDate = date;
					validInput = true;

				} else {

					validInput = true;

				}

			} catch (ParseException exception) {

				System.out.println("Invalid Input. Please Re-Enter");
				option = readInput();
			}
		} while (!validInput);

	}

	/**
	 * 
	 */
	private static void assignVoilations() {

		// SonarWebServiceAPI.createNewReviews(createReviewList);

	}

	/**
	 * @throws java.io.IOException
	 * 
	 */
	private static void exportAsDataAsExcel() throws IOException {

		List<ReviewRecord> finalExcelData = populateExcelData();

	}

	/**
	 * @return
	 * @throws java.io.IOException
	 */
	private static List<ReviewRecord> populateExcelData() throws IOException {

		List<ReviewRecord> finalExcelData = new ArrayList<ReviewRecord>();

		List<Issue> issueList = SonarWebServiceAPI.fetchAllVoilations(pojoList,
				enteredDate, false);

		List<ReviewRecord> projectsList = null;

		for (Issue issue : issueList) {

			if (null != notFoundUserIssue && !notFoundUserIssue.isEmpty()) {

				Issue issueLocal = notFoundUserIssue.get(issue.key());

				if (null == issueLocal) {

					ReviewRecord review = populateFoundNotFoundUserDetails(
							issue, issue.assignee(), issue.status());

					if (null == projectsList) {
						projectsList = new ArrayList<ReviewRecord>();
					}

					projectsList.add(review);
				}
			}

		}

		if (null == projectsList) {
			projectsList = new ArrayList<ReviewRecord>();
		}
		projectsList.addAll(notFoundReviewList);

		if (null != projectsList && !projectsList.isEmpty()) {
			ExcelGenerator.createVoilationAssignmentSheet(projectsList,
                    "SONAR_VIOLATIONS", pathToProperties);
		} else {

			System.out.println("NO DATA TO UPDATE IN EXCEL");
		}

		return finalExcelData;
	}

	/**
	 * @throws java.sql.SQLException
	 * @throws java.io.IOException
	 * 
	 */
	private static void fetchVoilations() throws SQLException, IOException {

		ResultSetMapper<ReviewRecord> resultSetMapper = new ResultSetMapper<ReviewRecord>();
		ResultSet resultSet = null;
		String querytoUse = null;

		if (!"allProjects".equalsIgnoreCase(projectAbsoluteName)) {
			querytoUse = "select rf.kee from projects rf where rf.kee like '%"
				+ projectAbsoluteName + "%'";

		} else {

			querytoUse = "select rf.kee from projects rf ";
		}

		resultSet = SetUpDataBase.runQuery(querytoUse, true);

		pojoList = resultSetMapper.mapRersultSetToObject(resultSet,
				ReviewRecord.class);

		voilationDetails = SonarWebServiceAPI.fetchAllVoilations(pojoList,
				enteredDate, true);

		voilationDetailsConsolidated.addAll(voilationDetails);

		for (int counter = 0; counter < voilationDetailsConsolidated.size(); counter++) {

			Issue violation = voilationDetailsConsolidated.get(counter);
			String assignee = violation.assignee();

			if (null != assignee) {

				voilationDetailsConsolidated.remove(counter);
				counter--;
				String title = violation.message();
				System.out.println("Review -->> " + title + " Already Present");

			} else {

			}
		}

		getSvnInformation();

	}

	/**
	 * @throws java.sql.SQLException
	 * @throws java.io.IOException
	 * 
	 */
	private static void getSvnInformation() throws SQLException, IOException {

		for (Issue violations : voilationDetails) {

			ResultSetMapper<ReviewRecord> resultSetMapper = new ResultSetMapper<ReviewRecord>();
			ResultSet resultSet = null;

			resultSet = SetUpDataBase
			.runQuery(
					"SELECT md.data data_final FROM project_measures pm	JOIN snapshots s ON pm.snapshot_id = s.id	JOIN metrics m ON m.id = pm.metric_id	JOIN projects p ON s.project_id = p.id	JOIN measure_data md ON md.snapshot_id = s.id WHERE  m.name = 'scm.hash'	and md.snapshot_id = s.id and p.kee = '"
					+ violations.componentKey() + "'", true);

			List<ReviewRecord> svnInfoList = resultSetMapper
			.mapRersultSetToObject(resultSet, ReviewRecord.class);

			if (null != svnInfoList) {

				for (ReviewRecord svnInfo : svnInfoList) {

					String svnData = svnInfo.getData_final();
					if (svnData.startsWith("1=")
							&& svnData.substring(2, 3).matches("^([A-Za-z])")) {

						int index = svnData.indexOf(";" + violations.line()
								+ "=");
						if (index > -1) {
							int equalsIndex = svnData.indexOf("=", index);
							int colanIndex = svnData.indexOf(";", equalsIndex);
							String author = svnData.substring(equalsIndex + 1,
									colanIndex);
							String resourceKey = getLocalUserIdFromAuthorName(
									author, violations);

						}

						break;

					}

				}
			} else {
				System.out.println("Unable to fetch SVN Details for project "
						+ violations.key());
			}

		}

	}

	/**
	 * @param _authorLocal
	 * @param _violationsLocal
	 * @return
	 * @throws java.io.IOException
	 */
	private static String getLocalUserIdFromAuthorName(String _authorLocal,
			Issue _violationsLocal) throws IOException {

		if (null != useridMap && null != useridMap.get(_authorLocal)) {

			List userData = useridMap.get(_authorLocal);

			if (userData.size() > 0) {

				// int userId = (Integer) userData.get(0);
				String userName = (String) userData.get(1);

				SonarWebServiceAPI.createNewReviews(_violationsLocal, userName);

			} else {

				ResultSetMapper<ReviewRecord> resultSetMapper = new ResultSetMapper<ReviewRecord>();
				ResultSet resultSet = null;

				resultSet = SetUpDataBase
				.runQuery(
						"select id as local_user_id,login local_user_name from users u where u.login like '%"
						+ _authorLocal + "%'", true);

				List<ReviewRecord> userInfo = resultSetMapper
				.mapRersultSetToObject(resultSet, ReviewRecord.class);

				List userDataList = new ArrayList();

				for (ReviewRecord user : userInfo) {

					userDataList.add(user.getLocal_User_Id());
					userDataList.add(user.getLocal_user_names());
					useridMap.put(_authorLocal, userDataList);

					SonarWebServiceAPI.createNewReviews(_violationsLocal,
							user.getLocal_user_names());
				}

			}

		} else {

			ResultSetMapper<ReviewRecord> resultSetMapper = new ResultSetMapper<ReviewRecord>();
			ResultSet resultSet = null;

			resultSet = SetUpDataBase
			.runQuery(
					"select id as local_user_id,login local_user_name from users u where u.login like '%"
					+ _authorLocal + "%'", true);

			List<ReviewRecord> userInfo = resultSetMapper
			.mapRersultSetToObject(resultSet, ReviewRecord.class);

			List userDataList = new ArrayList();

			if (null != userInfo) {
				for (ReviewRecord user : userInfo) {

					userDataList.add(user.getLocal_User_Id());
					userDataList.add(user.getLocal_user_names());
					useridMap.put(_authorLocal, userDataList);

					SonarWebServiceAPI.createNewReviews(_violationsLocal,
							user.getLocal_user_names());
				}
			}

		}

		if (null != useridMap && null == useridMap.get(_authorLocal)) {

			System.out
			.println("Voilation -->> "
					+ _violationsLocal.message()
					+ " for resource "
					+ _violationsLocal.componentKey()
					+ " is not found for any user existing in sonar DB. User from whom violation is created is -->>  "
					+ _authorLocal);

			ReviewRecord review = populateFoundNotFoundUserDetails(
					_violationsLocal, _authorLocal, "NOT FOUND");
			notFoundReviewList.add(review);
		}

		return _violationsLocal.key();
	}

	/**
	 * @param _violationsLocal
	 * @param _authorLocal
	 */
	private static ReviewRecord populateFoundNotFoundUserDetails(
			Issue _violationsLocal, String _authorLocal, String status) {

		ReviewRecord review = new ReviewRecord();
		review.setIssueKey(_violationsLocal.key());
		review.setProject_key(_violationsLocal.projectKey());
		review.setLogin(_authorLocal);
		review.setLocal_user_names(_authorLocal);
		review.setSeverity(_violationsLocal.severity());
		review.setStatus(status);
		String key = _violationsLocal.componentKey();
		int index = key.indexOf(":");
		key = key.substring(index + 1);
		review.setName(key);
		review.setResource_line(_violationsLocal.line());
		review.setPlugin_rule_key(_violationsLocal.message());

		if ("NOT FOUND".equalsIgnoreCase(status)) {

			notFoundUserIssue.put(_violationsLocal.key(), _violationsLocal);
		}

		return review;

	}

	/**
	 * @return
	 */
	private static String readInput() {
		Scanner inputReader = new Scanner(System.in);
		readString = inputReader.nextLine();
		readString = readString.trim();

		if (readString.length() == 0) {
			System.out
			.println("Please enter the text and press THEN press Enter");
			readInput();
		}

		return readString;
	}

	/**
	 * This method is used to prompt for list project checked out and begin its
	 * compilation when user selects to build a single project
	 */
	private static boolean promptForListOfProjects() throws Exception {

		File directory = new File("");

		ResultSetMapper<ReviewRecord> resultSetMapper = new ResultSetMapper<ReviewRecord>();
		ResultSet resultSet = null;

		resultSet = SetUpDataBase.runQuery(
				"select * from projects rv where rv.scope = 'PRJ'", true);

		List<ReviewRecord> projectsList = resultSetMapper
		.mapRersultSetToObject(resultSet, ReviewRecord.class);

		System.out.println("Choose From the Following Projects.");
		System.out.println("-----------------------------------");

		int count = 1;
		for (ReviewRecord projectName : projectsList) {
			String projectAbsoluteName = projectName.getName();
			System.out.println(count++ + "  " + projectAbsoluteName);
		}

		System.out.println(count++ + " " + " All Projects");

		System.out.println(count + "  To Go Back To Main Menu");

		System.out.println("Enter Option");

		String option = readInput();

		System.out.println("\n\n");

		int optionValue = 99;
		boolean validInput = false;

		do {
			try {
				optionValue = Integer.parseInt(option);
				if (optionValue > projectsList.size() + 2) {
					System.out.println("Invalid Input. Please Re-Enter");
					option = readInput();
				} else {

					validInput = true;

					if (optionValue == projectsList.size() + 1) {
						projectAbsoluteName = "allProjects";
					} else if (optionValue == projectsList.size() + 2) {
						return false;

					} else {

						projectAbsoluteName = projectsList.get(optionValue - 1)
						.getProject_key();
					}
				}

			} catch (NumberFormatException exception) {

				System.out.println("Invalid Input. Please Re-Enter");
				option = readInput();
			}
		} while (!validInput);

		return true;
	}

	public static void setPathToProperties(String pathToProperties) {
		VoilationAssignmentDriver.pathToProperties = pathToProperties;
	}

	public static String getPathToProperties() {
		return pathToProperties;
	}
}
