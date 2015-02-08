package com.util;
import com.violation.VoilationAssignmentDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used to create the database named Sonar in mysql database. The
 * useName and pwd are hardcoded.
 * 
 * 
 * @author 388524
 * 
 */
public class SetUpDataBase {
	// JDBC driver name and database URL
	static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static String DB_URL = "jdbc:mysql://localhost/";
	static String DB_URL_DML = "jdbc:mysql://localhost/sonar";
	// Database credentials
	static String USER = "root";
	static String PASS = "root";

	static {

		File directory = new File("");
		String currentDirectoryPath = directory.getAbsolutePath();

		File workSpaceFolder = new File(currentDirectoryPath);
		String propertiesFile = workSpaceFolder.getAbsolutePath()
		+ "/sonar-project.properties";
		
		if(null != VoilationAssignmentDriver.getPathToProperties() && !"".equalsIgnoreCase(VoilationAssignmentDriver.getPathToProperties())){
			propertiesFile = VoilationAssignmentDriver.getPathToProperties() + "/sonar-project.properties";
		}
		
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(propertiesFile);

			// Load properties file
			PropertiesUtil properties = new PropertiesUtil();

			properties.load(inputStream);

			// String runManual =
			// properties.get("configuration.runmode.manual");
			JDBC_DRIVER = properties
			.get("configuration.db.driverName");
			DB_URL = properties
			.get("configuration.db.url");
			 
			DB_URL_DML = properties
				.get("configuration.db.url");
			
			USER = properties
			.get("configuration.db.userName");
			
			PASS = properties
			.get("configuration.db.password");
			 
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void setUpDataBase() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating database Sonar");
			stmt = conn.createStatement();

		//	String sql = "CREATE DATABASE IF NOT EXISTS SONAR";
			//stmt.executeUpdate(sql);
			System.out.println("Database created successfully");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
	}
	static Connection conn = null;
	static Statement stmt = null;

	/**
	 * This method is used to run the passed query
	 * 
	 * @param query
	 * @param isDMLStatementRequired
	 * @return
	 */
	public static ResultSet runQuery(String query, boolean isDMLStatementRequired) {

		boolean insertRequired = true;
		ResultSet resultSet = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL_DML, USER, PASS);
			stmt = conn.createStatement();

			if (isDMLStatementRequired) {
				//System.out.println("executing " + query);
				resultSet = stmt.executeQuery(query);
				//System.out.println("Done " + query);
			} else {
				System.out.println("executing " + query);
				stmt.execute(query);
				System.out.println("Done " + query);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return resultSet;
	}

	public static Connection getConn() {
		return conn;
	}

	public static Statement getStmt() {
		return stmt;
	}

	public static void setConn(Connection _connLocal) {
		conn = _connLocal;
	}

	public static void setStmt(Statement _stmtLocal) {
		stmt = _stmtLocal;
	}

	// end main
}// end JDBCExample
