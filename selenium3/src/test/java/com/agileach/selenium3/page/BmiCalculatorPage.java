package com.agileach.selenium3.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import com.agileach.selenium3.Util;

public class BmiCalculatorPage {
	//使用id或者name属性自动获取该元素，无需使用@FindBy注解
	@FindBy(id = "heightCMS")
	@CacheLookup
	private WebElement heightField;
	
	private WebElement weightKg;
	private WebElement bmi;
	private WebElement bmi_category;
	private WebElement Calculate;	
	Util util = new Util();
	
	public BmiCalculatorPage(WebDriver driver) {	
		PageFactory.initElements(driver, this);
	}

	public void calculateBmi(String height, String weight) {
		heightField.clear();
		heightField.sendKeys(height);
		weightKg.clear();
		weightKg.sendKeys(weight);				
		Calculate.click();
	}

	public String getBmi() {
		return bmi.getAttribute("value");
	}

	public String getBmiCategory() {
		return bmi_category.getAttribute("value");
	}
}
