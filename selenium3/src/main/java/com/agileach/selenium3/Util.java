package com.agileach.selenium3;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
	private static long pauseTime = 1000;
	private static String capturePath = new File(System.getProperty("user.dir")) + "/screenshots/";
	private static Logger logger = LoggerFactory.getLogger(TestcaseBase.class);
	private static WebDriver driver = null;

	public static void setWebDriver(WebDriver webDriver) {
		driver = webDriver;
	}

	public static String getFormatedTime(String timeStyle) {
		SimpleDateFormat df = new SimpleDateFormat(timeStyle);// 设置日期格式
		return df.format(System.currentTimeMillis());
	}

	public static void operationCheck(String methodName, boolean isSucceed) {
		if (isSucceed) {
			logger.info("method [" + methodName + "]运行通过!");
		} else {
			String dateTime = getFormatedTime("yyyy-MM-dd HH:mm:ss:SSS");
			StringBuffer sb = new StringBuffer();
			String captureName = sb.append(capturePath).append(methodName).append(dateTime).append(".png").toString();
			takeScreenshot(captureName);
			logger.error("method [" + methodName + "]运行失败，请查看截图快照!" + captureName);
		}
	}

	// 显式等待方式获取元素
	public static WebElement getElement(final By by) {
		boolean isSucceed = false;		
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			    element = wait.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver d) {
					WebElement e = d.findElement(by);
					return e.isDisplayed() ? e : null; 
				}
			});
			logger.info("get element by [" + by.toString() + " ]  success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("getElement", isSucceed);
		return element;
	}

	public static void pause(long time) {	
		try {
			Thread.sleep(time);
			logger.info("Thread sleep [" + time / 1000 + "] seconds");
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}	
	
	public static void selectDefaultFrame() {
		driver.switchTo().defaultContent();
	}

	public static boolean selectFrame(String nameOrId, long timeOut) {
		boolean isSucceed = false;
		long timeBegins = System.currentTimeMillis();
		do {
			try {
				selectDefaultFrame();
				driver.switchTo().frame(nameOrId);
				logger.info("select frame by name or Id [" + nameOrId + "] success!");
				isSucceed = true;
				break;
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
			pause(pauseTime);
		} while (System.currentTimeMillis() - timeBegins <= timeOut * 1000);
		operationCheck("selectFrame", isSucceed);
		return isSucceed;
	}

	public static boolean selectFrame(int index, long timeOut) {
		boolean isSucceed = false;
		long timeBegins = System.currentTimeMillis();
		do {
			try {
				selectDefaultFrame();
				driver.switchTo().frame(index);
				logger.info("select frame by index [" + index + "] success!");
				isSucceed = true;
				break;
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
			pause(pauseTime);
		} while (System.currentTimeMillis() - timeBegins <= timeOut * 1000);
		operationCheck("selectFrame", isSucceed);
		return isSucceed;
	}

	public static boolean selectFrame(WebElement webElement, long timeOut) {
		boolean isSucceed = false;
		long timeBegins = System.currentTimeMillis();
		do {
			try {
				selectDefaultFrame();			
				driver.switchTo().frame(webElement);
				logger.info("select frame by frameitself [" + webElement.toString() + "] success!");
				isSucceed = true;
				break;
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
			pause(pauseTime);
		} while (System.currentTimeMillis() - timeBegins <= timeOut * 1000);
		operationCheck("selectFrame", isSucceed);
		return isSucceed;
	}

	public static boolean selectFrame(String nameOrId) {
		return selectFrame(nameOrId, 0);
	}

	public static boolean selectFrame(int index) {
		return selectFrame(index, 0);
	}

	public static boolean selectFrame(WebElement webElement) {
		return selectFrame(webElement, 0);
	}

	public static void setTextOnFCKEditor(String editorId, String text) {
		boolean isSucceed = false;
		try {
			String javascript = "FCKeditorAPLGetInstance('" + editorId + "').SetHTML('<p>" + text + "</p>');";
			jsExecutor(javascript);
			logger.info("input text [" + text + " ] to FCKEditor [" + editorId + " ] success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("setTextOnFCKEditor", isSucceed);
	}

	public static void setTextOnKindEditor(String editorId, String text) {
		boolean isSucceed = false;
		try {
			String javascript = "KE.html('" + editorId + "').'<p>" + text + "</p>');";
			jsExecutor(javascript);
			logger.info("input text [" + text + " ] to KindEditor [" + editorId + " ] success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("setTextOnKindEditor", isSucceed);
	}

	public static void setTextOnUEEditor(String editorId, String text) {
		boolean isSucceed = false;
		try {
			String javascript = "UE.getEditor('" + editorId + "').setContent('" + text + "');";
			jsExecutor(javascript);
			logger.info("input text [" + text + " ] to UEEditor [" + editorId + " ] success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("setTextOnUEEditor", isSucceed);
	}

	/**
	 * 根据指定的内容获取元素
	 * 
	 * @param content
	 * @return
	 */
	public static WebElement getElementByContent(String content) {
		boolean isSucceed = false;
		WebElement webElement = null;	
		try {
			webElement = getElement(By.xpath("//*[contains(.,'" + content + "')]"));
			logger.info("get element by content [" + content + "] success!");
			isSucceed = true;
		} catch (NoSuchElementException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("getElementByContent", isSucceed);
		return webElement;
	}

	public static Object jsExecutor(String js) {
		logger.info("execute js [:" + js + " ]");
		return ((JavascriptExecutor) driver).executeScript(js);
	}

	public static Object jsExecutor(String js, Object... args) {
		logger.info("execute js [:" + js + " ],arguments are:" + args.toString());
		return ((JavascriptExecutor) driver).executeScript(js, args);
	}

	/**
	 * for selecting date of calendar control
	 * 
	 * @param elementId
	 * @param text
	 * @param isReadOny
	 */
	public static void setTextById(String elementId, String text, boolean isReadOny) {
		boolean isSucceed = false;
		try {
			if (isReadOny) {
				jsExecutor("window.document.getElementById(" + elementId
						+ ").removeAttribute('readOnly');window.document.getElementById(" + elementId
						+ ").setAttributea('value'," + text + ");");
			} else {
				jsExecutor("window.document.getElementById(" + elementId + ").setAttribute('value','" + text + "');");
			}
			logger.info("input text [" + text + " ] to [" + elementId + "] success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("setTextById", isSucceed);
	}

	public static void setTextByElement(WebElement element, String text, boolean isReadOny) {
		boolean isSucceed = false;
		try {		
			if (isReadOny) {
				jsExecutor("arguments[0].removeAttribute('readOnly');arguments[0].setAttribute('value'," + text + ");",
						element);
			} else {
				jsExecutor("arguments[0].setAttribute('value','" + text + "');", element);
			}
			logger.info("input text [" + text + " ] to [" + element.toString() + "] success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("setTextByElement", isSucceed);
	}
	/**
	 * 获取当前时间
	 * 
	 * @return 返回"yyyy-MM-dd HH:mm:ss格式的当前时间
	 */
	public static String getNowTime() {
		return getFormatedTime("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获取当前日期
	 * @return 返回"yyyy-MM-dd格式的当前日期
	 */
	public static String getNowDate() {
		return getFormatedTime("yyyy-MM-dd");
	}
	// 设置元素属性值
	public static void setAttribute(WebElement element, String attributeName, String value) {	
		boolean isSucceed = false;
		try {			
			jsExecutor("arguments[0].setAttribute(arguments[1],arguments[2])", element, attributeName,value);		
			logger.info("set value [" + value + " ] to [" + attributeName + "] of element [" + element.toString() + "] success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("setTextByElement", isSucceed);
	}

	/**
	 * 获取屏幕快照
	 * 
	 * @param driver   WebDriver
	 * @param fileName 截图文件名，不包括文件路径
	 * @return 截图保存路径
	 */
	public static void takeScreenshot(String fileName) {			
		try {
			File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);	
			File file = new File(capturePath);
			if  (!file.exists()  && !file.isDirectory())      
			{       			  
			    file.mkdir();    
			} 
			FileUtils.copyFile(screenShotFile, new File(capturePath + fileName));
			logger.info("take screenshot [" + fileName + "] success!");		
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}	
	}

	// 页面元素截图
	public static File captureElement(WebElement element) throws Exception {	
		File screen = null;
		boolean isSucceed = false;
		try {
			WrapsDriver wrapsDriver = (WrapsDriver) element;
			// 截图整个页面
			screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
			BufferedImage img = ImageIO.read(screen);
			// 获得元素的高度和宽度
			int width = element.getSize().getWidth();
			int height = element.getSize().getHeight();
			// 创建一个矩形使面上面的高度，和宽度
			Rectangle rect = new Rectangle(width, height);
			// 得到元素的坐标
			org.openqa.selenium.Point p = element.getLocation();
			BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
			// 存为png格式
			ImageIO.write(dest, "png", screen);			
			logger.info("capture element screenshot [" + element.toString() + "] success!");
			isSucceed = true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		operationCheck("captureElement", isSucceed);
		return screen;
	}

	// 高亮元素
	public static void highlightElement(WebElement element) {
		boolean isSucceed = false;
		// 为元素设置style来高亮
		try {
			WrapsDriver wrappedElement = (WrapsDriver) element;			
			JavascriptExecutor driver = (JavascriptExecutor) wrappedElement.getWrappedDriver();	
			driver.executeScript("arguments[0].setAttribute('style',arguments[1]);", element,
					"color: green; border: 2px solid yellow;");
			// 取消高亮将style清掉
			pause(pauseTime);
			driver.executeScript("arguments[0].setAttribute('style',arguments[1]);", element, "");
			pause(pauseTime);
			logger.info("highlight element [" + element.toString() + "] success!");
			isSucceed = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		operationCheck("captureElement", isSucceed);
	}
}