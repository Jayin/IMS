package com.ims.service;

import java.io.IOException;

import com.ims.common.ScreenShot;

/**
 * 定时截图器 0.必须设置回调函数 1.在start()前设置截图时间间隔waittingTime 3.启动截图start()
 * 
 * @author Jayin Ton
 * 
 */
public class ScreenShoter extends Thread {
	// 单例
	private static ScreenShoter instance = ScreenShoter.getInstance();
	// 截图时间间隔
	private int waittingTime = 1000;
	// 在运行
	private boolean isRunning = false;
	// 截图
	private byte[] imageBytes = null;
	private ScreenShoter.Callback callback = null;

	/**
	 * 停止截图
	 */
	public void stopShotting() {
		isRunning = false;
	}

	/**
	 * 每隔waittingTime截一次图
	 */
	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			try {
				imageBytes = ScreenShot.getImgeBytes();
				callback.onShotScreen(imageBytes);
				sleep(waittingTime);
			} catch (IOException e) {
				e.printStackTrace();
				callback.onError(e, "ScreenShoter:run():IOException");

			} catch (InterruptedException e) {
				e.printStackTrace();
				callback.onError(e, "ScreenShoter:run():InterruptedException");
			}

		}
	}

	// 设置回调函数
	public void setCallback(ScreenShoter.Callback _callback) {
		callback = _callback;
	}

	private static ScreenShoter getInstance() {
		if (instance == null)
			instance = new ScreenShoter();
		return instance;
	}

	// public byte[] getImageBytes() {
	// return imageBytes;
	// }

	public int getWaittingTime() {
		return waittingTime;
	}

	public void setWaittingTime(int waittingTime) {
		this.waittingTime = waittingTime;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public static void main(String[] args) {

	}

	/**
	 * 回调函数
	 * 
	 * @author Jayin Ton
	 * 
	 */
	public interface Callback {
		/**
		 * IOException:截图失败 InterruptedException：线程睡眠异常
		 * 
		 * @param e
		 * @param description
		 */
		public void onError(Exception e, String description);

		public void onShotScreen(byte[] img);
	}
}
