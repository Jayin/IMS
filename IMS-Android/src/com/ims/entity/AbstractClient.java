package com.ims.entity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import android.os.Handler;

import com.ims.handler.AskHandler;
import com.ims.handler.DisplayHandler;
import com.ims.handler.DownloadHandler;
import com.ims.handler.IdentifyHandler;
import com.ims.utils.Constant;

/**
 * 抽象的Client 11.2 抽离短连接
 * 
 * @author Jayin Ton Feature： 1.默认端口为10006 2.添加CallBack 来处理切换状态onSwitch()
 *         和处理错误！3.必须在Handler前setHandlers()... 3.记得关闭closeClient() NOTE：
 */
public abstract class AbstractClient extends Thread implements ClientBehavior {
	protected Socket displyaSocket = null;
	
	//init
	protected boolean isInit,isInitIP,isInitHandler = true,isInitCallBack;
	protected String ip = null;
	protected static int port_long = 10007; // 长连接默认端口
	protected static int port_short = 10006; // 短连接默认端口
	protected AbstractClient.CallBack callback;
	protected DisplayHandler displayHandler;
	protected AskHandler askHandler;
	protected DownloadHandler downloadHandler;
	protected IdentifyHandler identifyHandler;
	
	
	//create
	protected int currentStatus = Constant.Status_Waitting;
	
	
	
	protected boolean isLogin = false;
	protected boolean isClosed = false;

	
	

	protected InetAddress dstAddress = null;

	protected String username = "admin";
	protected ArrayBlockingQueue<Integer> taskQueue = new ArrayBlockingQueue<Integer>(3);
	
	/**
	 * 增加任务进队列，如果队列慢了就阻塞
	 * @param task 状态常量
	 */
	public void addTask(int task){
		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 拿到任务，如果队列里面没有任务，就一直等待
	 * @return
	 */
	public int getTask(){
		try {
			return taskQueue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 创建还没有init的对象，之后应该init
	 */
	protected AbstractClient(){
	}
	/**
	 * 常规的构造方法
	 * 
	 * @param callback
	 *            不能为空
	 * @param ip
	 * @param port
	 */
	protected AbstractClient(AbstractClient.CallBack callback, String ip,
			int port_long,int port_short) {
		setCallback(callback);
		setIP(ip, port_long, port_short);
	}

	/**
	 * 默认端口
	 * 
	 * @param callback
	 * @param ip
	 */
	protected AbstractClient(AbstractClient.CallBack callback, String ip) {
		this(callback, ip, port_long, port_short);
	}

	/**
	 * 1.只初始化DisplaySocket(不初始化messageSocket),连接服务器
	 * 
	 * 
	 * @return ture if initialize successfully
	 */
	protected boolean init() {
		if (ip == null || "".equals(ip)) {
			return false;
		}
		try {
			dstAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			callback.onError(e, "未知主机");
			return false;
		}
		try {
			displyaSocket = new Socket(dstAddress, port_long);
		} catch (IOException e) {
			e.printStackTrace();
			callback.onError(e, "displyaSocket:无法连接到指定主机  ");
			return false;
		}
		return true;
	}

	/**
	 * 发送请求，返回响应
	 * 
	 * @param req
	 * @return 如果出错就返回null。
	 */
	public ResponseMessage sendRequestMessage(RequestMessage req) {
		ResponseMessage res = null;
		Socket socket = null;
		try {
			socket = new Socket(ip, port_short);
			OutputStream out = socket.getOutputStream();
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));

			w.write(req.getXML());
			w.flush();
			socket.shutdownOutput();
			res = ResponseMessage.parse(socket.getInputStream());
		} catch (IOException e) {
			callback.onError(e, "displyaSocket:无法连接到指定主机  ");
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
			}
		}
		return res;
	}

	/**
	 * 关闭客户端
	 */
	public void closeClient() {
		callback.onClosing();
		if (displyaSocket != null) {
			try {
				displyaSocket.close();
				displyaSocket = null;
			} catch (IOException e) {

				e.printStackTrace();
				callback.onError(e, "closeClient():关闭displaySocket 失败");
			}

		}

		setClosed(true);
	}

	public interface CallBack {
		/**
		 * 出现异常时
		 * 
		 * @param e
		 * @param description
		 *            描述
		 */
		public void onError(Exception e, String description);

		/**
		 * 切换时
		 */
		public void onSwitch();

		/**
		 * 关闭前
		 */
		public void onClosing();
	}

	/**
	 * 根据目前状态来获取一个Handler
	 * 
	 * @param status
	 * @return
	 */
	public Handler getHandler(int status) {
		switch (status) {
		case Constant.Status_Asking:
			return getAskHandler();
		case Constant.Status_Display:
			return getDisplayHandler();
		case Constant.Status_Downloading:
			return getDownloadHandler();
		case Constant.Status_Identifying:
			return getIdentifyHandler();
		default:
			return null;

		}
	}

	public Socket getDisplyaSocket() {
		return displyaSocket;
	}

	public void setDisplyaSocket(Socket displyaSocket) {
		this.displyaSocket = displyaSocket;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}


	public int getPort_long() {
		return port_long;
	}

	public void setPort_long(int port_long) {
		this.port_long = port_long;
	}

	public int getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	public boolean isInitIP() {
		return isInitIP;
	}

	public void setInitIP(boolean isInitIP) {
		this.isInitIP = isInitIP;
	}

	public boolean isInitHandler() {
		return isInitHandler;
	}

	public void setInitHandler(boolean isInitHandler) {
		this.isInitHandler = isInitHandler;
	}

	public boolean isInitCallBack() {
		return isInitCallBack;
	}

	public void setInitCallBack(boolean isInitCallBack) {
		this.isInitCallBack = isInitCallBack;
	}
	

	public void setCallback(AbstractClient.CallBack callback) {
		if (callback == null)
			throw new IllegalArgumentException(
					"AbstractClient.CallBack can't be NULL!");
		this.callback = callback;
		isInitCallBack = true;
		setInit();
	}

	public DisplayHandler getDisplayHandler() {
		return displayHandler;
	}

	public void setDisplayHandler(DisplayHandler displayHandler) {
		this.displayHandler = displayHandler;
	}

	public AskHandler getAskHandler() {
		return askHandler;
	}

	public void setAskHandler(AskHandler askHandler) {
		this.askHandler = askHandler;
	}

	public DownloadHandler getDownloadHandler() {
		return downloadHandler;
	}

	public void setDownloadHandler(DownloadHandler downloadHandler) {
		this.downloadHandler = downloadHandler;
	}

	public IdentifyHandler getIdentifyHandler() {
		return identifyHandler;
	}

	public void setIdentifyHandler(IdentifyHandler identifyHandler) {
		this.identifyHandler = identifyHandler;
	}

	public int getPort_short() {
		return port_short;
	}


	/**
	 * 初始化ip 和 port
	 * @param IP
	 * @param long_port
	 * @param short_port
	 */
	public void setIP(String IP, int port_long, int port_short) {
		this.ip = IP;
		this.port_long = port_long;
		this.port_short = port_short;
		isInitIP = true;
		setInit();
	}
	
	private void setInit(){
		if(isInitIP && isInitCallBack &&isInitHandler){
			isInit = true;
		}
	}
}
