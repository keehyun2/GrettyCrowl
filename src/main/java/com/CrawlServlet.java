package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrawlServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		// 로그 설정
		Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		
		// 검색어 필수 체크
		if (request.getParameter("keyword") == null || "".equals(request.getParameter("keyword"))) {
			out.println("{msg : '검색키워드가 입력되지 않았습니다.'}");
			out.flush();
		}else {
			//out.println("검색키워드 : " + request.getParameter("keyword"));
			NaverService ns = new NaverService();
			String result = "{}";
				try {
					result = ns.collectProductList(request.getParameter("keyword"));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			out.print(result);
		}
		out.flush();
	}
}

