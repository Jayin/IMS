package com.ims.common;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

public class ScreenShot {
	public static String DefaultImageFormat = "PNG";
	/**
	 * 保存截图 文件保存格式（拓）： SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyymmddHHmmss"); String name = sdf.format(new Date());
	 * 
	 * @param img
	 *            图像
	 * @param savePath
	 *            保存的文件路径
	 * @param imgName
	 *            名称（不含后缀）
	 * @param imageFormat
	 *            图像格式JPG/JPEG/PNG
	 * @return
	 */
	public static boolean saveImage(BufferedImage img, String savePath,
			String imgName, String imageFormat) {
		boolean flag = false;
		if (img == null || savePath == null || savePath.equals(""))
			return false;
		// 确保保存到这个文件目录下
		if (!(savePath.charAt(savePath.length() - 1) == '/'))
			savePath += "/";
		File f = new File(savePath + imgName + "." + imageFormat);
		if (!f.exists())
			f.mkdirs();
		System.out.println("Save File " + f.getAbsolutePath());
		// 将screenshot对象写入图像文件
		try {
			flag = ImageIO.write(img, imageFormat, f);
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	 
	/**
	 * 把一个BufferedImage 转化为byte[]
	 * @param img
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] ImgeToBytes(BufferedImage img) throws IOException{
		ByteArrayOutputStream out  =new ByteArrayOutputStream();
		ImageIO.write(img, DefaultImageFormat, out); 
		return out.toByteArray();
	}
	/**
	 * 用byte[]构造一个BufferedImage
	 * @param buf
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage BytesToImge(byte[] buf) throws IOException{
		ByteArrayInputStream input = new ByteArrayInputStream(buf);
		return ImageIO.read(input);
	}

	/**
	 * 截图 获得一个BufferedImage
	 * 
	 * @return
	 */
	public static BufferedImage getImage() {
		Dimension d = getScreenSize();
		BufferedImage screenshot = null;
		try {
			screenshot = (new Robot()).createScreenCapture(new Rectangle(0, 0,
					(int) d.getWidth(), (int) d.getHeight()));
		} catch (Exception e) {
			// TODO: handle exception
		}

		return screenshot;
	}
   /**
    * 截图 获得一个byte[]
    * @return
    * @throws IOException
    */
	public static byte[] getImgeBytes() throws IOException {
		BufferedImage img = getImage();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "PNG", out);
		return out.toByteArray();

	}

	/**
	 * 获得电脑的屏幕大小(分辨率 像数)
	 * 
	 * @return
	 */
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static void main(String[] arg) {
		BufferedImage img = getImage();
		if (saveImage(img, "d://JavaFileTemp", "test", "jpg")) {
			System.out.println("ok");
		} else {
			System.out.println("faild");
		}
	}
}
