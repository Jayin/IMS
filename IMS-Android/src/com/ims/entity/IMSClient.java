package com.ims.entity;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ims.utils.Constant;

/**
 * 客户端 AbstractClient实现类 NOTE: 1.单例模式，为了防止意外，还是加了个reSet函数。。 2.必须设置回调函数 不会然会出错
 * 3，全部和服务端通信都应该通过这个类完成。就是说在这个类上封装方法 4.必须设置IP地址
 * 
 * <p>
 * how to use?
 * </p>
 * <p>
 * 1.在onCreate()初始化(回调函数，)，handler,start()
 * </p>
 * <p>
 * 2.在onResume()控制其状态（影响到了他的回调的方法）
 * </p>
 * <P>
 * 3.在onPause()把其状态变为waitting 以免资源浪费
 * </p>
 * <p>
 * 4.切换时，必须改变其状态
 * </p>
 * 11.2 用户行为只保存2个 display + waitting
 * 
 * @author Jayin Ton
 * 
 */
public class IMSClient extends AbstractClient {

	private static IMSClient mImsClient = null;

	private boolean isIdentity = false;
	private boolean isDownloadMeetingData = false;
	private boolean isDisplay = false;
	//是否正在同步图片
	private boolean isSyncingImage = true;

	private String askPro;

	public boolean isDisplay() {
		return isDisplay;
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	/**
	 * 获得已存在的Client,使用时要注意！
	 * 
	 * @return
	 */
	public static IMSClient getExistInstance() {
		if (mImsClient == null)
			throw new IllegalStateException("client还没实例化，请使用getInstance()");
		return mImsClient;

	}

	/*
	 * if(mImsClient!=null) mImsClient.interrupt(); //打断线程
	 */
	public void reSet() {
		if (mImsClient == null
				|| mImsClient.getState() == Thread.State.TERMINATED
				|| mImsClient.getState() == Thread.State.WAITING) {
			try {
				if (mImsClient != null)
					mImsClient.interrupt(); // 打断线程
			} catch (Exception e) {

			} finally {
				mImsClient = null;
			}

		}
	}

	public static IMSClient getInstance() {
		if (mImsClient == null)
			mImsClient = new IMSClient();
		return mImsClient;
	}

	public static IMSClient getInstance(AbstractClient.CallBack callback,
			String ip, int port_long, int port_short) {
		if (mImsClient == null)
			mImsClient = new IMSClient(callback, ip, port_long, port_short);
		return mImsClient;
	}

	/**
	 * 用默认端口
	 * 
	 * @param callback
	 * @param ip
	 * @return
	 */
	public static IMSClient getInstance(AbstractClient.CallBack callback,
			String ip) {
		if (mImsClient == null)
			mImsClient = new IMSClient(callback, ip);
		return mImsClient;
	}

	private IMSClient(AbstractClient.CallBack callback, String ip,
			int port_long, int port_short) {
		super(callback, ip, port_long, port_short);
	}

	private IMSClient(AbstractClient.CallBack callback, String ip) {
		super(callback, ip);
	}

	private IMSClient() {
		super();
	}

	public boolean isDownloadMeetingData() {
		return isDownloadMeetingData;
	}

	public void setDownloadMeetingData(boolean isDownloadMeetingData) {
		this.isDownloadMeetingData = isDownloadMeetingData;
	}

	public boolean isIdentity() {
		return isIdentity;
	}

	public void setIdentity(boolean isIdentity) {
		this.isIdentity = isIdentity;
	}

	@Override
	public void downloadData() {

		RequestMessage request = new RequestMessage();
		request.setRequestType(RequestMessage.REQUEST_TYPE_DOWNLOAD_MEETING_DATA);
		ResponseMessage response = sendRequestMessage(request);
		Handler h = getDownloadHandler();
		Message msg = h.obtainMessage();
		msg.obj = response;
		h.sendMessage(msg);
		setDownloadMeetingData(true);

	}

	@Override
	public void display() {
		init();
		setDisplay(true);
		Handler h = getDisplayHandler();
		if (h == null)
			throw new IllegalArgumentException("DisplayHandler is null!");
		try {

			while (true) {
				if(isSyncingImage){
					Bitmap bitmap = BitmapFactory.decodeStream(getDisplyaSocket()
							.getInputStream());
					Log.i("debug", "获得bitmap! ");

					Message msg = h.obtainMessage();
					msg.obj = bitmap;
					h.sendMessage(msg);
					Log.i("debug", "发送bitmap! ");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			callback.onError(e, "IMSClient:run():display():IOException:链接断开");
		} catch (NullPointerException e) {
			e.printStackTrace();
			callback.onError(e,
					"IMSClient:run():display():NullPointerException:getDisplyaSocket()is null");
		}
	}

	/**
	 * 认证客户端信息
	 */
	@Override
	public boolean identity() {
		RequestMessage request = new RequestMessage();
		request.setRequestType(RequestMessage.REQUEST_TYPE_DOWNLOAD_MEETING_DATA);
		ResponseMessage response = sendRequestMessage(request);
		Handler h = getIdentifyHandler();
		Message msg = h.obtainMessage();
		if (response.getState() == ResponseMessage.RESPONSE_STATE_SUCCEED) {
			msg.obj = "登陆成功！";
		} else {
			msg.obj = "登陆失败！";
		}

		h.sendMessage(msg);
		setIdentity(true);
		return true;

	}

	/**
	 * 提交问题
	 */
	public boolean ask(String problem) {

		return false;
	}

	public boolean ask() {
		RequestMessage request = new RequestMessage();
		request.setRequestType(RequestMessage.REQUEST_TYPE_PROBLEM);
		request.setProblemCtn(askPro);
		request.setUsername(username);
		ResponseMessage response = sendRequestMessage(request);
//		Handler h = getIdentifyHandler();
//		Message msg = h.obtainMessage();
//		if (response.getState() == ResponseMessage.RESPONSE_STATE_SUCCEED) {
//			msg.obj = "登陆成功！";
//		} else {
//			msg.obj = "登陆失败！";
//		}
//
//		h.sendMessage(msg);
		return true;
	}

	@Override
	public void waitting() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			callback.onError(e,
					"IMSClient:run():waitting():InterruptedException:睡死了");
		}
	}

	@Override
	public void run() {
		if (!isInit) {
			throw new IllegalArgumentException("IP | Port | CallBack 还没有设置 ");
		}
		while (!isClosed()) {
			switch (getTask()) {
			case Constant.Status_Display:
				new Thread(new Runnable() {
					@Override
					public void run() {
						display();
					}
				}).start();
				break;
			case Constant.Status_Waitting:
				waitting();
				break;
			case Constant.Status_Identifying:
				new Thread(new Runnable() {

					@Override
					public void run() {
						identity();
					}
				}).start();
				break;
			case Constant.Status_Downloading:
				new Thread(new Runnable() {

					@Override
					public void run() {
						downloadData();
					}
				}).start();
				break;
			case Constant.Status_Asking:
				new Thread(new Runnable() {

					@Override
					public void run() {
						ask();
					}
				}).start();
				break;


			}
		}
		Log.i("debug", "run() finishss");
	}

	public void setAskPro(String pro) {
		this.askPro = pro;
	}

	public boolean isSyncingImage() {
		return isSyncingImage;
	}

	public void setSyncingImage(boolean isSyncingImage) {
		this.isSyncingImage = isSyncingImage;
	}
	
	
}
