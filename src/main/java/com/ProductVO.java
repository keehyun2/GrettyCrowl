package com;

public class ProductVO {

	private String searchKeyword;
	private String imgUrl;
	private String tit;
	private int price;
	private double imgMinDiff;
	
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
	
}
