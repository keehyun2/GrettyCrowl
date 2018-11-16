package com.selenium;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.ProductVO;
import com.util.ImageDiff;

public class WebRunnable implements Runnable {
	
	String url;
	List<ProductVO> syncList;
	
	public WebRunnable(String url, List<ProductVO> syncList) {
		this.url = url;
		this.syncList = syncList;
	}

	@Override
	public void run() {
		
		EventFiringWebDriver eDriver = new WebDriverConfig().getWebDriver(); 
		ImageDiff df = new ImageDiff();
		
		eDriver.get(this.url);
		List<WebElement> listGoods = eDriver.findElements(By.className("_itemSection"));
		System.out.println("리스트 파싱 완료");
		for (WebElement webElement : listGoods) {
			ProductVO productVO = new ProductVO();
			productVO.setImgUrl(webElement.findElement(By.className("_productLazyImg")).getAttribute("src"));
			productVO.setTit(webElement.findElement(By.className("tit")).getText());
			productVO.setPrice(Integer.parseInt(webElement.findElement(By.className("_price_reload")).getText().replaceAll(",", "")));
			try {
				productVO.setImgBuf(df.getWebImg(productVO.getImgUrl()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.syncList.add(productVO);
		}
	}

}
