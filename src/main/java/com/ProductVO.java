package com;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

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
	
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTit() {
		return tit;
	}
	public void setTit(String tit) {
		this.tit = tit;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public double getImgMinDiff() {
		return imgMinDiff;
	}
	public void setImgMinDiff(double imgMinDiff) {
		this.imgMinDiff = imgMinDiff;
	}
	public List<ProductVO> getGroups() {
		return groups;
	}
	public void setGroups(List<ProductVO> groups) {
		this.groups = groups;
	}
	public BufferedImage getImgBuf() {
		return imgBuf;
	}
	public void setImgBuf(BufferedImage imgBuf) {
		this.imgBuf = imgBuf;
	}
	
}
