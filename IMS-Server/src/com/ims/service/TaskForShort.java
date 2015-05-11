package com.ims.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ims.common.XMLFactory;
import com.ims.entity.MeetingData;
import com.ims.entity.Problem;
import com.ims.entity.RequestMessage;
import com.ims.entity.ResponseMessage;
import com.ims.file.MeetingDataFile;
import com.ims.file.ProblemFile;

/**
 * 任务(用户向客户端发送) 1.可以设置回调函数Task.CallBack 2.接受socket，发送图片，如果和Socket
 * 断开就关闭socket，调用回调函数
 * 
 * @author Jayin Ton
 * 
 */
public class TaskForShort implements Runnable {

	private CallBack callback;
	private Socket socket;
	int socketId;

	XMLFactory xmlFactory = new XMLFactory();

	public TaskForShort(Socket _socket, int _socketId, CallBack _callback) {
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

	// 接收请求，返回响应
	@Override
	public void run() {

		try {
			RequestMessage rqmg = RequestMessage.parse(socket.getInputStream());
			System.out.println("Problem ->"+rqmg);
			if (rqmg.getRequestType() == RequestMessage.REQUEST_TYPE_PROBLEM) {
				Problem p = new Problem(rqmg.getUsername(), rqmg.getProblemCtn());
				ProblemFile fileM = new ProblemFile();
				fileM.create(p);
				
				ResponseMessage r = new ResponseMessage();
				r.setState(ResponseMessage.RESPONSE_STATE_SUCCEED);
				
				OutputStream out = socket.getOutputStream();
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));
				
				System.out.println("Response -> "+r.getXML());
				w.write(r.getXML());
				w.flush();
			}
			
			
			if(rqmg.getRequestType() == RequestMessage.REQUEST_TYPE_DOWNLOAD_MEETING_DATA){
				
			
				MeetingDataFile fileM = new MeetingDataFile();
				System.out.println("下载会议资料2");
				MeetingData meetingData = fileM.getMeetingData();
				System.out.println("下载会议资料"+meetingData);
				ResponseMessage r = new ResponseMessage();
				r.setState(ResponseMessage.RESPONSE_STATE_SUCCEED);
				r.setTitle(meetingData.getTitle());
				r.setMeetingData(meetingData.getMeetingData());
				
				OutputStream out = socket.getOutputStream();
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));
				
				System.out.println("响应---->>>"+r.getXML());
				w.write(r.getXML());
				w.flush();
			}
			
			if(rqmg.getRequestType() == RequestMessage.REQUEST_TYPE_IDENTITY){
				
				
				
				ResponseMessage r = new ResponseMessage();
				r.setState(ResponseMessage.RESPONSE_STATE_SUCCEED);
				
				OutputStream out = socket.getOutputStream();
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));
				
				System.out.println("响应---->>>"+r.getXML());
				w.write(r.getXML());
				w.flush();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 最后要调用回调函数
		callback.Call(socketId);
	}

}