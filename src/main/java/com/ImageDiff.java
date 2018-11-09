package com;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

public class ImageDiff {

	private double getDifferencePercent(String imgUrl1, String imgUrl2) throws IOException {
		
		System.setProperty("http.agent", "Chrome");
		
		BufferedImage img1 = getWebImg(imgUrl1);
		BufferedImage img2 = getWebImg(imgUrl2);
		
		int width = img1.getWidth();
		int height = img1.getHeight();
		int width2 = img2.getWidth();
		int height2 = img2.getHeight();
		if (width != width2 || height != height2) {
			throw new IllegalArgumentException(String.format(
					"Images must have the same dimensions: (%d,%d) vs. (%d,%d)", width, height, width2, height2));
		}

		long diff = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
			}
		}
		long maxDiff = 3L * 255 * width * height;

		return 100.0 * diff / maxDiff;
	}

	private int pixelDiff(int rgb1, int rgb2) {
		int r1 = (rgb1 >> 16) & 0xff;
		int g1 = (rgb1 >> 8) & 0xff;
		int b1 = rgb1 & 0xff;
		int r2 = (rgb2 >> 16) & 0xff;
		int g2 = (rgb2 >> 8) & 0xff;
		int b2 = rgb2 & 0xff;
		return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
	}
	
	private BufferedImage getWebImg(String urlStr) throws IOException {
		URL url = new URL(urlStr);
//		URLConnection uc = urlObj.openConnection();
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.connect();
		con.getInputStream();
		return ImageIO.read(url);
	} 

}
