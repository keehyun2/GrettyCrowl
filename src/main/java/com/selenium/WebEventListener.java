package com.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class WebEventListener extends AbstractWebDriverEventListener {

	public void beforeNavigateTo(String url, WebDriver driver) {
//		System.out.println("Before navigating to: '" + url + "'");
	}

	public void afterNavigateTo(String url, WebDriver driver) {
		System.out.println("Navigated to:'" + url + "'");
		System.out.println("Page title: " + driver.getTitle());
	}

	public void beforeClickOn(WebElement element, WebDriver driver) {
//		System.out.println("Trying to click on: " + element.toString());
	}

	public void afterClickOn(WebElement element, WebDriver driver) {
		System.out.println("Clicked on: " + element.toString());
		System.out.println("Page title: " + driver.getTitle());
	}

	public void onException(Throwable error, WebDriver driver) {
		System.out.println("Error occurred: " + error);
	}
}