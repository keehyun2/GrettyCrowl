package com;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBUtil {
	
	static MongoDatabase db = null;
	
	/**
	 * 몽고 디비에서 컬렉션 객체를 가져옴.
	 * @param collectionName
	 * @return
	 */
	public MongoCollection<Document> getCollection(String collectionName) {
		if(db == null) {
			MongoClientURI uri = new MongoClientURI("mongodb://product1:product1@ds147723.mlab.com:47723/product?authSource=product");
			MongoClient mongoClient = new MongoClient(uri);
			DBUtil.db = mongoClient.getDatabase("product");
		}
		return db.getCollection(collectionName);
	}
	
	/**
	 * 콜렉션에 리스트 입력
	 * @param collection
	 * @param list
	 * @param searchKeyword
	 * @return
	 */
	public int insertList(MongoCollection<Document> collection, List<WebElement> list, String searchKeyword) {
		
		List<Document> docList = new ArrayList<Document>();
		for (WebElement webElement : list) {

			Document doc = new Document();
			doc.append("searchKeyword", searchKeyword);
			doc.append("imgUrl", webElement.findElement(By.className("_productLazyImg")).getAttribute("src"));
			doc.append("tit", webElement.findElement(By.className("tit")).getText());
			docList.add(doc);
		}
		collection.insertMany(docList);
		return docList.size();
	}
}
