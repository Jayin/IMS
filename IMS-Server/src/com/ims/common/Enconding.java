package com.ims.common;
import java.util.Hashtable;
import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Enconding {

	private static final int BLACK = 0xff000000; // 4个字节，每一个字节为两个16 进制数，分别为ARGB

	public static BufferedImage createQRCode(String str, int widthAndHeight)
			throws WriterException {

		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();

		BufferedImage image = new BufferedImage(width, width,
				BufferedImage.TYPE_4BYTE_ABGR);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) { // 得到(x,y)坐标是不是为 黑色
					image.setRGB(x, y, BLACK); // 设置好一个像素的值
				} 
			}
		}

		return image;
	}
}
