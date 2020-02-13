package com.agileach.selenium3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelProcess {
	public static Iterator<String[]> proessExcel(String filePath, int sheetId) throws IOException {			
		File file = new File(System.getProperty("user.dir") + filePath);
		InputStream is = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		// 获得工作表
		XSSFSheet sheet = workbook.getSheetAt(sheetId);
		XSSFCell cell = null;
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		List<String[]> records = new ArrayList<String[]>();
		for (int i = 1; i <= rowNum; i++) {
			// 当前行
			XSSFRow row = sheet.getRow(i);
			//得到总列数
			int colNum = row.getLastCellNum();
			String[] data = new String[colNum];
			for (int j = 0; j < colNum; j++) {
				cell = row.getCell(j);
				data[j] = cell.getCellType() == CellType.STRING ? cell.getStringCellValue()
						: String.valueOf(cell.getNumericCellValue());				
			}
			records.add(data);
		}
		workbook.close();
		return records.iterator();	
	}	
}