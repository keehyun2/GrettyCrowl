package com;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.selenium.WebDriverConfig;
import com.util.DBUtil;
import com.util.ImageDiff;

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
		ImageDiff imageDiff = new ImageDiff(); // image 차이점 or 차이점
		JaroWinklerDistance jwd = new JaroWinklerDistance(); // 텍스트 유사성
		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		
		// 실제 셀레니움을 사용해서 naver 쇼핑 데이터를 크롤링
		String searchKeyword = URLEncoder.encode(request.getParameter("keyword"), "UTF-8");  
		StringBuilder sb = new StringBuilder();
		sb.append("https://search.shopping.naver.com/search/all.nhn");
		sb.append("?origQuery=%s&pagingIndex=1&pagingSize=80&viewType=list&sort=price_asc&frm=NVSHATC&sps=Y&query=%s");
		String url = String.format(sb.toString(), searchKeyword, searchKeyword);
		
		System.out.println("객체생성 완료");
		eDriver.get(url);
		List<WebElement> listGoods = eDriver.findElements(By.className("_itemSection"));
		String recommendedText = eDriver.findElement(By.className("info_align_low")).getText();
		int recommendedPrice = Integer.parseInt(recommendedText.replaceAll("[^0-9]", "") + "0000");
		
		System.out.println(recommendedText);
		System.out.println("상품 수 : " + listGoods.size());
//		int cnt = dbUtil.insertList(dbUtil.getCollection("naver"), listGoods, searchKeyword); // 몽고 디비 입력
//		return cnt + "건 수집완료";
		
		List<ProductVO> prodList = new ArrayList<ProductVO>();
		for (WebElement webElement : listGoods) {
			ProductVO productVO = new ProductVO();
			//productVO.setSearchKeyword(request.getParameter("keyword"));
			productVO.setImgUrl(webElement.findElement(By.className("_productLazyImg")).getAttribute("src"));
			productVO.setTit(webElement.findElement(By.className("tit")).getText());
			productVO.setPrice(Integer.parseInt(webElement.findElement(By.className("_price_reload")).getText().replaceAll(",", ""))); 
			productVO.setImgBuf(imageDiff.getWebImg(productVO.getImgUrl()));
			prodList.add(productVO);
		}
		
		for (int i = 0; i < 5; i++) {
			sb = new StringBuilder();
			sb.append("https://search.shopping.naver.com/search/all.nhn?");
			sb.append("?origQuery=%s&pagingIndex=1&pagingSize=80&productSet=hotdeal&viewType=list&sort=price_asc&minPrice=%d&maxPrice=%d&frm=NVSHPAG&sps=Y&query=%s");
			url = String.format(sb.toString(), searchKeyword, recommendedPrice - (1000 * i) - 1000, recommendedPrice - (1000 * i), searchKeyword);
			eDriver.get(url);
			listGoods = eDriver.findElements(By.className("_itemSection"));
			for (WebElement webElement : listGoods) {
				ProductVO productVO = new ProductVO();
				//productVO.setSearchKeyword(request.getParameter("keyword"));
				productVO.setImgUrl(webElement.findElement(By.className("_productLazyImg")).getAttribute("src"));
				productVO.setTit(webElement.findElement(By.className("tit")).getText());
				productVO.setPrice(Integer.parseInt(webElement.findElement(By.className("_price_reload")).getText().replaceAll(",", ""))); 
				productVO.setImgBuf(imageDiff.getWebImg(productVO.getImgUrl()));
				prodList.add(productVO);
			}
		}
		
		System.out.println("상품 read 완료");
		List<ProductVO> printList = new ArrayList<ProductVO>();
		
		for (WebElement webElement : listGoods) {
			ProductVO productVO = new ProductVO();
			//productVO.setSearchKeyword(request.getParameter("keyword"));
			productVO.setImgUrl(webElement.findElement(By.className("_productLazyImg")).getAttribute("src"));
			productVO.setTit(webElement.findElement(By.className("tit")).getText());
			productVO.setPrice(Integer.parseInt(webElement.findElement(By.className("_price_reload")).getText().replaceAll(",", ""))); 
			productVO.setImgBuf(imageDiff.getWebImg(productVO.getImgUrl()));
			prodList.add(productVO);
		}
		
		// 이미지 유사성으로 구분한 하위 상품 구분
		prod:for (int i = 0; i < prodList.size(); i++) {
			double simil = 0.0;
			double txtSimil = 0.0;
			print:for (int j = 0; j < printList.size(); j++) {
				simil = imageDiff.getSimilarity(prodList.get(i).getImgBuf(),printList.get(j).getImgBuf());
				txtSimil = jwd.apply(prodList.get(i).getTit(), prodList.get(j).getTit()) * 100;
				if(simil > 90.0){ // 흰색 여백이 많은 이미지들 때문에 상품이 많이 다른 데도 유사성이 높다고 나타남. 그래서 80에서 90으로 올림..
					prodList.get(i).setImgSimilarity(simil);
					prodList.get(i).setTxtSimilarity(txtSimil);
					printList.get(j).getGroups().add(prodList.get(i));
					continue prod;
				}
			}
			printList.add(prodList.get(i));
		}
		System.out.println("이미지, 텍스트 비교 완료");
		
		ResultVO resultVO = new ResultVO();
		resultVO.setProductList(printList);
		resultVO.setSearchKeyword(searchKeyword);
		resultVO.setRecommendedText(recommendedText);
		resultVO.setRecommendedPrice(recommendedPrice);
		
		return gson.toJson(resultVO);
		
	}
}
