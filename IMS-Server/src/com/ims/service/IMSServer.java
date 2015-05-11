package com.ims.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * 服务端 Component : ThreadPool + ServerSocket 直接start 启动
 * 任务：等待连接，连接好直接丢到ThreadPool
 * 
 * @author Jayin Ton
 * 
 */
public class IMSServer {

	private static IMSServer mImsServer = null;

	public static int LONGPORT = 10007; // 长连接端口
	private ServerSocket longSocket;

	public static int SHORTPORT = 10006; // 长连接端口
	private ServerSocket shortSocket;

	// 应该提供add(Socket socket) close() isClose()方法
	private ThreadPool threadPool;
	private ThreadPoolForShort threadPoolForShort;

	private IMSServer() {
	}

	public static IMSServer getInstance() {
		if (mImsServer == null) {
			mImsServer = new IMSServer();
		}
		return mImsServer;
	}

	// 初始化，尝试监听10007 端口，如果不行，就向上加1，直到监听成功
	// 端连接尝试监听10006，如果不行，就加 -1，直到成功
	private void init() {
		initLong();
		initShort();
		threadPool = ThreadPool.getInstance();
		threadPoolForShort = ThreadPoolForShort.getInstance();
		System.out.println(threadPool + "new");
	}

	private void initShort() {
		try {
			shortSocket = new ServerSocket(SHORTPORT);
		} catch (IOException e) {
		} finally {
			if (shortSocket == null) {
				SHORTPORT--;
				initShort();
			}
		}
	}

	private void initLong() {
		try {
			longSocket = new ServerSocket(LONGPORT);
		} catch (IOException e) {
		} finally {
			if (longSocket == null) {
				LONGPORT++;
				initLong();
			}
		}
	}

	// 启动
	public void start() {
		init();
		new Thread(new LongSocketAcceptor()).start();
		new Thread(new ShortSocketAcceptor()).start();
	}

	/**
	 * 长连接的处理线程
	 */
	private class LongSocketAcceptor implements Runnable {

		@Override
		public void run() {

			while (true) {
				try {
					// 当一个新用户进来了
					Socket socket = longSocket.accept();
					threadPool.add(socket);
					System.out.println("有用户进来了");
				} catch (SocketException e1) {
					// close() 应该退出。
					if (longSocket.isClosed())
						return;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 长连接的处理线程
	 */
	private class ShortSocketAcceptor implements Runnable {

		@Override
		public void run() {

			while (true) {
				try {
					// 当一个新用户进来了
					Socket socket = shortSocket.accept();
					threadPoolForShort.add(socket);
					System.out.println("短连接有用户进来了");
				} catch (SocketException e1) {
					// close() 应该退出。
					if (shortSocket.isClosed())
						return;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	// 销毁全部的资源
	public void stop() {

		System.out.println("Ims stop --1");
		try {
			// 长连接关闭，这个时候longSocket.accept()会抛出SocketException
			// 应该退出
			if (longSocket != null) {
				longSocket.close();
			}

		} catch (IOException e) {

		} finally {
			// 线程池已经连接好的用户也应该停止
			if (!threadPool.isClose()) {
				threadPool.close();
			}
			if (!threadPoolForShort.isClose()) {
				threadPoolForShort.close();
			}
		}

		mImsServer = null;
	}

	public int getLongPort() {
		return LONGPORT;
	}

	public int getShortPort() {
		return SHORTPORT;
	}

}
