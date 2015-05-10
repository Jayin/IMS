package com.ims.service;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池 任务：有socket 加入进来，就增加一个Task,把socket 穿进去，关联着一个socket，发送图片
 * 
 * 客户端请求socket断开：应该放在 Task 中处理，返回谁断开，在ThreadPool 去除, 我们应该设置一个回调方法，就用Runnable
 * 
 * 服务器请求socket断开：应该在ThreadPool 中处理
 * 
 * @author web
 * 
 */
public class ThreadPool {

	/* 单例 */
	private static ThreadPool instance = null;

	// 线程池，这里用有缓存的线程池
	ExecutorService threadPool = null;

	// 线程池现在的状态
	private boolean isClose = true;

	// 保存正在连接着的socket，当有一个socket断开，应该remove
	final HashMap<Integer, Socket> sockets = new HashMap<Integer, Socket>();

	int socketId = 0;// 只能向上加，保证唯一性

	// 实例new 出，也new 出线程池
	private ThreadPool() {
		isClose = false;
		threadPool = Executors.newCachedThreadPool();
	}

	public static synchronized ThreadPool getInstance() {
		if (instance == null)
			instance = new ThreadPool();
		return instance;
	}

	// 增加socket，就封装好任务，对到线程池完成任务。发送图片。
	public void add(Socket socket) {

		sockets.put(socketId, socket);
		// 当和socket断开的时候就是任务完成的时候
		threadPool.execute(new Task(socket, socketId, new Task.CallBack() {
			// 任务完成就调用这个函数，remove 完成的任务
			public void Call(int socketId) {
				sockets.remove(socketId);
				System.out.println(sockets.size());
			}
		}));
		socketId++;
	}

	public boolean isClose() {
		return isClose;
	}

	public void close() {
		//关闭线程池
		threadPool.shutdownNow();
		
		//遍历全部还连接的socket，之后关闭
		Iterator<Integer> iterator = sockets.keySet().iterator();
		while (iterator.hasNext()) {
			Socket socket = sockets.get(iterator.next());
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		isClose = true;
		
		instance = null;
	}
}
