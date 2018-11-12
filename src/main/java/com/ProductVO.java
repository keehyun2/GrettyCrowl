package com;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class ProductVO {
	
	@Expose
	private String searchKeyword;
	@Expose
	private String imgUrl;
	@Expose
	private String tit;
	@Expose
	private int price;
	@Expose
	private double imgMinDiff = 100.0; // 최소 차이율
	@Expose
	private List<ProductVO> groups = new ArrayList<ProductVO>(); // 비슷한 이미지 그룹
	
	private BufferedImage imgBuf; // 이미지 버퍼
	
}
