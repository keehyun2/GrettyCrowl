package com;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class NaverService {
	
	/**
	 * 데이터 수집
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String collectProductList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		
		EventFiringWebDriver e_driver = this.getWebDriver(); 
		DBUtil dbUtil = new DBUtil(); // 몽고 디비 연결

		// 실제 셀레니움을 사용해서 naver 쇼핑 데이터를 크롤링
		String searchKeyword = URLEncoder.encode(request.getParameter("keyword"), "UTF-8"); 
		StringBuilder sb = new StringBuilder();
		sb.append("https://search.shopping.naver.com/search/all.nhn");
		sb.append("?origQuery=%s&pagingIndex=1&pagingSize=40&viewType=list&sort=price_asc&frm=NVSHATC&sps=N&query=%s");
		String url = String.format(sb.toString(), searchKeyword, searchKeyword);
		e_driver.get(url);
		List<WebElement> list_goods = e_driver.findElements(By.className("_itemSection"));
		System.out.println("상품 수 : " + list_goods.size());

		int cnt = dbUtil.insertList(dbUtil.getCollection("naver"), list_goods, searchKeyword); // 몽고 디비 입력
		
		return cnt + "건 수집완료";
	}
	
	/**
	 * 셀레니움 webDriver 설정
	 * @return
	 */
	public EventFiringWebDriver getWebDriver() {
		
		WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
		WebEventListener eventListener = new WebEventListener();
		
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		EventFiringWebDriver e_driver = new EventFiringWebDriver(driver); // 이벤트 등록
		e_driver.register(eventListener);
		return e_driver;
	}
}
