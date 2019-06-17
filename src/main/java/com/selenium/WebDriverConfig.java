package com.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class WebDriverConfig {
	
	/**
	 * 셀레니움 webDriver 설정
	 * @return
	 */
	public EventFiringWebDriver getWebDriver() {

		// 웹드라이버 설정 영역
		WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
//		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
		
		WebEventListener eventListener = new WebEventListener();

		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		EventFiringWebDriver eDriver = new EventFiringWebDriver(driver); // 이벤트 등록
		eDriver.register(eventListener);
		return eDriver;
	}
}
