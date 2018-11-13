package com;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class WebDriverConfig {
	
	/**
	 * 셀레니움 webDriver 설정
	 * @return
	 */
	public EventFiringWebDriver getWebDriver() {

		WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
		WebEventListener eventListener = new WebEventListener();

		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		EventFiringWebDriver eDriver = new EventFiringWebDriver(driver); // 이벤트 등록
		eDriver.register(eventListener);
		return eDriver;
	}
}
