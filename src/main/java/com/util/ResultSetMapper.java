package com.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.beanutils.BeanUtils;

public class ResultSetMapper<T> {
	
	@SuppressWarnings("unchecked")
	public List<T> mapRersultSetToObject(ResultSet rs, Class outputClass) throws IOException {
		List<T> outputList = null;
		try {
			// make sure resultset is not null
			if (rs != null) {
				// check if outputClass has 'Entity' annotation
				if (outputClass.isAnnotationPresent(Entity.class)) {
					// get the resultset metadata
					ResultSetMetaData rsmd = rs.getMetaData();
					// get all the attributes of outputClass
					Field[] fields = outputClass.getDeclaredFields();
					while (rs.next()) {
						T bean = (T) outputClass.newInstance();
						for (int _iterator = 0; _iterator < rsmd
								.getColumnCount(); _iterator++) {
							// getting the SQL column name
							String columnName = rsmd
									.getColumnName(_iterator + 1);
							// reading the value of the SQL column
							Object columnValue = rs.getObject(_iterator + 1);
							// iterating over outputClass attributes to check if any attribute has 'Column' annotation with matching 'name' value
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field
											.getAnnotation(Column.class);
									if (!"DATA_FINAL".equalsIgnoreCase(columnName) && column.name().equalsIgnoreCase(
											columnName)
											&& columnValue != null) {
										BeanUtils.setProperty(bean, field
												.getName(), columnValue);
										//System.out.println(field.getName());
										break;
										
									} else if ("DATA_FINAL"
											.equalsIgnoreCase(columnName)
											&& column.name().equalsIgnoreCase(
													columnName)
													&& columnValue != null) {

										String str = "";
										Blob blob = rs.getBlob(_iterator + 1);
										byte[] b = blob.getBytes(1,
												(int) blob.length());
										ByteArrayInputStream inputStream = new ByteArrayInputStream(
												b);
										Reader r = new InputStreamReader(
												inputStream);
										StringWriter sw = new StringWriter();
										char[] buffer = new char[20000];
										for (int n; (n = r.read(buffer)) != -1;)
											sw.write(buffer, 0, n);
										str = sw.toString();

										BeanUtils.setProperty(bean,
												field.getName(), str);
									}
								}
							}
						}
						if (outputList == null) {
							outputList = new ArrayList<T>();
						}
						outputList.add(bean);
					}

				} else {
					// throw some error
				}
			} else {
				return null;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}finally{

			// finally block used to close resources
			try {
				if (SetUpDataBase.getStmt() != null)
					SetUpDataBase.getStmt().close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (SetUpDataBase.getConn() != null)
					SetUpDataBase.getConn().close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		
		}
		return outputList;
	}
	
	
}

