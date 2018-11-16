package com;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.selenium.WebDriverConfig;
import com.selenium.WebRunnable;
import com.util.DBUtil;
import com.util.ImageDiff;

public class NaverService {

	/**
	 * 데이터 수집
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws URISyntaxException 
	 */
	public String collectProductList(String searchKeyword) throws IOException, InterruptedException, URISyntaxException {
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		EventFiringWebDriver eDriver = new WebDriverConfig().getWebDriver(); 
		DBUtil dbUtil = new DBUtil(); // 몽고 디비 연결
		JaroWinklerDistance jwd = new JaroWinklerDistance(); // 텍스트 유사성
		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		List<ProductVO> syncList = Collections.synchronizedList( new ArrayList<ProductVO>() );
		ImageDiff df = new ImageDiff();
		
		// url 생성
		URIBuilder builder = new URIBuilder();
		builder.setCharset(Charset.forName("UTF-8"));
		builder.setScheme("https");
		builder.setHost("search.shopping.naver.com");
		builder.setPath("/search/all.nhn");
		builder.addParameter("origQuery", searchKeyword);
		builder.addParameter("pagingIndex", "1");
		builder.addParameter("pagingSize", "80");
		builder.addParameter("viewType", "list");
		builder.addParameter("sort", "price_asc");
		builder.addParameter("minPrice", "0");
		builder.addParameter("maxPrice", "0");
		builder.addParameter("frm", "NVSHPRC");
		builder.addParameter("sps", "Y");
		builder.addParameter("query", searchKeyword);
		
		System.out.println("객체생성 완료");
		
		eDriver.get(builder.build().toURL().toString());
		List<WebElement> listGoods = eDriver.findElements(By.className("_itemSection"));
		String recommendedText = eDriver.findElement(By.className("info_align_low")).getText();
		int recommendedPrice = Integer.parseInt(recommendedText.replaceAll("[^0-9]", "") + "0000");
		
		System.out.println(recommendedText);
		System.out.println("상품 수 : " + listGoods.size());
		
		for (WebElement webElement : listGoods) {
			ProductVO productVO = new ProductVO();
			productVO.setImgUrl(webElement.findElement(By.className("_productLazyImg")).getAttribute("src"));
			productVO.setTit(webElement.findElement(By.className("tit")).getText());
			productVO.setPrice(Integer.parseInt(webElement.findElement(By.className("_price_reload")).getText().replaceAll(",", ""))); 
			productVO.setImgBuf(df.getWebImg(productVO.getImgUrl()));
			syncList.add(productVO);
		}
		
		for (int i = 0; i < 1; i++) {
			builder.addParameter("minPrice", String.valueOf(recommendedPrice - (1000 * i) - 1000));
			builder.addParameter("maxPrice", String.valueOf(recommendedPrice - (1000 * i)));
			executorService.execute(new WebRunnable(builder.build().toURL().toString(), syncList));
		}
		
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		
		System.out.println("상품 read 완료");
		
//		for (ProductVO vo : syncList) {
//			System.out.println("이미지 가져오는중..");
//			vo.setImgBuf(df.getWebImg(vo.getImgUrl()));
//		}
		df.getWebImg(syncList);
		
		System.out.println("이미지 버퍼 저장 완료");
		
		
		List<ProductVO> printList = new ArrayList<ProductVO>();
		
		// 이미지 유사성으로 구분한 하위 상품 구분
		prod:for (int i = 0; i < syncList.size(); i++) {
			for (int j = 0; j < printList.size(); j++) {
				double simil = ImageDiff.getSimilarity(syncList.get(i).getImgBuf(),printList.get(j).getImgBuf());
				double txtSimil = jwd.apply(syncList.get(i).getTit(), syncList.get(j).getTit()) * 100;
				if(simil > 90.0){ // 흰색 여백이 많은 이미지들 때문에 상품이 많이 다른 데도 유사성이 높다고 나타남. 그래서 80에서 90으로 올림..
					syncList.get(i).setImgSimilarity(simil);
					syncList.get(i).setTxtSimilarity(txtSimil);
					printList.get(j).getGroups().add(syncList.get(i));
					continue prod;
				}
			}
			printList.add(syncList.get(i));
		}
		
		for (ProductVO vo : printList) {
			if(!vo.getGroups().isEmpty()) {
				Collections.sort(vo.getGroups());
			}
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
