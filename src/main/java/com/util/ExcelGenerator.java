package com.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelGenerator {
	private static HSSFWorkbook workBook = null;

	private static FileOutputStream outPut = null;
	

	/**
	 * This method is used to create a Excel File.
	 * 
	 * @param fileName
	 * @param _fileNameLocal 
	 */
	public static void createExcelFile(String fileName, String _fileNameLocal) {

		workBook = new HSSFWorkbook();
		File excelFile = new File(
				fileName + "/" + _fileNameLocal + ".xls");
		try {
			excelFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outPut = new FileOutputStream(excelFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createVoilationAssignmentSheet(
			List<ReviewRecord> masterList,
			 String _fileNameFileLocal,
			String _currentDirectoryPathLocal) throws IOException {
		
		System.out.println("\n\nExporting Voilation data for " + _fileNameFileLocal);
		
		createExcelFile(_currentDirectoryPathLocal,_fileNameFileLocal);
		
		createMasterInputSheet(masterList);
		
		workBook.write(outPut);
		outPut.close();
		System.out.println("\n\nData Exported");
		
		
	}
	

	private static void createMasterInputSheet(
			List<ReviewRecord> masterList) {
		


		int rowNum = 0;

		HSSFSheet sheet = workBook.createSheet("Sonar Violations");

		CellStyle style = workBook.createCellStyle();
		HSSFFont font = workBook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLACK.index);
		style.setFont(font);
		style.setWrapText(true);

		HSSFRow row = sheet.createRow(rowNum);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue("S.No.");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("Project");
		
		cell = row.createCell(2);
		cell.setCellStyle(style);
		cell.setCellValue("Author");
		
		cell = row.createCell(3);
		cell.setCellStyle(style);
		cell.setCellValue("Author Name");
		
		cell = row.createCell(4);
		cell.setCellStyle(style);
		cell.setCellValue("Severity");
		
		cell = row.createCell(5);
		cell.setCellStyle(style);
		cell.setCellValue("Status");
		
		cell = row.createCell(6);
		cell.setCellStyle(style);
		cell.setCellValue("File Name");

		cell = row.createCell(7);
		cell.setCellStyle(style);
		cell.setCellValue("Line");
		
		cell = row.createCell(8);
		cell.setCellStyle(style);
		cell.setCellValue("Rule Description");
		
		

		style = workBook.createCellStyle();
		style.setWrapText(true);
		int serialNum = 1;

		for (ReviewRecord pojo : masterList) {
			
			row = sheet.createRow(++rowNum);
			int i = 0;
			
			cell = row.createCell(i);
			cell.setCellStyle(style);
			//System.out.println(pojo.QueryId);
			cell.setCellValue(serialNum++);
			
			i++;
			cell = row.createCell(i);
			cell.setCellStyle(style);
			//System.out.println(pojo.QueryId);
			String project = pojo.getProject_key();
			int index  = project.indexOf("_");
			project = project.substring(0,index);
			cell.setCellValue(project);
			
			i++;
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(pojo.getLogin());
			
			i++;
			
			cell = row.createCell(i);
			cell.setCellStyle(style);
			
			cell.setCellValue(pojo.getLocal_user_names());
		
			
			
			i++;
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(pojo.getSeverity());
			
			
			i++;
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(pojo.getStatus());
			
			i++;
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(pojo.getName());
			
			i++;
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(null != pojo.getResource_line() ? pojo.getResource_line().intValue():0);
			
			
			i++;
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(pojo.getPlugin_rule_key());
			
		}
		/*sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		sheet.autoSizeColumn(12);
		sheet.autoSizeColumn(13);
		sheet.autoSizeColumn(14);
		sheet.autoSizeColumn(15);
		sheet.autoSizeColumn(16);
		sheet.autoSizeColumn(17);*/
		
		

	
		
	}

}
