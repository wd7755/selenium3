package com.agileach.selenium3;

import org.openqa.selenium.WebDriver;

public class MyException extends Exception{		
	private static final long serialVersionUID = 1L;	
	String dateTime = Util.getFormatedTime("yyyy-MM-dd HH_mm_ss_SSS");
	StringBuffer sb = new StringBuffer();
	String captureName = sb.append(dateTime).append(".png").toString();		
	
	public MyException(Exception e, WebDriver driver) {				
		Util.takeScreenshot(driver, captureName);
	}	
}
