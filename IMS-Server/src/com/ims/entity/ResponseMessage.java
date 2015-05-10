package com.ims.entity;

import java.io.InputStream;

import com.ims.common.XMLFactory;

/**
 * 参考RequestMessage
 * 
 * @author Administrator
 * 
 */
public class ResponseMessage {

	// 响应状态
	public static int RESPONSE_STATE_SUCCEED = 200;
	public static int RESPONSE_STATE_FAIL = 400;

	private int state = 400;
	private String title;
	private String meetingData;

	public static String[] attribute = { "state","title", "meetingData" };

	public ResponseMessage(String[] attribute) {
		state = Integer.parseInt(attribute[0]);// 排序和attribute 一样的
		title = attribute[1];
		meetingData = attribute[2];
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ResponseMessage() {
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMeetingData() {
		return meetingData;
	}

	public void setMeetingData(String meetingData) {
		this.meetingData = meetingData;
	}

	/**
	 * 返回由字段完成的xml 数据，为空的不应该加入
	 * 
	 * @return
	 */
	public String getXML() {
		String xml = "";

		xml += "<ResponseMessage><state>" + state + "</state>";

		
		if (title != null) {
			xml += "<title>" + title + "</title>";
		}
		
		if (meetingData != null) {
			xml += "<meetingData>" + meetingData + "</meetingData>";
		}
		xml += "</ResponseMessage>";

		return xml;
	}

	public static ResponseMessage parse(InputStream in) {
		
		ResponseMessage rpmg = null;
		XMLFactory xmlFactory = new XMLFactory();
		rpmg = (ResponseMessage) xmlFactory.getMessageObject(in);
		return rpmg;
	}

	@Override
	public String toString() {
		return "ResponseMessage [state=" + state + ", meetingData="
				+ meetingData + "]";
	}

}
