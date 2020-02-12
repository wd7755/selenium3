package com.agileach.selenium3;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

public class TestcaseBase {
	protected static WebDriver driver;
	protected static Logger logger = LogManager.getLogger(TestcaseBase.class);
	public static WebDriver getDriver() {
		if (driver == null) {
			synchronized (WebDriver.class) {
				try {
					System.setProperty("webdriver.chrome.driver", "D:\\jdk\\chromedriver.exe");
					ChromeOptions option = new ChromeOptions();		
					//通过ChromeOptions的setExperimentalOption方法，传下面两个参数来禁止掉谷歌受自动化控制的信息栏
					option.setExperimentalOption("useAutomationExtension", false); 
					option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));	
					option.setCapability("acceptSslCerts", true);
					driver = new ChromeDriver(option);		
					logger.info("instance an new webdriver...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		
		return driver;
	}

	@BeforeTest
	public void setUp() {
		driver = getDriver();
		logger.info("get webdriver...");
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
		driver = null;
		logger.info("quit webdriver...");
	}
}
