package com.ims.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.ims.common.ScreenShot;

/**
 * 任务(向客户端发送) 
 * 1.可以设置回调函数Task.CallBack
 * 2.接受socket，发送图片，如果和Socket 断开就关闭socket，调用回调函数 
 * 
 * @author Jayin Ton
 * 
 */
public class Task implements Runnable {

	private CallBack callback;
	private Socket socket;
	int socketId;
	
	public Task(Socket _socket,int _socketId, CallBack _callback) {
		socketId = _socketId;
		socket = _socket;
		callback = _callback;
	}


	/**
	 * 回调接口
	 * 
	 * @author Jayin Ton
	 * 
	 */
	public interface CallBack {
		public void Call(int socketId);
	}

	//发送图片
	@Override
	public void run() {

		BufferedOutputStream out = null; //socket的流
		byte[] data;//保存图片
		
		try {
			out = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("socket 断开");
		}
		while (true) {
			try {
				data = ScreenShot.getImgeBytes();
				out.write(data);
				out.flush();
				Thread.sleep(1000);
				
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}  catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}catch (Exception e) {
				e.printStackTrace();
				break;
			} 
		}
		if(!socket.isClosed()){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			callback.Call(socketId);
			return;
		}
		
		//最后要调用回调函数
		callback.Call(socketId);
	}
	
}
