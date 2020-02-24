package com.agileach.selenium3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import com.agileach.selenium3.Util;

public class MyWebDriverListener implements WebDriverEventListener{
	protected static Logger logger = LogManager.getLogger(TestcaseBase.class);
	
    public void onException(Throwable throwable, WebDriver driver) {       
        String dateTime = Util.getFormatedTime("yyyy_MM_dd_HH_mm_ss");     
		StringBuffer sb = new StringBuffer();
		String captureName = sb.append(dateTime).append(".png").toString();        
        Util.takeScreenshot(driver, captureName);      
        logger.error("发生异常,原因是: " + throwable.getMessage() + ". 截图保存在: "+captureName);
    }  

    public void beforeClickOn(WebElement element, WebDriver driver) {      
        logger.info("点击页面元素: " + element.toString());
    }

    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
    	logger.info("查找元素的条件是: "+ by.toString());      
    }
    public void beforeNavigateTo(String url, WebDriver driver) {
    	logger.info("beforeNavigateTo: " + url);
    	logger.info("beforeNavigateTo by driver : " + driver.getCurrentUrl());
    }
    
    public void afterNavigateTo(String url, WebDriver driver) {
    	logger.info("afterNavigateTo: " + url);
    	logger.info("afterNavigateTo by driver: " + driver.getCurrentUrl());
    }
    public void afterChangeValueOf(WebElement element, WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterClickOn(WebElement element, WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterNavigateBack(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterNavigateForward(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterNavigateRefresh(WebDriver driver) {
        // TODO Auto-generated method stub

    }


    public void afterScript(String url, WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
        // TODO Auto-generated method stub

    }


    public void beforeNavigateBack(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void beforeNavigateForward(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void beforeNavigateRefresh(WebDriver driver) {
        // TODO Auto-generated method stub

    }
   
    public void beforeScript(String url, WebDriver driver) {
        // TODO Auto-generated method stub

    }

	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}	

	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		
	}

	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	public void afterGetText(WebElement element, WebDriver driver, String text) {
		// TODO Auto-generated method stub
		
	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}

	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}

	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}
}
