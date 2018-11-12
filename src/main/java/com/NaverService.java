package com;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NaverService {
	
	/**
	 * 데이터 수집
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public String collectProductList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		EventFiringWebDriver eDriver = new WebDriverConfig().getWebDriver(); 
		DBUtil dbUtil = new DBUtil(); // 몽고 디비 연결
		ImageDiff imageDiff = new ImageDiff(); 

		// 실제 셀레니움을 사용해서 naver 쇼핑 데이터를 크롤링
		String searchKeyword = URLEncoder.encode(request.getParameter("keyword"), "UTF-8"); 
		StringBuilder sb = new StringBuilder();
		sb.append("https://search.shopping.naver.com/search/all.nhn");
		sb.append("?origQuery=%s&pagingIndex=1&pagingSize=10&viewType=list&sort=price_asc&frm=NVSHATC&sps=N&query=%s");
		String url = String.format(sb.toString(), searchKeyword, searchKeyword);
		eDriver.get(url);
		List<WebElement> listGoods = eDriver.findElements(By.className("_itemSection"));
		System.out.println("상품 수 : " + listGoods.size());

//		int cnt = dbUtil.insertList(dbUtil.getCollection("naver"), listGoods, searchKeyword); // 몽고 디비 입력
//		return cnt + "건 수집완료";
		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		
		List<ProductVO> prodList = new ArrayList<ProductVO>();
		for (WebElement webElement : listGoods) {

			ProductVO productVO = new ProductVO();
			productVO.setSearchKeyword(request.getParameter("keyword"));
			productVO.setImgUrl(webElement.findElement(By.className("_productLazyImg")).getAttribute("src"));
			productVO.setTit(webElement.findElement(By.className("tit")).getText());
			productVO.setPrice(Integer.parseInt(webElement.findElement(By.className("_price_reload")).getText().replaceAll(",", ""))); 
			productVO.setImgBuf(imageDiff.getWebImg(productVO.getImgUrl()));
			prodList.add(productVO);
		}
		
		List<ProductVO> printList = new ArrayList<ProductVO>();
		
		prod:for (int i = 0; i < prodList.size(); i++) {
			double diff = 0.0;
			print:for (int j = 0; j < printList.size(); j++) {
				diff = imageDiff.getDifferencePercent(prodList.get(i).getImgBuf(),printList.get(j).getImgBuf());
				if(diff < 20.0){
					prodList.get(i).setImgMinDiff(diff);
					printList.get(j).getGroups().add(prodList.get(i));
					continue prod;
				}
			}
			printList.add(prodList.get(i));
		}
		
		return gson.toJson(printList);
		
	}
	
}
