package com.agileach.selenium3.testcases;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import com.agileach.selenium3.BmiCalculatorPage;
import com.agileach.selenium3.ExcelProcess;
import com.agileach.selenium3.OperateDB;
import com.agileach.selenium3.TestcaseBase;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class BmiCalculatorTest extends TestcaseBase{
	private String filePath = System.getProperty("user.dir");
	private String url = "file:///" + filePath + "/src/main/resources/BmiCalculator.html";
	//String fileName = filePath + "/src/test/resources/" + this.getClass().getSimpleName() + ".xlsx";	
	
	@DataProvider
	public Iterator<String[]> testDataByDB() throws SQLException {
		List<String[]> records = new ArrayList<String[]>();
		ResultSet rs = OperateDB.getResultSet("SELECT * FROM BMI");		
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int cols = rsMetaData.getColumnCount();
		while (rs.next()) {
			String fields[] = new String[cols];
			int col = 0;
			for (int colIdx = 1; colIdx <= cols; colIdx++) {
				fields[col] = rs.getString(colIdx);
				col++;
			}
			records.add(fields);
		}
		OperateDB.closeAll();
		return records.iterator();
	}		

	@DataProvider
	protected Iterator<String[]> testDataByExcel() throws IOException  {	
		String fileName = excelPath + this.getClass().getSimpleName() + ".xlsx";	
		return ExcelProcess.proessExcel(fileName, 0);
	}	
	
	@Test(dataProvider = "testDataByExcel")
	public void testBmiCalculatorTest(String height, String weight, String bmi, String bmiCategory) {
		driver.get(url);			
		BmiCalculatorPage cp = new BmiCalculatorPage(driver);		
		cp.calculateBmi(height, weight);	
		AssertJUnit.assertEquals(bmi, cp.getBmi());
		AssertJUnit.assertEquals(bmiCategory, cp.getBmiCategory());		
	}
}