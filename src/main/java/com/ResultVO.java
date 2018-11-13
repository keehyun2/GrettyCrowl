package com;

import java.util.List;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class ResultVO {
	
	@Expose
	private String searchKeyword;
	@Expose
	private List<ProductVO> productList;
	@Expose
	private String recommendedText; // 추천가격
	@Expose
	private int recommendedPrice; // 추천가격
	
}
