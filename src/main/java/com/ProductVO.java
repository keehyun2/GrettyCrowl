package com;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class ProductVO {
	
//	@Expose
//	private String searchKeyword;
	@Expose
	private String imgUrl;
	@Expose
	private String tit;
	@Expose
	private int price;
	@Expose
	private double imgSimilarity = 0.0; // 이미지 유사율 - 상위 상품과
	@Expose
	private double txtSimilarity = 0.0; // 텍스트 유사율 - 상위 상품과
	
	@Expose
	private List<ProductVO> groups = new ArrayList<ProductVO>(); // 비슷한 이미지 그룹
	
	private BufferedImage imgBuf; // 이미지 버퍼

	//private double imgMinDiff = 100.0; // 최소 차이율 (사용안함)
}
