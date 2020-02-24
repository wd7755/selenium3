package com.agileach.selenium3;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestcaseBase {
	protected static WebDriver driver;
	protected static Logger logger = LoggerFactory.getLogger(TestcaseBase.class);
	private static EventFiringWebDriver eventDriver;	
	
	public static WebDriver getDriver(String browser) throws MyException{
		if (driver == null) {
			synchronized (WebDriver.class) {
				try {				
					DesiredCapabilities dc = new DesiredCapabilities();
					if(browser.equalsIgnoreCase("chrome")) {	
						//Selenium Grid方式1:
						//dc.setBrowserName(browser);						
						//driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),dc);
						
						//Selenium Grid方式2:
//						dc = DesiredCapabilities.chrome();						
//						driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),dc);
						
						//local方式
						System.setProperty("webdriver.chrome.driver", "D:\\jdk\\chromedriver.exe");
						ChromeOptions option = new ChromeOptions();								
						//通过ChromeOptions的setExperimentalOption方法，传下面两个参数来禁止掉谷歌受自动化控制的信息栏
						option.setExperimentalOption("useAutomationExtension", false); 
						option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));	
						option.setCapability("acceptSslCerts", true);						
						driver = new ChromeDriver(option);							
					}else if(browser.equalsIgnoreCase("firefox")) {		
						//Selenium Grid方式1
						//dc.setBrowserName(browser);							
						//driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),dc);					
						
						//Selenium Grid方式2
						//dc.setBrowserName(browser);		
//						dc = DesiredCapabilities.firefox();					
//						driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),dc);
						
						//local方式
						System.setProperty("webdriver.gecko.driver", "D:\\jdk\\geckodriver.exe");    
						FirefoxOptions option = new FirefoxOptions();		
						option.setCapability("acceptSslCerts", true);
						driver = new FirefoxDriver(option);		
					}else if(browser.equalsIgnoreCase("internet explorer")) {							
						//Selenium Grid方式
						dc.setBrowserName("internet explorer");	
						driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),dc);	
						
						//local方式
//						System.setProperty("webdriver.ie.driver", "D:\\jdk\\IEDriverServer.exe");
//						InternetExplorerOptions option = new InternetExplorerOptions();		
//						option.setCapability("acceptSslCerts", true);
//						driver = new InternetExplorerDriver(option);		
					}
					logger.info("instance an new webdriver...");					
					eventDriver = new EventFiringWebDriver(driver);
			        //注册事件
			        eventDriver.register(new MyWebDriverListener());	
				} catch (Exception e) {
					e.printStackTrace();
					throw new MyException(e, eventDriver);
				}
			}			
			eventDriver.manage().window().maximize();
			eventDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}		
		return eventDriver;
	}

	@BeforeTest
	public void setUp() throws MyException{		
		// 数据流的形式读取配置文件
		Properties prop = new Properties();
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("config.properties");	
			prop.load(fis);
			String browser = prop.getProperty("Browser");			
			driver = getDriver(browser);				
			logger.info("Start {0}，get webdriver...",browser);
			Util.setWebDriver(driver);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e, eventDriver);			
		}		
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
		driver = null;
		logger.info("quit webdriver...");
	}
}
